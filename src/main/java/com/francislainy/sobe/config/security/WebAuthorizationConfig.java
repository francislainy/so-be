package com.francislainy.sobe.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebAuthorizationConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        http.authorizeHttpRequests(
                c -> c
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/*").permitAll()
                        .anyRequest().authenticated()
        );

        http.csrf(
                c -> c.disable()
        );

        return http.build();
    }
}