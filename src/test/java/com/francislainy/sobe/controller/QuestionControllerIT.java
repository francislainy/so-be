package com.francislainy.sobe.controller;

import com.francislainy.sobe.config.BasePostgresConfig;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.model.Question;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuestionControllerIT extends BasePostgresConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    @WithMockUser
    void testCreateQuestion() throws Exception {
        UserEntity userEntity = userRepository.save(User.builder()
                .username("user")
                .password("password")
                .role("USER")
                .build().toEntity());

        Question question = Question.builder()
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .userId(userEntity.getId())
                .build();

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(question)))
                .andExpect(status().isCreated());
    }
}