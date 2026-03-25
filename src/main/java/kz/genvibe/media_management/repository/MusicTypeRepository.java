package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.MusicType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MusicTypeRepository extends JpaRepository<MusicType, Long> {
    Optional<MusicType> findByName(String name);

    Set<MusicType> findMusicTypesByNameIn(List<String> names);
}
