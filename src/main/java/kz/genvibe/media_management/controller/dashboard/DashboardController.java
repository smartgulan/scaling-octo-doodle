package kz.genvibe.media_management.controller.dashboard;

import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;

    @GetMapping
    public String dashboard(Principal principal, Model model) {
        var email = principal.getName();
        model.addAttribute("musicType", userService.getUserMusicType(email));
        model.addAttribute("stores", userService.getUserStores(email));
        return "pages/dashboard";
    }

}
