package com.bus.repository;

import com.bus.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository
        extends JpaRepository<UserProfile, Long> {
}