package kz.genvibe.media_management.service.internal.impl;

import kz.genvibe.media_management.model.entity.Music;
import kz.genvibe.media_management.repository.MusicRepository;
import kz.genvibe.media_management.service.internal.MusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicServiceImpl implements MusicService {

    private final MusicRepository musicRepository;

    @Override
    public List<Music> getAllDistinctMusic() {
        return musicRepository.findDistinctBy();
    }

}
