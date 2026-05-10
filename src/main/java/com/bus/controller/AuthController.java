package com.bus.controller;

import com.bus.dto.LoginRequest;
import com.bus.dto.RegisterRequest;
import com.bus.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("loginRequest", new LoginRequest());

        if (error != null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
        }

        return "auth/login";
    }
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "auth/login";
        }

        boolean success = authService.login(request.getUsername(), request.getPassword());

        if (!success) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "auth/login";
        }

        return "redirect:/";
    }
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.register(request);
        } catch (IllegalArgumentException ex) {
            result.reject("register.invalid", ex.getMessage());
            return "auth/register";
        }

        return "redirect:/login";
    }

}
