package kz.genvibe.media_management.controller.store;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public String storesPage(
        Model model,
        @CurrentUser AppUser appUser
    ) {
        model.addAttribute("stores", storeService.getAllStores(appUser));
        return "pages/stores";
    }

    @GetMapping("/{id}/{uuid}")
    public String storePage(
        @PathVariable long id,
        @PathVariable UUID uuid,
        @CurrentUser AppUser appUser,
        Model model
    ) {
        if (!storeService.verifyStore(id, uuid, appUser)) {
            return "error/404";
        }

        model.addAttribute("store", storeService.getStoreById(id));
        model.addAttribute("organization", appUser.getOrganization());
        return "pages/store-dashboard";
    }

}
