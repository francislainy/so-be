package com.francislainy.sobe.controller.answer;

import com.francislainy.sobe.model.Answer;
import com.francislainy.sobe.service.AnswerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.francislainy.sobe.util.TestUtil.toJson;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnswerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AnswerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnswerService answerService;

    @Test
    void shouldCreateAnswer() throws Exception {
        Answer answer = Answer.builder()
                .content("This is an answer")
                .build();

        UUID questionId = randomUUID();

        mockMvc.perform(post("/api/v1/questions/{questionId}/answers", questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(answer)))
                .andExpect(status().isCreated());

        verify(answerService, times(1)).createAnswer(any(), any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldNotCreateAnswerWhenContentIsNullOrEmpty(String content) throws Exception {
        Answer answer = Answer.builder()
                .content(content)
                .build();

        UUID questionId = randomUUID();

        mockMvc.perform(post("/api/v1/questions/{questionId}/answers", questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(answer)))
                .andExpect(status().isBadRequest());

        verify(answerService, never()).createAnswer(any(), any());
    }

    @Test
    void shouldDeleteAnswer() throws Exception {
        UUID questionId = randomUUID();
        UUID answerId = randomUUID();

        mockMvc.perform(delete("/api/v1/questions/{questionId}/answers/{answerId}", questionId, answerId))
                .andExpect(status().isNoContent());

        verify(answerService, times(1)).deleteAnswer(any(), any());
    }
}
