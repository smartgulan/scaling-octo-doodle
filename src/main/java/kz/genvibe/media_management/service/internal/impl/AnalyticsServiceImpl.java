package kz.genvibe.media_management.service.internal.impl;

import kz.genvibe.media_management.model.entity.analytics.StoreAnalyticsData;
import kz.genvibe.media_management.repository.StoreRepository;
import kz.genvibe.media_management.repository.analytics.StoreAnalyticsRepository;
import kz.genvibe.media_management.service.internal.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final StoreAnalyticsRepository storeAnalyticsRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public void recordPing(long storeId) {
        final var store = storeRepository.findById(storeId).orElseThrow();
        final var today = LocalDate.now();

        final var stats = storeAnalyticsRepository
            .findByStoreNameAndSnapshotDate(store.getName(), today)
            .orElseGet(() -> {
                final var newData = new StoreAnalyticsData();
                newData.setSnapshotDate(today);
                newData.setStoreName(store.getName());
                newData.setPlayMinutes(0);
                return newData;
            });

        stats.setPlayMinutes(stats.getPlayMinutes() + 1);

        storeAnalyticsRepository.save(stats);
    }

}
