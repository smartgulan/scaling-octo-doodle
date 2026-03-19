package kz.genvibe.media_management.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.BaseEntity;
import lombok.*;

@Entity
@Table(name = "music_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MusicType extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String iconName;

    @Column(nullable = false)
    private String previewUrl;

}
