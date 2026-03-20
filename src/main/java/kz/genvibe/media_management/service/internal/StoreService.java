package kz.genvibe.media_management.service.internal;

import kz.genvibe.media_management.model.entity.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllStores(String email);
}
