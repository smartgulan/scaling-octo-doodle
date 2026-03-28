package kz.genvibe.media_management.controller.dashboard;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping
    public String dashboard(Model model, @CurrentUser AppUser appUser) {
        model.addAttribute("musicType", appUser.getOrganization().getMusicTypes());
        model.addAttribute("stores", appUser.getOrganization().getStores());
        return "pages/dashboard";
    }

}
