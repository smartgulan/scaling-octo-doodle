package kz.genvibe.media_management.controller.store;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public String storesPage(
        Model model,
        @CurrentUser AppUser appUser,
        @RequestParam(required = false) boolean storeAdded
    ) {
        model.addAttribute("stores", storeService.getAllStores(appUser));
        return "pages/stores";
    }

}
