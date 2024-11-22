package com.francislainy.sobe.service.question;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.exception.EntityDoesNotBelongToUserException;
import com.francislainy.sobe.exception.QuestionNotFoundException;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.impl.CurrentUserService;
import com.francislainy.sobe.service.impl.question.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.francislainy.sobe.exception.AppExceptionHandler.ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION;
import static com.francislainy.sobe.exception.AppExceptionHandler.QUESTION_NOT_FOUND_EXCEPTION;
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

    @Mock
    CurrentUserService currentUserService;

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
                () -> assertEquals(userId, createdQuestion.getUserId(), "Question user id should match"));

        verify(questionRepository).save(any(QuestionEntity.class));
    }

    @Test
    void shouldUpdateQuestion() {
        UUID ownerId = randomUUID();

        UUID questionId = randomUUID();
        QuestionEntity originalQuestionEntity = QuestionEntity.builder()
                .id(questionId)
                .title("Original question")
                .content("Original content")
                .userEntity(UserEntity.builder().id(ownerId).build())
                .createdAt(LocalDateTime.now())
                .build();

        Question question = Question.builder()
                .title("Updated question")
                .content("Updated content")
                .userId(ownerId)
                .createdAt(LocalDateTime.now())
                .build();
        QuestionEntity updatedQuestionEntity = question.withId(questionId).toEntity();

        UserEntity currentUser = UserEntity.builder()
                .id(ownerId)
                .username("testuser")
                .password("password")
                .build();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(originalQuestionEntity));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(updatedQuestionEntity);

        Question updatedQuestion = questionService.updateQuestion(questionId, question);

        assertNotNull(updatedQuestion, "Question should not be null");

        assertAll(
                () -> assertEquals(questionId, updatedQuestion.getId(), "Question id should match"),
                () -> assertEquals(question.getTitle(), updatedQuestion.getTitle(), "Question title should match"),
                () -> assertEquals(question.getContent(), updatedQuestion.getContent(), "Question content should match"),
                () -> assertEquals(ownerId, updatedQuestion.getUserId(), "Question user id should match"));

        verify(questionRepository, times(1)).findById(questionId);
        verify(currentUserService, times(1)).getCurrentUser();
        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
    }

    @Test
    void shouldNotUpdateQuestionWhenNotOwner() {
        UUID ownerId = randomUUID();
        UUID questionId = randomUUID();

        Question question = Question.builder()
                .title("Updated question")
                .content("Updated content")
                .userId(ownerId)
                .createdAt(LocalDateTime.now())
                .build();

        UserEntity currentUser = UserEntity.builder()
                .id(randomUUID())
                .username("testuser")
                .password("password")
                .build();

        QuestionEntity originalQuestionEntity = QuestionEntity.builder()
                .id(questionId)
                .title("Original question")
                .content("Original content")
                .userEntity(UserEntity.builder().id(ownerId).build())
                .createdAt(LocalDateTime.now())
                .build();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(originalQuestionEntity));
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);

        Exception e = assertThrows(EntityDoesNotBelongToUserException.class, () -> questionService.updateQuestion(questionId, question));

        assertEquals(ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(questionRepository, times(1)).findById(questionId);
        verify(currentUserService, times(1)).getCurrentUser();
        verify(questionRepository, never()).save(any(QuestionEntity.class));
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

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        Exception e = assertThrows(QuestionNotFoundException.class, () -> questionService.updateQuestion(questionId, question));

        assertEquals(QUESTION_NOT_FOUND_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, never()).save(any(QuestionEntity.class));
    }

    @Test
    void shouldDeleteQuestion() {
        UserEntity currentUser = UserEntity.builder()
                .id(randomUUID())
                .username("testuser")
                .password("password")
                .build();
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);

        UUID questionId = randomUUID();

        QuestionEntity questionEntity = QuestionEntity.builder().id(questionId).userEntity(currentUser).build();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionEntity));

        questionService.deleteQuestion(questionId);
        verify(questionRepository, times(1)).deleteById(questionId);
    }

    @Test
    void shouldNotDeleteQuestionWhenQuestionNotFound() {
        UUID questionId = randomUUID();
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        Exception e = assertThrows(QuestionNotFoundException.class, () -> questionService.deleteQuestion(questionId));

        assertEquals(QUESTION_NOT_FOUND_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(questionRepository, never()).deleteById(questionId);
    }

    @Test
    void shouldNotDeleteQuestionWhenUserIsNotOwner() {
        UserEntity currentUser = UserEntity.builder()
                .id(randomUUID())
                .username("testuser")
                .password("password")
                .build();
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);

        UserEntity anotherUser = UserEntity.builder()
                .id(randomUUID())
                .username("anotheruser")
                .password("password")
                .build();

        UUID questionId = randomUUID();

        QuestionEntity questionEntity = QuestionEntity.builder().id(questionId).userEntity(anotherUser).build();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionEntity));

        Exception e = assertThrows(EntityDoesNotBelongToUserException.class, () -> questionService.deleteQuestion(questionId));

        assertEquals(ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION, e.getMessage());

        verify(questionRepository, never()).deleteById(questionId);
    }
}
