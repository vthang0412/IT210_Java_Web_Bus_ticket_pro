package com.bus.controller;

import com.bus.dto.RegisterRequest;
import com.bus.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute(
                "registerRequest",
                new RegisterRequest()
        );

        return "register";
    }

    @PostMapping("/register")
    public String register(

            @Valid
            @ModelAttribute RegisterRequest request,

            BindingResult result
    ) {

        if (result.hasErrors()) {

            return "register";
        }

        authService.register(request);

        return "redirect:/login";
    }

    @GetMapping("/redirect")
    public String redirect(
            Authentication authentication
    ) {

        boolean isAdmin =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(a ->

                                a.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        boolean isStaff =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(a ->

                                a.getAuthority()
                                        .equals("ROLE_STAFF")
                        );

        if (isAdmin) {

            return "redirect:/admin/buses";
        }

        if (isStaff) {

            return "redirect:/staff/tickets";
        }

        return "redirect:/";
    }
}