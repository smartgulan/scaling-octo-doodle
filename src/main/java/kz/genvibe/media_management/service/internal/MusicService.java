package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.Music;

import java.util.List;

public interface MusicService {
    List<Music> getAllDistinctMusic();
}
