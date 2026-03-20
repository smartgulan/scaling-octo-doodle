package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.Jingle;

import java.util.List;

public interface JingleService {
    void createJingle();
    List<Jingle> getJingleHistory();
}
