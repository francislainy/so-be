package com.francislainy.sobe.service.question;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.impl.question.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
                .createdAt(LocalDateTime.now())
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
                () -> assertEquals(question.getCreatedAt(), createdQuestion.getCreatedAt(), "Question created at should match"),
                () -> assertEquals(userId, createdQuestion.getUserId(), "Question user id should match")
        );

        verify(questionRepository).save(any(QuestionEntity.class));
    }

    @Test
    void shouldUpdateQuestion() {
        Question question = Question.builder()
                .title("question")
                .content("content")
                .userId(randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        UUID questionId = randomUUID();
        QuestionEntity questionEntity = question.withId(questionId).toEntity();

        when(questionRepository.existsById(any(UUID.class))).thenReturn(true);
        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(questionEntity);

        Question updatedQuestion = questionService.updateQuestion(questionId, question);

        assertNotNull(updatedQuestion, "Question should not be null");

        assertAll(
                () -> assertEquals(questionId, updatedQuestion.getId(), "Question id should match"),
                () -> assertEquals(question.getTitle(), updatedQuestion.getTitle(), "Question title should match"),
                () -> assertEquals(question.getContent(), updatedQuestion.getContent(), "Question content should match"),
                () -> assertEquals(question.getCreatedAt(), updatedQuestion.getCreatedAt(), "Question created at should match"),
                () -> assertEquals(question.getUserId(), updatedQuestion.getUserId(), "Question user id should match")
        );

        verify(questionRepository, times(1)).existsById(any(UUID.class));
        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
    }

    @Test
    void shouldNotUpdateQuestionWhenQuestionNotFound() {
        Question question = Question.builder()
                .title("question")
                .content("content")
                .userId(randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        UUID questionId = randomUUID();

        when(questionRepository.existsById(questionId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> questionService.updateQuestion(questionId, question));

        verify(questionRepository, times(1)).existsById(questionId);
        verify(questionRepository, never()).save(any(QuestionEntity.class));
    }
}
