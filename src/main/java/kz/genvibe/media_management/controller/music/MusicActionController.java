package kz.genvibe.media_management.controller.music;

import kz.genvibe.media_management.service.internal.MusicService;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicActionController {

    private final UserService userService;
    private final MusicService musicService;

    @PostMapping
    public String saveMusicType(@RequestParam String musicType, Principal principal) {
        var email = principal.getName();
        userService.saveUserMusicType(email, musicType);
        return "redirect:/music";
    }

}
