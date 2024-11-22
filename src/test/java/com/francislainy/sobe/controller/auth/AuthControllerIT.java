package com.francislainy.sobe.controller.auth;

import com.francislainy.sobe.config.BasePostgresConfig;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.model.User;
import com.francislainy.sobe.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.francislainy.sobe.util.TestUtil.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerIT extends BasePostgresConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    void testRegisterUser() throws Exception {
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
    void testLoginUser() throws Exception {
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

    // todo: remove this test once new valid endpoints are added - 06/11/2024
    @Test
    @WithMockUser(roles = "USER")
    void testUserLogin() throws Exception {
        // Save user to db before logging in
        UserEntity savedUser = UserEntity.builder()
                .username("admin")
                .password("password")
                .role("USER")
                .build();
        userRepository.save(savedUser);

        User user = savedUser.toModel();

        mockMvc.perform(get("/api/v1/test-user")
                .content(toJson(user)))
                .andExpect(status().isOk());
    }

    // todo: remove this test once new valid endpoints are added - 06/11/2024
    @Test
    @WithMockUser(roles = "ADMIN")
    void testUserAdmin() throws Exception {
        // Save user to db before logging in
        UserEntity savedUser = UserEntity.builder()
                .username("admin")
                .password("password")
                .role("USER")
                .build();
        userRepository.save(savedUser);

        User user = savedUser.toModel();

        mockMvc.perform(post("/api/v1/test-admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isCreated());
    }
}
