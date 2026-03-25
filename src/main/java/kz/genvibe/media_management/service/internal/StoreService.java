package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.domain.dto.store.StoreCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllStores(AppUser appUser);
    void addStore(AppUser appUser, StoreCreateDto dto);
    void activateStore(long id, AppUser appUser);
    String regenerateMusicAccessLink(long id, AppUser appUser);
}
