package kz.genvibe.media_management.service.internal.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.genvibe.media_management.config.props.AppProps;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final AppProps appProps;

    @Override
    @Transactional(readOnly = true)
    public List<Store> getAllStores(AppUser appUser) {
        return storeRepository.findAllByOrganization(appUser.getOrganization());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> getAllStoresByAppUserAndNames(AppUser appUser, List<String> names) {
        return storeRepository.findStoresByOrganizationAndNameIn(appUser.getOrganization(), names);
    }

    @Override
    @Transactional
    public void addStore(AppUser appUser, StoreCreateDto dto) {
        var store = Store.builder()
            .name(dto.name())
            .location(dto.location())
            .email(dto.email())
            .organization(appUser.getOrganization())
            .build();

        storeRepository.save(store);
        log.info("Added store: {} for user: {}", dto.name(), appUser.getEmail());
    }

    @Override
    @Transactional
    public void activateStore(long id, AppUser appUser) {
        var store = storeRepository.findStoreByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        var uuid = UUID.randomUUID();

        store.setActive(true);
        store.setMusicLinkUuid(uuid);
        store.setMusicLink(generateMusicAccessLink(id, uuid));

        log.info("Activated store: {} with id: {}", store.getName(), id);
    }

    @Override
    @Transactional
    public String regenerateMusicAccessLink(long id, AppUser appUser) {
        var store = storeRepository.findStoreByIdAndOrganization(id, appUser.getOrganization())
            .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        var newUuid = UUID.randomUUID();
        String newLink = generateMusicAccessLink(id, newUuid);

        store.setMusicLinkUuid(newUuid);
        store.setMusicLink(newLink);

        log.info("Regenerated music access link for store: {} with id: {}", store.getName(), id);

        return newLink;
    }

    private String generateMusicAccessLink(long id, UUID uuid) {
        return appProps.getBaseUrl() + "/stores/" + id + "/" + uuid;
    }

}
