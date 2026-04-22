package kz.genvibe.media_management.repository;

import kz.genvibe.media_management.model.entity.JingleSchedule;
import kz.genvibe.media_management.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JingleScheduleRepository extends JpaRepository<JingleSchedule, Long> {
    Optional<JingleSchedule> findJingleScheduleByOrganization(Organization organization);
}
