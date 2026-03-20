package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.MusicType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MusicTypeRepository extends JpaRepository<MusicType, Long> {
    Optional<MusicType> findByName(String name);
}
