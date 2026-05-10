package com.bus.repository;

import com.bus.entity.User;
import com.bus.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository
        extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByUser(User user);
}