package com.bus.config;

import com.bus.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .userDetailsService(customUserDetailService)

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(

                                "/",
                                "/search",

                                "/login",
                                "/register",

                                "/ticket/**",

                                "/css/**",
                                "/js/**",
                                "/images/**"

                        ).permitAll()

                        // ADMIN
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // STAFF
                        .requestMatchers("/staff/**")
                        .hasAnyRole(
                                "STAFF",
                                "ADMIN"
                        )

                        // PASSENGER
                        .requestMatchers("/passenger/**")
                        .hasAnyRole(
                                "PASSENGER",
                                "STAFF",
                                "ADMIN"
                        )

                        // BOOKING
                        .requestMatchers(
                                "/booking/**",
                                "/trips/**"
                        )

                        .hasAnyRole(
                                "PASSENGER",
                                "STAFF",
                                "ADMIN"
                        )

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .defaultSuccessUrl(
                                "/redirect",
                                true
                        )

                        .failureUrl("/login?error")

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl(
                                "/login?logout"
                        )

                        .permitAll()
                );

        return http.build();
    }
}