package kz.genvibe.media_management.controller.jingle;

import jakarta.validation.Valid;
import kz.genvibe.media_management.config.annotations.CurrentUser;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleAddStoresDto;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleApproveDto;
import kz.genvibe.media_management.model.domain.dto.jingle.JingleCreateDto;
import kz.genvibe.media_management.model.entity.AppUser;
import kz.genvibe.media_management.service.internal.JingleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public String deleteJingle(
        @PathVariable Long id,
        @CurrentUser AppUser appUser
    ) {
        jingleService.deleteJingleById(id, appUser);
        return "redirect:/jingles";
    }

    @PatchMapping("/{id}/stores")
    public String addJingleToLocations(
        @PathVariable long id,
        @ModelAttribute JingleAddStoresDto dto,
        @CurrentUser AppUser appUser
    ) {
        jingleService.addJingleToStores(id, dto, appUser);
        return "redirect:/jingles";
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public String approveJingle(
        @PathVariable long id,
        @RequestBody JingleApproveDto dto,
        @CurrentUser AppUser appUser
    ) {
        jingleService.setPauseApprovalStatus(id, dto, appUser);
        return "redirect:/jingles";
    }

    @ResponseBody
    @PatchMapping("/pause-request/{id}")
    public void requestToPause(@PathVariable long id) {
        jingleService.requestToPauseJingle(id);
    }

}
