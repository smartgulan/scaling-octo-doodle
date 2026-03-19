package kz.genvibe.media_management.controller.music;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicLibraryController {

    @GetMapping
    public String musicPage() {
        return "pages/music";
    }

}
