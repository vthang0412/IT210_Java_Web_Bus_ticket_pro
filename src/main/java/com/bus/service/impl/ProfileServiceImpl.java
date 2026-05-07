package com.bus.service.impl;

import com.bus.dto.ProfileRequest;
import com.bus.entity.User;
import com.bus.entity.UserProfile;
import com.bus.repository.ProfileRepository;
import com.bus.repository.UserRepository;
import com.bus.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    public void updateProfile(String username, ProfileRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        UserProfile profile = user.getProfile();

        // nếu chưa có profile → tạo mới
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setEmail(request.getEmail());
        profile.setAddress(request.getAddress());

        // CHỈ CẦN SAVE 1 BÊN (QUAN TRỌNG)
        profileRepository.save(profile);
    }
}