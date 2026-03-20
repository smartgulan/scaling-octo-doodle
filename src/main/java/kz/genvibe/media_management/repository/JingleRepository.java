package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.Jingle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JingleRepository extends JpaRepository<Jingle, Long> {
}
