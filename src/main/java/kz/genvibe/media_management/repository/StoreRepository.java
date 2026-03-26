package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByAppUser(AppUser appUser);
    List<Store> findStoresByAppUserAndNameIn(AppUser appUser, List<String> names);
    Optional<Store> findStoreByIdAndAppUser(Long id, AppUser appUser);
}
