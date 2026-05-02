package kz.genvibe.media_management.repository.analytics;

import kz.genvibe.media_management.model.entity.analytics.StoreAnalyticsData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface StoreAnalyticsRepository extends JpaRepository<StoreAnalyticsData, Long> {
    Optional<StoreAnalyticsData> findByStoreNameAndSnapshotDate(String storeName, LocalDate snapshotDate);
}
