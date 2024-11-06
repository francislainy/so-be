package com.francislainy.sobe.service;

import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.model.User;
import com.francislainy.sobe.repository.UserRepository;
import com.francislainy.sobe.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    AuthServiceImpl authService;

    @Mock
    UserRepository userRepository;

    @Test
    void shouldRegisterUser() {
        User user = User.builder()
                .username("username")
                .password("password")
                .build();

        UserEntity userEntity = user.withId(randomUUID()).toEntity();

       when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

       User registeredUser = authService.registerUser(user);
       assertNotNull(registeredUser, "User should not be null");

       assertAll(
                () -> assertNotNull(registeredUser.getId(), "User id should not be null"),
                () -> assertNotNull(registeredUser.getUsername(), "User username should not be null"),
                () -> assertNotNull(registeredUser.getPassword(), "User password should not be null")
       );

       verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void shouldLoginUser() {
        User userRequest = User.builder()
                .username("email.com")
                .password("password")
                .build();

        User userResponse = userRequest.withId(randomUUID());

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userResponse.toEntity()));

        User loggedInUser = authService.loginUser(userRequest);
        assertNotNull(loggedInUser, "User should not be null");

        assertAll(
                () -> assertNotNull(userResponse.getId(), "User id should not be null"),
                () -> assertNotNull(userResponse.getUsername(), "User username should not be null"),
                () -> assertNotNull(userResponse.getPassword(), "User password should not be null")
        );

        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnLogin() {
        User userRequest = User.builder()
                .username("username")
                .password("password")
                .build();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.loginUser(userRequest));
    }
}
