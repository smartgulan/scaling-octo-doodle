DROP MATERIALIZED VIEW IF EXISTS music_analytics_data_view;

CREATE MATERIALIZED VIEW music_analytics_data_view AS
WITH daily_stats AS (
    SELECT
        s.organization_id,
        sad.snapshot_date,
        SUM(sad.play_minutes) as daily_minutes
    FROM store_analytics_data sad
             JOIN stores s ON sad.store_name = s.name
    GROUP BY s.organization_id, sad.snapshot_date
),
     weekly_map AS (
         SELECT
             organization_id,
             jsonb_object_agg(trim(to_char(snapshot_date, 'Day')), daily_minutes) as weekly_plays_map
         FROM daily_stats
         WHERE snapshot_date >= CURRENT_DATE - INTERVAL '7 days'
         GROUP BY organization_id
     ),
     growth_calc AS (
         SELECT
             organization_id,
             SUM(daily_minutes) FILTER (WHERE snapshot_date >= CURRENT_DATE - INTERVAL '7 days') as total_now,
             SUM(daily_minutes) FILTER (WHERE snapshot_date < CURRENT_DATE - INTERVAL '7 days'
                 AND snapshot_date >= CURRENT_DATE - INTERVAL '14 days') as total_prev
         FROM daily_stats
         GROUP BY organization_id
     ),
     top_store_per_org AS (
         SELECT DISTINCT ON (s.organization_id)
             s.organization_id,
             sad.store_name,
             SUM(sad.play_minutes) as ts_total
         FROM store_analytics_data sad
                  JOIN stores s ON sad.store_name = s.name
         WHERE sad.snapshot_date >= CURRENT_DATE - INTERVAL '7 days'
         GROUP BY s.organization_id, sad.store_name
         ORDER BY s.organization_id, ts_total DESC
     )
SELECT
    ob.id as id,
    ob.id as organization_id,
    CURRENT_DATE as snapshot_date, -- Дата генерации аналитики
    COALESCE(wm.weekly_plays_map, '{}'::jsonb) as weekly_plays,
    COALESCE(gc.total_now, 0) as total_plays,
    CASE
        WHEN COALESCE(gc.total_prev, 0) = 0 AND COALESCE(gc.total_now, 0) > 0 THEN 100
        WHEN COALESCE(gc.total_prev, 0) > 0
            THEN ((gc.total_now::float - gc.total_prev::float) * 100 / gc.total_prev::float)::int
        ELSE 0
        END as total_plays_growth_percentage,
    ROUND(COALESCE(gc.total_now, 0)::float / 7)::int as average_daily_plays,
    COALESCE(tsp.store_name, 'N/A') as top_store_name,
    0 as top_store_plays_growth_percentage
FROM organizations ob
         LEFT JOIN weekly_map wm ON ob.id = wm.organization_id
         LEFT JOIN growth_calc gc ON ob.id = gc.organization_id
         LEFT JOIN top_store_per_org tsp ON ob.id = tsp.organization_id
WITH DATA;

CREATE UNIQUE INDEX idx_music_analytics_org_id ON music_analytics_data_view (id);


DROP MATERIALIZED VIEW IF EXISTS store_aggregate_analytics_data_view;

CREATE MATERIALIZED VIEW store_aggregate_analytics_data_view AS
WITH slot_with_org AS (
    SELECT
        st.organization_id,
        js.status,
        js.jingle_id,
        js.created_at
    FROM jingle_slots js
             JOIN jingle_schedules jsch ON js.jingle_schedule_id = jsch.id
             JOIN stores st ON jsch.store_id = st.id
),
     org_counts AS (
         SELECT
             organization_id,
             COUNT(*) FILTER (WHERE status = 'PLAYED') as total_played,
             COUNT(*) FILTER (WHERE status = 'PLAYED' AND created_at >= CURRENT_DATE - INTERVAL '7 days') as played_this_week,
             COUNT(*) FILTER (WHERE status = 'PLAYED' AND created_at < CURRENT_DATE - INTERVAL '7 days'
                 AND created_at >= CURRENT_DATE - INTERVAL '14 days') as played_last_week,
             COUNT(*) FILTER (WHERE status = 'PLAYED') * 100 /
             NULLIF(COUNT(*) FILTER (WHERE status IN ('PLAYED', 'MISSED', 'FAILED')), 0) as rate
         FROM slot_with_org
         GROUP BY organization_id
     ),
     top_jingle_per_org AS (
         SELECT DISTINCT ON (organization_id)
             organization_id, announcement_text, play_count
         FROM (
                  SELECT swo.organization_id, j.announcement_text, COUNT(*) as play_count
                  FROM slot_with_org swo
                           JOIN jingles j ON swo.jingle_id = j.id
                  WHERE swo.status = 'PLAYED'
                  GROUP BY swo.organization_id, j.announcement_text
              ) sub ORDER BY organization_id, play_count DESC
     ),
     top_task_per_org AS (
         SELECT DISTINCT ON (organization_id)
             organization_id, task_name, task_count
         FROM (
                  SELECT swo.organization_id, j.category::text as task_name, COUNT(*) as task_count
                  FROM slot_with_org swo
                           JOIN jingles j ON swo.jingle_id = j.id
                  GROUP BY swo.organization_id, j.category
              ) sub ORDER BY organization_id, task_count DESC
     )
