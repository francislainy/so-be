package com.francislainy.sobe.controller.answer;

import com.francislainy.sobe.config.BasePostgresConfig;
import com.francislainy.sobe.entity.AnswerEntity;
import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.model.Answer;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.model.User;
import com.francislainy.sobe.repository.AnswerRepository;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.repository.UserRepository;
import com.francislainy.sobe.service.impl.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.francislainy.sobe.util.TestUtil.fromJson;
import static com.francislainy.sobe.util.TestUtil.toJson;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AnswerControllerIT extends BasePostgresConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;

    @MockBean
    CurrentUserService currentUserService;

    UserEntity userEntity;

    QuestionEntity questionEntity;

    @BeforeEach
    void setUp() {
        userEntity = userRepository.save(User.builder()
                .username("user")
                .password("password")
                .role("USER")
                .build().toEntity());

        when(currentUserService.getCurrentUser()).thenReturn(userEntity);

        Question question = Question.builder()
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .userId(userEntity.getId())
                .build();
        questionEntity = questionRepository.save(question.toEntity());
    }

    @Test
    @WithMockUser
    void shouldCreateAnswerToQuestion() throws Exception {
        Answer answer = Answer
                .builder()
                .content("This is an answer")
                .questionId(questionEntity.getId())
                .userId(userEntity.getId())
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/questions/{questionId}/answers", questionEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(answer)))
                .andExpect(status().isCreated())
                .andReturn();

        Answer createdAnswer = (Answer) fromJson(mvcResult.getResponse().getContentAsString(), Answer.class);
        assertNotNull(createdAnswer, "Answer should not be null");

        assertAll(
                () -> assertNotNull(createdAnswer.getQuestionId(), "Question ID should not be null"),
                () -> assertNotNull(createdAnswer.getUserId(), "User ID should not be null"),
                () -> assertNotNull(createdAnswer.getId(), "Answer ID should not be null"),
                () -> assertEquals(answer.getContent(), createdAnswer.getContent(), "Answer content should match")
        );
    }

    @Test
    void shouldNotCreateAnswerWhenUserIsNotAuthenticated() throws Exception {
        Answer answer = Answer
                .builder()
                .content("This is an answer")
                .questionId(questionEntity.getId())
                .build();

        mockMvc.perform(post("/api/v1/questions/{questionId}/answers", questionEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(answer)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldDeleteAnswer() throws Exception {
        AnswerEntity answerEntity = answerRepository.save(AnswerEntity
                .builder()
                .content("This is an answer")
                .questionEntity(questionEntity)
                .build());

        mockMvc.perform(delete("/api/v1/questions/{questionId}/answers/{answerId}", questionEntity.getId(), answerEntity.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteAnswerWhenUserIsNotAuthenticated() throws Exception {
        AnswerEntity answerEntity = answerRepository.save(AnswerEntity
                .builder()
                .content("This is an answer")
                .questionEntity(questionEntity)
                .userEntity(userEntity)
                .build());

        mockMvc.perform(delete("/api/v1/questions/{questionId}/answers/{answerId}", questionEntity.getId(), answerEntity.getId()))
                .andExpect(status().isUnauthorized());
    }
}
