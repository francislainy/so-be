package com.francislainy.sobe.controller;

import com.francislainy.sobe.config.BasePostgresConfig;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.model.User;
import com.francislainy.sobe.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.francislainy.sobe.util.TestUtil.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerIT extends BasePostgresConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testRegisterUser() throws Exception {
        User user = User.builder()
                .username("username")
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testLoginUser() throws Exception {
        // Save user to db before logging in
        UserEntity savedUser = UserEntity.builder()
                .username("username")
                .password("password")
                .build();
        userRepository.save(savedUser);

        User user = savedUser.toModel();

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isOk());
    }

}