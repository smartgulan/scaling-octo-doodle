package kz.genvibe.media_management.service.internal.impl;

import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.repository.StoreRepository;
import kz.genvibe.media_management.service.internal.StoreService;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final UserService userService;
    private final StoreRepository storeRepository;

    @Override
    public List<Store> getAllStores(AppUser appUser) {
        return storeRepository.findAllByAppUser(appUser);
    }

}
