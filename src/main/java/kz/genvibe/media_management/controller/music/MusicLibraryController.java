package kz.genvibe.media_management.controller.music;

import kz.genvibe.media_management.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicLibraryController {

    private final MusicService musicService;

    @GetMapping
    public String musicPage(
        Model model,
        @RequestParam(required = false) boolean musicTypeSaved
    ) {
        model.addAttribute("musicTypes", musicService.getAllMusicTypes());
        return "pages/music";
    }

}
