package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Jingle;

import java.util.List;

public interface JingleService {
    void createJingle(AppUser appUser, JingleCreateDto dto);
    List<Jingle> getJingleHistory();
}
