package kz.genvibe.media_management.model.entity.analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import lombok.Getter;

@Getter
@Entity
@Table(name = "jingle_aggregate_analytics_data")
public class JingleAggregateAnalyticsData extends BaseEntity {
    private int totalJingleBroadcasts;
    private int totalJingleBroadcastsWeekGrowth;
    private int completionRate;
    private String mostPlayedJingleName;
    private int mostPlayedJinglePlayCount;
    private String scheduledJingleTaskName;
    private int scheduledJingleTaskCount;
}
