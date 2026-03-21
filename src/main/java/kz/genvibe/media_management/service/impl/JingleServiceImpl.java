package kz.genvibe.media_management.service.impl;

import kz.genvibe.media_management.client.elevenlabs.ElevenlabsClient;
import kz.genvibe.media_management.model.entity.Jingle;
import kz.genvibe.media_management.model.enums.JingleVoice;
import kz.genvibe.media_management.repository.JingleRepository;
import kz.genvibe.media_management.service.JingleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JingleServiceImpl implements JingleService {

    private final JingleRepository jingleRepository;
    private final ElevenlabsClient elevenlabsClient;

    @Override
    public void createJingle() {
    }

    @Override
    public List<Jingle> getJingleHistory() {
        return List.of();
    }

}
