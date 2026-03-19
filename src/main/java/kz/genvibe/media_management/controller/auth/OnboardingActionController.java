package kz.genvibe.media_management.controller.auth;

import jakarta.validation.Valid;
import kz.genvibe.media_management.model.domain.OnboardingSession;
import kz.genvibe.media_management.model.domain.dto.onboarding.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class OnboardingActionController {

    private final OnboardingSession session;

    @PostMapping("/step-1")
    public String onboardingStep1(@Valid @ModelAttribute Step1Dto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/auth/onboarding/welcome";
        }

        session.setCompanyName(dto.companyName());
        session.setBusinessType(dto.businessType());
        session.setMusicProvider(dto.musicProvider());

        return "redirect:/onboarding/brand-identity";
    }

    @PostMapping("/step-2")
    public String onboardingStep2(@Valid @ModelAttribute Step2Dto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/auth/onboarding/brand-identity";
        }
        session.setBrandIdentities(dto.brandIdentities());
        return "redirect:/onboarding/current-feel";
    }

    @PostMapping("/step-3")
    public String onboardingStep3(@Valid @ModelAttribute Step3Dto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/auth/onboarding/current-feel";
        }
        session.setCurrentFeels(dto.currentFeels());
        return "redirect:/onboarding/customer-feel";
    }

    @PostMapping("/step-4")
    public String onboardingStep4(@Valid @ModelAttribute Step4Dto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/auth/onboarding/customer-feel";
        }
        session.setSpacePurpose(dto.spacePurpose());
        return "redirect:/onboarding/playtime-window";
    }

    @PostMapping("/step-5")
    public String onboardingStep5(@Valid @ModelAttribute Step5Dto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/auth/onboarding/playtime-window";
        }
        session.setPlaytimeWindow(dto.playtimeWindow());
        return "redirect:/onboarding/verify-email";
    }

}
