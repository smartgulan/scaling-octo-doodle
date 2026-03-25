package kz.genvibe.media_management.controller.music;

import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicActionController {

    private final UserService userService;

    @PostMapping
    public String saveMusicType(
        @RequestParam String musicType,
        @CurrentUser AppUser appUser,
        RedirectAttributes redirectAttributes
    ) {
        userService.saveUserMusicType(appUser, musicType);
        redirectAttributes.addFlashAttribute("toast", "Music type successfully saved for your company");
        return "redirect:/dashboard";
    }

}
