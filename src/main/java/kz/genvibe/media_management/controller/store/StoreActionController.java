package kz.genvibe.media_management.controller.store;

import jakarta.validation.Valid;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.domain.dto.store.StoreCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreActionController {

    private final StoreService storeService;

    @PostMapping
    public String createStore(
        @ModelAttribute @Valid StoreCreateDto dto,
        @CurrentUser AppUser appUser
    ) {
        storeService.addStore(appUser, dto);
        return "redirect:/stores";
    }

    @PostMapping("/activate/{id}")
    public String activateStore(
        @PathVariable long id,
        @CurrentUser AppUser appUser
    ) {
        storeService.activateStore(id, appUser);
        return "redirect:/stores";
    }

}
