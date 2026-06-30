package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import kz.genvibe.media_management.model.enums.JingleSlotStatus;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "jingle_slots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JingleSlot extends CreateEntity {

    /** Absolute moment the jingle must play, resolved from the store's wall-clock cadence in its zone. */
    @Column(nullable = false)
    private Instant playTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jingle_id", nullable = false)
    private Jingle jingle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jingle_schedule_id", nullable = false)
    private JingleSchedule jingleSchedule;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JingleSlotStatus status = JingleSlotStatus.PENDING;

}