SELECT
    ob.id as id,
    ob.id as organization_id,
    CURRENT_DATE as snapshot_date,
    COALESCE(oc.total_played, 0) as total_jingle_broadcasts,
    CASE
        WHEN COALESCE(oc.played_last_week, 0) = 0 AND COALESCE(oc.played_this_week, 0) > 0 THEN 100
        WHEN COALESCE(oc.played_last_week, 0) > 0
            THEN ((oc.played_this_week::float - oc.played_last_week::float) * 100 / oc.played_last_week::float)::int
        ELSE 0
        END as total_jingle_broadcasts_week_growth,
    COALESCE(oc.rate, 0) as completion_rate,
    COALESCE(tj.announcement_text, 'No data') as most_played_jingle_name,
    COALESCE(tj.play_count, 0) as most_played_jingle_play_count,
    COALESCE(tt.task_name, 'No data') as scheduled_jingle_task_name,
    COALESCE(tt.task_count, 0) as scheduled_jingle_task_count
FROM organizations ob
         LEFT JOIN org_counts oc ON ob.id = oc.organization_id
         LEFT JOIN top_jingle_per_org tj ON ob.id = tj.organization_id
         LEFT JOIN top_task_per_org tt ON ob.id = tt.organization_id
WITH DATA;

CREATE UNIQUE INDEX idx_store_aggregate_org_id ON store_aggregate_analytics_data_view (id);


DROP MATERIALIZED VIEW IF EXISTS jingle_aggregate_analytics_data_view;

CREATE MATERIALIZED VIEW jingle_aggregate_analytics_data_view AS
WITH organization_base AS (
    SELECT id AS org_id FROM organizations
),
     slot_data AS (
         SELECT
             s.organization_id,
             js.status,
             js.jingle_id,
             js.created_at,
             js.play_time
         FROM jingle_slots js
                  JOIN jingle_schedules jsch ON js.jingle_schedule_id = jsch.id
                  JOIN stores s ON jsch.store_id = s.id
     ),
     org_counts AS (
         SELECT
             organization_id,
             count(*) FILTER (WHERE status = 'PLAYED') AS total_played,
             count(*) FILTER (WHERE status = 'PLAYED' AND created_at >= CURRENT_DATE - INTERVAL '7 days') AS played_this_week,
             count(*) FILTER (WHERE status = 'PLAYED' AND created_at < CURRENT_DATE - INTERVAL '7 days'
                 AND created_at >= CURRENT_DATE - INTERVAL '14 days') AS played_last_week,
             count(*) FILTER (WHERE status = 'PLAYED') * 100 /
             NULLIF(count(*) FILTER (WHERE status IN ('PLAYED', 'MISSED', 'FAILED')), 0) AS rate
         FROM slot_data
         GROUP BY organization_id
     ),
     top_jingles AS (
         SELECT DISTINCT ON (organization_id)
             organization_id, announcement_text, cnt
         FROM (
                  SELECT sd.organization_id, j.announcement_text, count(*) AS cnt
                  FROM slot_data sd
                           JOIN jingles j ON sd.jingle_id = j.id
                  WHERE sd.status = 'PLAYED'
                  GROUP BY sd.organization_id, j.announcement_text
              ) sub
         ORDER BY organization_id, cnt DESC
     ),
     upcoming_tasks AS (
         SELECT
             organization_id,
             FIRST_VALUE(announcement_text) OVER (PARTITION BY organization_id ORDER BY play_time ASC) AS nearest_jingle_name,
             count(*) FILTER (WHERE status = 'PENDING' AND play_time >= NOW() AND play_time < (CURRENT_DATE + INTERVAL '1 day')) AS remaining_today_count
         FROM (
                  SELECT sd.organization_id, sd.status, sd.play_time, j.announcement_text
                  FROM slot_data sd
                           JOIN jingles j ON sd.jingle_id = j.id
                  WHERE sd.status = 'PENDING' AND sd.play_time >= NOW()
              ) sub_p
         GROUP BY organization_id, announcement_text, play_time
     )
SELECT
    ob.org_id AS id,
    ob.org_id AS organization_id,
    CURRENT_DATE as snapshot_date,
    COALESCE(oc.total_played, 0) AS total_jingle_broadcasts,
    CASE
        WHEN COALESCE(oc.played_last_week, 0) = 0 AND COALESCE(oc.played_this_week, 0) > 0 THEN 100
        WHEN COALESCE(oc.played_last_week, 0) > 0
            THEN ((oc.played_this_week::float - oc.played_last_week::float) * 100 / oc.played_last_week::float)::int
        ELSE 0
        END AS total_jingle_broadcasts_week_growth,
    COALESCE(oc.rate, 0) AS completion_rate,
    COALESCE(tj.announcement_text, 'No data') AS most_played_jingle_name,
    COALESCE(tj.cnt, 0) AS most_played_jingle_play_count,
    COALESCE((SELECT ut.nearest_jingle_name FROM upcoming_tasks ut WHERE ut.organization_id = ob.org_id LIMIT 1), 'None today') AS scheduled_jingle_task_name,
    COALESCE((SELECT SUM(ut.remaining_today_count) FROM upcoming_tasks ut WHERE ut.organization_id = ob.org_id), 0) AS scheduled_jingle_task_count
FROM organization_base ob
         LEFT JOIN org_counts oc ON ob.org_id = oc.organization_id
         LEFT JOIN top_jingles tj ON ob.org_id = tj.organization_id
WITH DATA;

ALTER MATERIALIZED VIEW jingle_aggregate_analytics_data_view OWNER TO postgres;
CREATE UNIQUE INDEX idx_jingle_aggregate_org_id ON jingle_aggregate_analytics_data_view (id);