package com.bus.service;

import com.bus.dto.ProfileRequest;

public interface ProfileService {

    void updateProfile(
            String username,
            ProfileRequest request
    );
}