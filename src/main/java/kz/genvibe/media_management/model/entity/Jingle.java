package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import kz.genvibe.media_management.model.enums.JingleCategory;
import kz.genvibe.media_management.model.enums.JingleVoice;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "jingles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Jingle extends CreateEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JingleVoice voice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JingleCategory category;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Duration duration;

    @Column(length = 500, nullable = false, updatable = false)
    private String announcementText;

    @Column(nullable = false)
    private String fileUrl;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean requestedToPause = false;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean pauseApproved = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "jingle_stores",
        joinColumns = @JoinColumn(name = "jingle_id"),
        inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores = new HashSet<>();

    @Transient
    public List<String> getStoreNames() {
        return stores.stream()
            .map(Store::getName)
            .collect(Collectors.toList());
    }

}
