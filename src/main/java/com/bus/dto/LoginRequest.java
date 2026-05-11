package com.bus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username không được để trống")
    @Pattern(
            regexp = "^\\S+$",
            message = "Username không được chứa khoảng trắng"
    )
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Pattern(
            regexp = "^\\S+$",
            message = "Password không được chứa khoảng trắng"
    )
    private String password;
}