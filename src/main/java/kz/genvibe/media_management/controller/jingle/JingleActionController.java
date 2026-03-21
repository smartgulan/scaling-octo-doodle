package kz.genvibe.media_management.controller.jingle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/jingles")
@RequiredArgsConstructor
public class JingleActionController {

    @PostMapping
    public String createJingle() {
        return "redirect:/jingles";
    }

}
