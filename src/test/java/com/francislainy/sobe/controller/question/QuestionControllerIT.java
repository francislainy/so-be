package com.francislainy.sobe.controller.question;

import com.francislainy.sobe.config.BasePostgresConfig;
import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.model.User;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static com.francislainy.sobe.exception.AppExceptionHandler.ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION;
import static com.francislainy.sobe.util.TestUtil.fromJson;
import static com.francislainy.sobe.util.TestUtil.toJson;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuestionControllerIT extends BasePostgresConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

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

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(question)))
                .andExpect(status().isCreated())
                .andReturn();

        Question createdQuestion = (Question) fromJson(mvcResult.getResponse().getContentAsString(), Question.class);
        assertNotNull(createdQuestion, "Question should not be null");
        assertAll(
                () -> assertNotNull(createdQuestion.getId(), "Question id should not be null"),
                () -> assertEquals(question.getTitle(), createdQuestion.getTitle(), "Question title should match"),
                () -> assertEquals(question.getContent(), createdQuestion.getContent(), "Question content should match"),
                () -> assertEquals(question.getUserId(), createdQuestion.getUserId(), "Question user id should match"),
                () -> assertNotNull(createdQuestion.getCreatedAt(), "Question created at should not be null"));
    }

    @Test
    void shouldNotCreateQuestionWhenNotAuthenticated() throws Exception {
        Question question = Question.builder()
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .userId(null)
                .build();

        mockMvc.perform(post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(question)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testUpdateQuestion() throws Exception {
        UserEntity userEntity = userRepository.save(User.builder()
                .username("user")
                .password("password")
                .role("USER")
                .build().toEntity());

        QuestionEntity questionEntity = QuestionEntity.builder()
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userEntity(userEntity)
                .build();
        questionEntity = questionRepository.saveAndFlush(questionEntity);

        Question updatedQuestionRequest = Question.builder()
                .title("How to update a question?")
                .content("I am trying to update a question but I am not sure how to do it.")
                .build();

        MvcResult updatedMvcResult = mockMvc.perform(put("/api/v1/questions/{questionId}", questionEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(updatedQuestionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Question updatedQuestionResponse = (Question) fromJson(updatedMvcResult.getResponse().getContentAsString(), Question.class);
        assertNotNull(updatedQuestionResponse, "Updated question should not be null");
        QuestionEntity finalQuestionEntity = questionEntity;
        assertAll(
                () -> assertEquals(finalQuestionEntity.getId(), updatedQuestionResponse.getId(), "id should match"),
                () -> assertEquals(updatedQuestionRequest.getTitle(), updatedQuestionResponse.getTitle(), "title should match"),
                () -> assertEquals(updatedQuestionRequest.getContent(), updatedQuestionResponse.getContent(), "content should match"),
                () -> assertEquals(userEntity.getId(), updatedQuestionResponse.getUserId(), "user id should match"),
                () -> assertEquals(finalQuestionEntity.getCreatedAt().toLocalDate(), updatedQuestionResponse.getCreatedAt().toLocalDate(), "created at should match"));
    }

    @Test
    void shouldNotUpdateQuestionWhenNotAuthenticated() throws Exception {
        UserEntity userEntity = userRepository.save(User.builder()
                .username("user")
                .password("password")
                .role("USER")
                .build().toEntity());

        QuestionEntity questionEntity = questionRepository.save(QuestionEntity.builder()
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .userEntity(userEntity)
                .build());

        Question updatedQuestionRequest = Question.builder()
                .title("How to update a question?")
                .content("I am trying to update a question but I am not sure how to do it.")
                .userId(userEntity.getId())
                .build();

        mockMvc.perform(put("/api/v1/questions/{questionId}", questionEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(updatedQuestionRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "anotherUser")
    void shouldNotUpdateQuestionWhenNotTheQuestionCreator() throws Exception {
        UserEntity userEntity = userRepository.save(User.builder()
                .username("user")
                .password("password")
                .role("USER")
                .build().toEntity());

        UserEntity anotherUserEntity = userRepository.save(User.builder()
                .username("anotherUser")
                .password("password")
                .role("USER")
                .build().toEntity());

        QuestionEntity questionEntity = questionRepository.save(QuestionEntity.builder()
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userEntity(userEntity)
                .build());

        Question updatedQuestionRequest = Question.builder()
                .title("How to update a question?")
                .content("I am trying to update a question but I am not sure how to do it.")
                .userId(anotherUserEntity.getId())
                .build();

        mockMvc.perform(put("/api/v1/questions/{questionId}", questionEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(updatedQuestionRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION));
    }
}
