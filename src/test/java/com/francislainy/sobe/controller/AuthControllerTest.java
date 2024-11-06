package com.francislainy.sobe.controller;

import com.francislainy.sobe.model.User;
import com.francislainy.sobe.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.francislainy.sobe.util.TestUtil.toJson;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthServiceImpl authService;

    @Test
    void shouldRegisterUser() throws Exception {
        User user = User.builder()
                .username("email.com")
                .password("password")
                .build();

        when(authService.registerUser(user)).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user)))
                .andExpect(status().isCreated());

        verify(authService, times(1)).registerUser(user);
    }

    @Test
    void shouldLoginUser() throws Exception {
        User userRequest = User.builder()
                .username("email.com")
                .password("password")
                .build();


        User userResponse = userRequest.withId(randomUUID());

        when(authService.loginUser(userRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(toJson(userResponse)));

        verify(authService, times(1)).loginUser(userRequest);
    }
}
