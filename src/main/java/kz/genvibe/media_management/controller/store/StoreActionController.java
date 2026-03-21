package kz.genvibe.media_management.controller.store;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.domain.dto.store.StoreCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreActionController {

    private final StoreService storeService;

    @PostMapping
    public String createStore(
        @ModelAttribute StoreCreateDto storeCreateDto,
        @CurrentUser AppUser appUser
    ) {
        return "redirect:/stores";
    }

}
