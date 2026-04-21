package kz.genvibe.media_management.controller.music;

import kz.genvibe.media_management.model.enums.SpacePurpose;
import kz.genvibe.media_management.service.internal.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicLibraryController {

    private final MusicService musicService;

    @GetMapping
    public String musicPage(Model model) {
        model.addAttribute("musicTypes", musicService.getAllMusicTypes());
        model.addAttribute("spacePurposes", SpacePurpose.values());
        return "pages/music";
    }

}
