package com.bus.service.impl;

import com.bus.dto.ProfileUpdateRequest;
import com.bus.entity.User;
import com.bus.entity.UserProfile;
import com.bus.repository.UserProfileRepository;
import com.bus.repository.UserRepository;
import com.bus.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final UserProfileRepository profileRepository;

    @Override
    public UserProfile getProfile(String username) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tim thấy người dùng"));

        return profileRepository
                .findByUser(user)
                .orElseGet(() -> {
                    UserProfile profile = new UserProfile();
                    profile.setUser(user);
                    profile.setFullName(user.getUsername());
                    return profileRepository.save(profile);
                });
    }

    @Override
    public void updateProfile(

            String username,

            ProfileUpdateRequest request

    ) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow();

        UserProfile profile = profileRepository
                .findByUser(user)
                .orElseThrow();

        profile.setFullName(
                request.getFullName()
        );

        profile.setPhone(
                request.getPhone()
        );

        profile.setEmail(
                request.getEmail()
        );

        profile.setAddress(
                request.getAddress()
        );

        profileRepository.save(profile);
    }
}
