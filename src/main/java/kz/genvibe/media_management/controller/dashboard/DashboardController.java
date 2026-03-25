package kz.genvibe.media_management.controller.dashboard;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;

    @GetMapping
    public String dashboard(Model model, @CurrentUser AppUser appUser) {
        model.addAttribute("musicType", appUser.getMusicTypes());
        model.addAttribute("stores", appUser.getStores());
        return "pages/dashboard";
    }

}
