package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.model.entity.MusicType;
import kz.genvibe.media_management.repository.MusicTypeRepository;
import kz.genvibe.media_management.service.internal.MusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicServiceImpl implements MusicService {

    private final MusicTypeRepository musicTypeRepository;

    @Override
    public List<MusicType> getAllMusicTypes() {
        return musicTypeRepository.findAll();
    }

    @Override
    public MusicType getMusicTypeByName(String musicTypeName) {
        return musicTypeRepository.findByName(musicTypeName)
            .orElseThrow(() -> new EntityNotFoundException("Music type not found for name: " + musicTypeName));
    }

    @Override
    public Set<MusicType> getMusicTypesByNames(List<String> musicTypeNames) {
        return musicTypeRepository.findMusicTypesByNameIn(musicTypeNames);
    }

}
