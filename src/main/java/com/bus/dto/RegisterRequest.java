package com.bus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 20, message = "Username phải từ 3 đến 20 ký tự")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username không được chứa khoảng trắng hoặc ký tự đặc biệt"
    )
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, max = 32, message = "Password phải từ 6 đến 32 ký tự")
    @Pattern(
            regexp = "^\\S+$",
            message = "Password không được chứa khoảng trắng"
    )
    private String password;

    @NotBlank(message = "Confirm password không được để trống")
    private String confirmPassword;
}