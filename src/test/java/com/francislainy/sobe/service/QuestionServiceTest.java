package com.francislainy.sobe.service;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    QuestionServiceImpl questionService;

    @Mock
    QuestionRepository questionRepository;

    @Test
    void shouldCreateQuestion() {
        UUID userId = randomUUID();

        Question question = Question.builder()
                .title("question")
                .content("content")
                .userId(userId)
                .build();

        UUID questionId = randomUUID();
        QuestionEntity questionEntity = question.withId(questionId).toEntity();

        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(questionEntity);

        Question createdQuestion = questionService.createQuestion(question);

        assertNotNull(createdQuestion, "Question should not be null");

        assertAll(
                () -> assertEquals(questionId, createdQuestion.getId(), "Question id should match"),
                () -> assertEquals(question.getTitle(), createdQuestion.getTitle(), "Question title should match"),
                () -> assertEquals(question.getContent(), createdQuestion.getContent(), "Question content should match"),

                () -> assertEquals(userId, createdQuestion.getUserId(), "Question user id should match")
        );

        verify(questionRepository).save(any(QuestionEntity.class));
    }
}
