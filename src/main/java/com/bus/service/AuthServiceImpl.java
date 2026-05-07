package com.bus.service;

import com.bus.dto.RegisterRequest;
import com.bus.entity.Role;
import com.bus.entity.User;
import com.bus.entity.UserProfile;
import com.bus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PASSENGER)
                .build();

        UserProfile profile = UserProfile.builder()
                .fullName(request.getFullName())
                .user(user)
                .build();

        user.setProfile(profile);

        userRepository.save(user);
    }
}