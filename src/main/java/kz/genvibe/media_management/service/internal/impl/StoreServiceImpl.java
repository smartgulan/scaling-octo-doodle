package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.model.domain.dto.store.StoreCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.repository.StoreRepository;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Store> getAllStores(AppUser appUser) {
        return storeRepository.findAllByAppUser(appUser);
    }

    @Override
    @Transactional
    public void addStore(AppUser appUser, StoreCreateDto dto) {
        var store = Store.builder()
            .name(dto.name())
            .location(dto.location())
            .email(dto.email())
            .appUser(appUser)
            .build();

        storeRepository.save(store);
        log.info("Added store: {} for user: {}", dto.name(), appUser.getEmail());
    }

    @Override
    @Transactional
    public void activateStore(long id, AppUser appUser) {
        var store = storeRepository.findStoreByIdAndAppUser(id, appUser)
            .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        store.setActive(true);
        store.setMusicLink(generateMusicAccessLink());
        log.info("Activated store: {} with id: {}", store.getName(), id);
    }

    private String generateMusicAccessLink() {
        return "";
    }

}
