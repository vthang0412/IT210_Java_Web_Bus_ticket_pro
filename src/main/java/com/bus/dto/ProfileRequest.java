package com.bus.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String address;
}