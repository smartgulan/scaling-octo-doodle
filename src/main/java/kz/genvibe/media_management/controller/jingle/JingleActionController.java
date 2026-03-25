package kz.genvibe.media_management.controller.jingle;

import jakarta.validation.Valid;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.JingleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/jingles")
@RequiredArgsConstructor
public class JingleActionController {

    private final JingleService jingleService;

    @PostMapping
    public String createJingle(
        @CurrentUser AppUser appUser,
        @Valid JingleCreateDto dto
    ) {
        jingleService.createJingle(appUser, dto);
        return "redirect:/jingles";
    }

}
