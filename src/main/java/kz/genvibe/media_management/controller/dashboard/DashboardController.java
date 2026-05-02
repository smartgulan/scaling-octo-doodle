package kz.genvibe.media_management.controller.dashboard;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.model.entity.Store;
import kz.genvibe.media_management.service.internal.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final MusicService musicService;

    @GetMapping
    public String dashboard(Model model, @CurrentUser AppUser appUser) {
        final var stores = appUser.getOrganization().getStores();
        final var activeStores = stores.stream()
            .filter(Store::isActive)
            .collect(Collectors.toSet());
        final var organization = appUser.getOrganization();

        model.addAttribute("stores", stores);
        model.addAttribute("activeStores", activeStores);
        model.addAttribute("atmosphere", new Atmosphere("Warm & Welcoming", "guitar, bass guitar, keys, piano.  BPM range: 102-110"));
        model.addAttribute("dailyPlaysAndMinutes", new DailyPlays(5420, "17%"));
        model.addAttribute("musicPreviewUrl", musicService.getMusicPreviewUrl(organization));

        return organization.hasEnhancedDashboardView() ? "pages/enhanced-dashboard" : "pages/dashboard";
    }

    record Atmosphere(String name, String description) {}
    record DailyPlays(int minutes, String plays) {}

}
