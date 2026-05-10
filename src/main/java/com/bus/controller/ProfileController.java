package com.bus.controller;

import com.bus.dto.ProfileUpdateRequest;
import com.bus.entity.UserProfile;
import com.bus.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String profile(

            Authentication authentication,

            Model model

    ) {

        UserProfile profile = profileService.getProfile(authentication.getName());

        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFullName(profile.getFullName());
        request.setPhone(profile.getPhone());
        request.setEmail(profile.getEmail());
        request.setAddress(profile.getAddress());

        model.addAttribute("username", profile.getUser().getUsername());
        model.addAttribute("profileUpdateRequest", request);

        return "profile/profile";
    }

    @PostMapping("/update")
    public String update(

            @Valid @ModelAttribute("profileUpdateRequest")
            ProfileUpdateRequest request,

            BindingResult result,

            Authentication authentication,

            Model model

    ) {
        if (result.hasErrors()) {
            model.addAttribute("username", authentication.getName());
            return "profile/profile";
        }

        profileService.updateProfile(

                authentication.getName(),

                request
        );

        return "redirect:/";
    }
}
