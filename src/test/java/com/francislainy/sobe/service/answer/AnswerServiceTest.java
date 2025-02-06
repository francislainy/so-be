package com.francislainy.sobe.service.answer;

import com.francislainy.sobe.entity.AnswerEntity;
import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.exception.QuestionNotFoundException;
import com.francislainy.sobe.model.Answer;
import com.francislainy.sobe.repository.AnswerRepository;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.impl.answer.AnswerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static com.francislainy.sobe.exception.AppExceptionHandler.QUESTION_NOT_FOUND_EXCEPTION;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @InjectMocks
    AnswerServiceImpl answerService;

    @Mock
    AnswerRepository answerRepository;

    @Mock
    QuestionRepository questionRepository;

    @Test
    void shouldCreateAnswer() {
        UUID questionId = randomUUID();
        UUID answerId = randomUUID();

        QuestionEntity questionEntity = QuestionEntity.builder()
                .id(questionId)
                .build();

        Answer answer = Answer.builder()
                .questionId(questionId)
                .content("This is an answer")
                .build();

        AnswerEntity answerEntity = answer.withId(answerId).withQuestionId(questionId).toEntity();

        when(questionRepository.findById(any())).thenReturn(Optional.of(questionEntity));
        when(answerRepository.save(any())).thenReturn(answerEntity);

        Answer createdAnswer = answerService.createAnswer(questionId, answer);

        assertNotNull(createdAnswer, "Answer should not be null");

        assertAll(
                () -> assertNotNull(createdAnswer.getQuestionId(), "Question ID should not be null"),
                () -> assertNotNull(createdAnswer.getId(), "Answer ID should not be null"),
                () -> assertEquals(answer.getContent(), createdAnswer.getContent(), "Answer content should match")
        );
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
    }
}
