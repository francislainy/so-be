package com.francislainy.sobe.service.answer;

import com.francislainy.sobe.entity.AnswerEntity;
import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.exception.EntityDoesNotBelongToUserException;
import com.francislainy.sobe.exception.EntityNotFoundException;
import com.francislainy.sobe.exception.QuestionNotFoundException;
import com.francislainy.sobe.model.Answer;
import com.francislainy.sobe.repository.AnswerRepository;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.impl.CurrentUserService;
import com.francislainy.sobe.service.impl.answer.AnswerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.francislainy.sobe.exception.AppExceptionHandler.ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION;
import static com.francislainy.sobe.exception.AppExceptionHandler.ENTITY_NOT_FOUND_EXCEPTION;
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
public class AnswerServiceTest {

    @InjectMocks
    AnswerServiceImpl answerService;

    @Mock
    AnswerRepository answerRepository;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    CurrentUserService currentUserService;

    @Test
    void shouldCreateAnswer() {
        UUID ownerId = randomUUID();

        UUID questionId = randomUUID();
        UUID answerId = randomUUID();

        UserEntity currentUser = UserEntity.builder()
                .id(ownerId)
                .username("testuser")
                .password("password")
                .build();

        QuestionEntity questionEntity = QuestionEntity.builder()
                .id(questionId)
                .build();

        Answer answer = Answer.builder()
                .questionId(questionId)
                .content("This is an answer")
                .build();

        AnswerEntity answerEntity = answer.withId(answerId).withQuestionId(questionId).withUserId(currentUser.getId()).toEntity();

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(questionRepository.findById(any())).thenReturn(Optional.of(questionEntity));
        when(answerRepository.save(any())).thenReturn(answerEntity);

        Answer createdAnswer = answerService.createAnswer(questionId, answer);

        assertNotNull(createdAnswer, "Answer should not be null");

        assertAll(
                () -> assertNotNull(createdAnswer.getQuestionId(), "Question ID should not be null"),
                () -> assertNotNull(createdAnswer.getId(), "Answer ID should not be null"),
                () -> assertEquals(answer.getContent(), createdAnswer.getContent(), "Answer content should match"),
                () -> assertEquals(ownerId, createdAnswer.getUserId(), "User id should match"));

        verify(answerRepository, times(1)).save(any());
    }

    @Test
    void shouldNotCreateAnswerWhenQuestionIsNotFound() {
        UUID questionId = randomUUID();

        Answer answer = Answer.builder()
                .questionId(questionId)
                .content("This is an answer")
                .build();

        when(questionRepository.findById(any())).thenReturn(Optional.empty());

        Exception e = assertThrows(QuestionNotFoundException.class, () -> answerService.createAnswer(questionId, answer));
        assertEquals(QUESTION_NOT_FOUND_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(answerRepository, never()).save(any());
    }

    @Test
    void shouldGetListOfAnswers() {
        UUID questionId = randomUUID();

        AnswerEntity answerEntity1 = Answer.builder()
                .id(randomUUID())
                .content("This is an answer")
                .build()
                .toEntity();

        AnswerEntity answerEntity2 = Answer.builder()
                .id(randomUUID())
                .content("This is an answer")
                .build()
                .toEntity();

        when(questionRepository.existsById(any(UUID.class))).thenReturn(true);
        when(answerRepository.findAllByQuestionEntity_Id(any(UUID.class))).thenReturn(List.of(answerEntity1, answerEntity2));

        List<Answer> answers = answerService.getAnswers(questionId);

        assertAll(
                () -> assertNotNull(answers, "Answers should not be null"),
                () -> assertEquals(2, answers.size(), "Answers list should be empty"),

                () -> assertEquals(answerEntity1.getId(), answers.get(0).getId(), "Answer ID should match"),
                () -> assertEquals(answerEntity1.getContent(), answers.get(0).getContent(), "Answer content should match"),
                () -> assertEquals(answerEntity1.getUserEntity().getId(), answers.get(0).getUserId(), "User ID should match"),

                () -> assertEquals(answerEntity2.getId(), answers.get(1).getId(), "Answer ID should match"),
                () -> assertEquals(answerEntity2.getContent(), answers.get(1).getContent(), "Answer content should match"),
                () -> assertEquals(answerEntity2.getUserEntity().getId(), answers.get(1).getUserId(), "User ID should match")
                );

        verify(answerRepository, times(1)).findAllByQuestionEntity_Id(questionId);
    }

    @Test
    void shouldNotGetListOfAnswersWhenQuestionIsNotFound() {
        UUID questionId = randomUUID();

        when(questionRepository.existsById(any(UUID.class))).thenReturn(false);

        Exception e = assertThrows(QuestionNotFoundException.class, () -> answerService.getAnswers(questionId));
        assertEquals(QUESTION_NOT_FOUND_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(answerRepository, never()).findAllByQuestionEntity_Id(questionId);
    }

    @Test
    void shouldDeleteAnswer() {
        UserEntity currentUser = UserEntity.builder()
                .id(randomUUID())
                .username("testuser")
                .password("password")
                .build();
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);

        UUID questionId = randomUUID();
        UUID answerId = randomUUID();

        when(questionRepository.existsById(any(UUID.class))).thenReturn(true);
        AnswerEntity answerEntity = AnswerEntity.builder().id(answerId).userEntity(currentUser).build();
        when(answerRepository.findById(any(UUID.class))).thenReturn(Optional.of(answerEntity));

        answerService.deleteAnswer(questionId, answerId);
        verify(answerRepository, times(1)).deleteById(answerId);
    }

    @Test
    void shouldThrowExceptionWhenQuestionIsNotFoundOnDeletion() {
        UUID questionId = randomUUID();
        UUID answerId = randomUUID();

        when(questionRepository.existsById(any(UUID.class))).thenReturn(false);

        Exception e = assertThrows(QuestionNotFoundException.class, () -> answerService.deleteAnswer(questionId, answerId));
        assertEquals(QUESTION_NOT_FOUND_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(answerRepository, never()).deleteById(answerId);
    }

    @Test
    void shouldThrowExceptionWhenAnswerIsNotFoundOnDeletion() {
        UUID questionId = randomUUID();
        UUID answerId = randomUUID();

        when(questionRepository.existsById(any(UUID.class))).thenReturn(true);
        when(answerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception e = assertThrows(EntityNotFoundException.class, () -> answerService.deleteAnswer(questionId, answerId));
        assertEquals(ENTITY_NOT_FOUND_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(answerRepository, never()).deleteById(answerId);
    }

    @Test
    void shouldNotDeleteAnswerWhenUserIsNotTheOwner() {
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
        UUID answerId = randomUUID();

        when(questionRepository.existsById(any(UUID.class))).thenReturn(true);
        AnswerEntity answerEntity = AnswerEntity.builder().id(answerId).userEntity(anotherUser).build();
        when(answerRepository.findById(any(UUID.class))).thenReturn(Optional.of(answerEntity));

        Exception e = assertThrows(EntityDoesNotBelongToUserException.class, () -> answerService.deleteAnswer(questionId, answerId));

        assertEquals(ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION, e.getMessage(), "Exception message should match");

        verify(answerRepository, never()).deleteById(answerId);
    }
}
