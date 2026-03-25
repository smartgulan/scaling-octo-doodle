package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.MusicType;

import java.util.List;
import java.util.Set;

public interface MusicService {
    List<MusicType> getAllMusicTypes();
    MusicType getMusicTypeByName(String musicTypeName);
    Set<MusicType> getMusicTypeByNames(List<String> musicTypeNames);
}
