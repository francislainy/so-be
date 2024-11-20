package com.francislainy.sobe.service.impl.question;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.exception.QuestionNotFoundException;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.francislainy.sobe.exception.AppExceptionHandler.QUESTION_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public Question createQuestion(Question question) {
        QuestionEntity questionEntity = question.withCreatedAt(LocalDateTime.now()).toEntity();
        return questionRepository.save(questionEntity).toModel();
    }

    @Override
    public Question updateQuestion(UUID questionId, Question question) {
        QuestionEntity questionEntity = questionRepository.findById(questionId) // todo: add updated at date - 20/11/2024
                .orElseThrow(() -> new QuestionNotFoundException(QUESTION_NOT_FOUND_EXCEPTION));
        return questionRepository.save(questionEntity).toModel();
    }
}
