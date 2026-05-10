package com.bus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)

    private Long id;

    private String fullName;

    private String phone;

    private String email;

    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}