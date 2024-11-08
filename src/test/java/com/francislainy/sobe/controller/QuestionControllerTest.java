package com.francislainy.sobe.controller;

import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.francislainy.sobe.util.TestUtil.toJson;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QuestionService questionService;

    @Test
    void shouldCreateQuestion() throws Exception {
        Question question = Question.builder()
                .id(randomUUID())
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(question)))
                .andExpect(status().isCreated());

        verify(questionService, times(1)).createQuestion(any(Question.class));
    }

    @Test
    void shouldNotCreateQuestionWhenTitleIsNull() throws Exception {
        Question question = Question.builder()
                .id(randomUUID())
                .title(null)
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(question)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateQuestionWhenTitleIsEmpty() throws Exception {
        Question question = Question.builder()
                .id(randomUUID())
                .title("")
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(question)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateQuestionWhenContentIsNull() throws Exception {
        Question question = Question.builder()
                .id(randomUUID())
                .title("How to create a question?")
                .content(null)
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(question)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateQuestionWhenContentIsEmpty() throws Exception {
        Question question = Question.builder()
                .id(randomUUID())
                .title("How to create a question?")
                .content("")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(question)))
                .andExpect(status().isBadRequest());
    }
}
