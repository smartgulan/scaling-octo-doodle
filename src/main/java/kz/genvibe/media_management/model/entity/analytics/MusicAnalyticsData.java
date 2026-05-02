package kz.genvibe.media_management.model.entity.analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.Map;

@Getter
@Entity
@Table(name = "music_analytics_data")
@Immutable
public class MusicAnalyticsData extends BaseEntity {
//    private Map<String, Integer> weeklyPlays;
    private int totalPlays;
    private int totalPlaysGrowthPercentage;
    private int averageDailyPlays;
    private String topStoreName;
    private int topStorePlaysGrowthPercentage;
}
