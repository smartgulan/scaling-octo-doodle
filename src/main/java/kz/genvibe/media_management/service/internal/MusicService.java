package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.MusicType;

import java.util.List;

public interface MusicService {
    List<MusicType> getAllMusicTypes();
    MusicType getMusicTypeByName(String musicTypeName);
}
