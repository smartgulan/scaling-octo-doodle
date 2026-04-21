package kz.genvibe.media_management.model.entity;

import jakarta.persistence.*;
import kz.genvibe.media_management.model.entity.base.UpdateEntity;
import kz.genvibe.media_management.model.enums.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization extends UpdateEntity {

    @Column(length = 160, nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(length = 160, nullable = false)
    private BusinessType businessType;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private MusicProvider musicProvider;

    @Builder.Default
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Enumerated(EnumType.STRING)
    private List<BrandIdentity> brandIdentity = new ArrayList<>();

    @Builder.Default
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Enumerated(EnumType.STRING)
    private List<CurrentFeel> currentFeel = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 128, nullable = false)
    private SpacePurpose spacePurpose;

    @Enumerated(EnumType.STRING)
    @Column(length = 128, nullable = false)
    private PlaytimeWindow playtimeWindow;

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppUser> appUsers = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "organization_music_types",
        joinColumns = @JoinColumn(name = "organization_id"),
        inverseJoinColumns = @JoinColumn(name = "music_type_id")
    )
    private Set<MusicType> musicTypes = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Store> stores = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Jingle> jingles = new HashSet<>();

}
