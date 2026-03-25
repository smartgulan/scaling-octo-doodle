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
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser extends UpdateEntity {

    @Column(length = 160, nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(length = 160, nullable = false)
    private BusinessType businessType;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private MusicProvider musicProvider;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<BrandIdentity> brandIdentity = new ArrayList<>();

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<CurrentFeel> currentFeel = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 128, nullable = false)
    private SpacePurpose spacePurpose;

    @Enumerated(EnumType.STRING)
    @Column(length = 128, nullable = false)
    private PlaytimeWindow playtimeWindow;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean onboardingCompleted = false;

    @Column
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole role = UserRole.ROLE_USER;

    @Column(unique = true, length = 254, nullable = false)
    private String email;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean emailChanged = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_verification_token_id")
    private EmailVerificationToken emailVerificationToken;

    @Builder.Default
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean emailVerified = false;

    @Builder.Default
    @Column(columnDefinition = "boolean default true", nullable = false)
    private boolean enabled = true;

    @Column
    private String fullName;

    @Column
    private String companyRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_type_id", nullable = true)
    private MusicType musicType;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Store> stores = new HashSet<>();

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Jingle> jingles = new HashSet<>();

}
