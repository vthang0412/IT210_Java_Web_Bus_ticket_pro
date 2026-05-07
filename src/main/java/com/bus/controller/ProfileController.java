package com.bus.controller;

import com.bus.dto.ProfileRequest;
import com.bus.entity.User;
import com.bus.repository.UserRepository;
import com.bus.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/passenger")
public class ProfileController {

    private final UserRepository userRepository;
    private final ProfileService profileService;

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        ProfileRequest request = new ProfileRequest();

        // luôn set username
        request.setUsername(user.getUsername());

        // nếu chưa có profile → tạo dữ liệu rỗng để tránh null
        if (user.getProfile() != null) {
            request.setFullName(user.getProfile().getFullName());
            request.setPhone(user.getProfile().getPhone());
            request.setEmail(user.getProfile().getEmail());
            request.setAddress(user.getProfile().getAddress());
        }

        model.addAttribute("profileRequest", request);

        return "passenger/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute ProfileRequest request,
                                Authentication authentication) {

        profileService.updateProfile(authentication.getName(), request);

        return "redirect:/passenger/profile";
    }
}