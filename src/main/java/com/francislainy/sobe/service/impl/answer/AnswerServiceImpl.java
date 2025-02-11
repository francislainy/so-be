package com.francislainy.sobe.service.impl.answer;

import com.francislainy.sobe.entity.AnswerEntity;
import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.exception.EntityDoesNotBelongToUserException;
import com.francislainy.sobe.exception.EntityNotFoundException;
import com.francislainy.sobe.exception.QuestionNotFoundException;
import com.francislainy.sobe.model.Answer;
import com.francislainy.sobe.repository.AnswerRepository;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.AnswerService;
import com.francislainy.sobe.service.impl.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.francislainy.sobe.exception.AppExceptionHandler.ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION;
import static com.francislainy.sobe.exception.AppExceptionHandler.ENTITY_NOT_FOUND_EXCEPTION;
import static com.francislainy.sobe.exception.AppExceptionHandler.QUESTION_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    private final CurrentUserService currentUserService;

    @Override
    public Answer createAnswer(UUID questionId, Answer answer) {
        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(QUESTION_NOT_FOUND_EXCEPTION));

        AnswerEntity answerEntity = answer.toEntity();
        answerEntity = answerEntity.withQuestionEntity(questionEntity);
        answerEntity = answerRepository.save(answerEntity);

        return answerEntity.toModel();
    }

    @Override
    public void deleteAnswer(UUID questionId, UUID answerId) {
        if (!questionRepository.existsById(questionId)) {
            throw new QuestionNotFoundException(QUESTION_NOT_FOUND_EXCEPTION);
        }

        AnswerEntity answerEntity = answerRepository.findById(answerId).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION));

        UserEntity currentUser = currentUserService.getCurrentUser();

        // Check ownership
        if (!answerEntity.getUserEntity().getId().equals(currentUser.getId())) {
            throw new EntityDoesNotBelongToUserException(ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION);
        }

        answerRepository.deleteById(answerId);
    }
}
