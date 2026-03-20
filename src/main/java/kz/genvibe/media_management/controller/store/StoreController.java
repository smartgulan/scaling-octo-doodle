package kz.genvibe.media_management.controller.store;

import kz.genvibe.media_management.service.internal.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public String storesPage(Principal principal, Model model) {
        var email = principal.getName();
        model.addAttribute("stores", storeService.getAllStores(email));
        return "pages/stores";
    }

}
