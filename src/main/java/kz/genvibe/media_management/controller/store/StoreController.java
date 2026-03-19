package kz.genvibe.media_management.controller.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    @GetMapping
    public String storesPage() {
        return "pages/stores";
    }

}
