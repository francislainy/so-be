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

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
        UserEntity userEntity = UserEntity.builder()
                .id(randomUUID())
                .username("email.com")
                .password("password")
                .build();

        User user = User.builder()
                .username("email.com")
                .password("password")
                .build();

       when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

       User registeredUser = authService.registerUser(user);

       assertNotNull(registeredUser, "User should not be null");

       assertAll(
                () -> assertNotNull(registeredUser.getId(), "User id should not be null"),
                () -> assertNotNull(registeredUser.getUsername(), "User username should not be null"),
                () -> assertNotNull(registeredUser.getPassword(), "User password should not be null")
       );

       verify(userRepository).save(any(UserEntity.class));
    }
}
