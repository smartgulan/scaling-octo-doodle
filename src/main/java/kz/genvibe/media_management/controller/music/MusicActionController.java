package kz.genvibe.media_management.controller.music;

import jakarta.validation.constraints.Size;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicActionController {

    private final UserService userService;

    @PostMapping
    public String saveMusicType(
        @RequestParam @Size(min = 2) List<String> musicType,
        @CurrentUser AppUser appUser,
        RedirectAttributes redirectAttributes
    ) {
        userService.saveUserMusicTypes(appUser, musicType);
        redirectAttributes.addFlashAttribute("toast", "Music type successfully saved for your company");
        return "redirect:/dashboard";
    }

}
