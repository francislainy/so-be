package com.francislainy.sobe.controller.question;

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
import java.util.UUID;

import static com.francislainy.sobe.util.TestUtil.toJson;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

        verify(questionService, never()).createQuestion(any(Question.class));
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

        verify(questionService, never()).createQuestion(any(Question.class));
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

        verify(questionService, never()).createQuestion(any(Question.class));
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

        verify(questionService, never()).createQuestion(any(Question.class));
    }

    @Test
    void shouldUpdateQuestion() throws Exception {
        Question question = Question.builder()
                .title("How to create a question?")
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        UUID questionId = randomUUID();

        mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(question)))
                .andExpect(status().isOk());

        verify(questionService, times(1)).updateQuestion(any(UUID.class), any(Question.class));
    }

    @Test
    void shouldNotUpdateQuestionWhenTitleIsNull() throws Exception {
        Question question = Question.builder()
                .title(null)
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        UUID questionId = randomUUID();

        mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(question)))
                .andExpect(status().isBadRequest());

        verify(questionService, never()).updateQuestion(any(UUID.class), any(Question.class));
    }

    @Test
    void shouldNotUpdateQuestionWhenTitleIsEmpty() throws Exception {
        Question question = Question.builder()
                .title("")
                .content("I am trying to create a question but I am not sure how to do it.")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        UUID questionId = randomUUID();

        mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(question)))
                .andExpect(status().isBadRequest());

        verify(questionService, never()).updateQuestion(any(UUID.class), any(Question.class));
    }

    @Test
    void shouldNotUpdateQuestionWhenContentIsNull() throws Exception {
        Question question = Question.builder()
                .title("How to create a question?")
                .content(null)
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        UUID questionId = randomUUID();

        mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(question)))
                .andExpect(status().isBadRequest());

        verify(questionService, never()).updateQuestion(any(UUID.class), any(Question.class));
    }

    @Test
    void shouldNotUpdateQuestionWhenContentIsEmpty() throws Exception {
        Question question = Question.builder()
                .title("How to create a question?")
                .content("")
                .createdAt(LocalDateTime.now())
                .userId(randomUUID())
                .build();

        UUID questionId = randomUUID();

        mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(question)))
                .andExpect(status().isBadRequest());

        verify(questionService, never()).updateQuestion(any(UUID.class), any(Question.class));
    }

    @Test
    void shouldDeleteQuestion() throws Exception {
        UUID questionId = randomUUID();

        mockMvc.perform(delete("/api/v1/questions/{questionId}", questionId))
                .andExpect(status().isNoContent());

        verify(questionService, times(1)).deleteQuestion(any(UUID.class));
    }
}
