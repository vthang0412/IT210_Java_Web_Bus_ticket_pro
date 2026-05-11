package com.bus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bus.config.CustomLoginSuccessHandler;
import com.bus.dto.LoginRequest;
import com.bus.dto.RegisterRequest;
import com.bus.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute(
                "loginRequest",
                new LoginRequest()
        );

        return "auth/login";
    }

    @PostMapping("/login")
        public String doLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult result,
            Model model,
            HttpServletRequest httpServletRequest
        ) {
            log.debug("Yêu cầu POST /login được thực thi với tên người dùng='{}'", request.getUsername());

        if (result.hasErrors()) {
            log.debug("Validation errors: {}", result.getAllErrors());
            return "auth/login";
        }

        boolean ok = authService.login(request.getUsername(), request.getPassword());

        if (!ok) {
            log.debug("Xác thực người dùng không thành công={}", request.getUsername());
            result.reject("login.invalid", "Sai tài khoản hoặc mật khẩu");
            return "auth/login";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        httpServletRequest.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        if (authentication != null) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "redirect:/admin/buses";
            }

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
                return "redirect:/staff/tickets";
            }
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute(
                "registerRequest",
                new RegisterRequest()
        );

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

            result.reject(
                    "register.invalid",
                    ex.getMessage()
            );

            return "auth/register";
        }

        return "redirect:/login";
    }
}