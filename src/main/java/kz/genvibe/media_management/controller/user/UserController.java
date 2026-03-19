package kz.genvibe.media_management.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.genvibe.media_management.model.domain.dto.user.PasswordSetupDto;
import kz.genvibe.media_management.service.internal.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public String createUser(
        @ModelAttribute PasswordSetupDto passwordSetupDto,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        userService.createUser(passwordSetupDto, request, response);
        return "redirect:/dashboard";
    }

}
