package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
