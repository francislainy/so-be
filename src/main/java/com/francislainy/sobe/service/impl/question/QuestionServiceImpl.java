package com.francislainy.sobe.service.impl.question;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.exception.EntityDoesNotBelongToUserException;
import com.francislainy.sobe.exception.QuestionNotFoundException;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.QuestionService;
import com.francislainy.sobe.service.impl.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.francislainy.sobe.exception.AppExceptionHandler.ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION;
import static com.francislainy.sobe.exception.AppExceptionHandler.QUESTION_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CurrentUserService currentUserService;

    @Override
    public Question createQuestion(Question question) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        QuestionEntity questionEntity = question.withCreatedAt(LocalDateTime.now()).withUserId(currentUser.getId()).toEntity();
        return questionRepository.save(questionEntity).toModel();
    }

    @Override
    public Question getQuestion(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QUESTION_NOT_FOUND_EXCEPTION))
                .toModel();
    }

    @Override
    public Question updateQuestion(UUID questionId, Question question) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        QuestionEntity questionEntity = questionRepository.findById(questionId) // todo: add updated at date - 20/11/2024
                .orElseThrow(() -> new QuestionNotFoundException(QUESTION_NOT_FOUND_EXCEPTION));

        // Check ownership
        if (!questionEntity.getUserEntity().getId().equals(currentUser.getId())) {
            throw new EntityDoesNotBelongToUserException(ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION);
        }

        questionEntity = questionEntity.withTitle(question.getTitle()).withContent(question.getContent());

        return questionRepository.save(questionEntity).toModel();
    }

    @Override
    public void deleteQuestion(UUID questionID) {
        QuestionEntity questionEntity = questionRepository.findById(questionID)
                .orElseThrow(() -> new QuestionNotFoundException(QUESTION_NOT_FOUND_EXCEPTION));

        UserEntity currentUser = currentUserService.getCurrentUser();

        // Check ownership
        if (!questionEntity.getUserEntity().getId().equals(currentUser.getId())) {
            throw new EntityDoesNotBelongToUserException(ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION);
        }

        questionRepository.deleteById(questionEntity.getId());
    }
}
