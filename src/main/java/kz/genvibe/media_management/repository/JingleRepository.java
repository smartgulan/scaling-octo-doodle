package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Jingle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JingleRepository extends JpaRepository<Jingle, Long> {
    @EntityGraph(attributePaths = "stores")
    List<Jingle> findJinglesByAppUser(AppUser appUser);

    @EntityGraph(attributePaths = "stores")
    List<Jingle> findJinglesByAppUserAndRequestedToPauseIsTrue(AppUser appUser);
}
