package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findDistinctBy();
}
