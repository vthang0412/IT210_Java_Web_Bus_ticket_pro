package com.bus.service;

import com.bus.dto.ProfileUpdateRequest;
import com.bus.entity.UserProfile;

public interface ProfileService {

    UserProfile getProfile(String username);

    void updateProfile(

            String username,

            ProfileUpdateRequest request
    );
}