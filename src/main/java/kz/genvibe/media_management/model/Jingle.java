package kz.genvibe.media_management.model;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import kz.genvibe.media_management.model.enums.JingleCategory;
import kz.genvibe.media_management.model.enums.JingleVoice;
import lombok.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jingles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Jingle extends CreateEntity {

    @Column(nullable = false)
    private JingleVoice voice;

    @Column(nullable = false)
    private JingleCategory category;

    @Column(length = 500, nullable = false)
    private String announcementText;

    @Column(nullable = false)
    private Duration duration;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "jingle_stores",
        joinColumns = @JoinColumn(name = "jingle_id"),
        inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores = new HashSet<>();

}
