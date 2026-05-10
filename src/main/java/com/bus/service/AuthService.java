package com.bus.service;

import com.bus.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    boolean login(String username, String password);
}