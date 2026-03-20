package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.UpdateEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store extends UpdateEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    @Column(length = 254, nullable = false)
    private String email;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean active = false;

    @Column(columnDefinition = "varchar default null")
    private String musicLink;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @ManyToMany(mappedBy = "stores")
    private Set<Jingle> jingles = new HashSet<>();

}
