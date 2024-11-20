package com.francislainy.sobe.service.impl.question;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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
        if (questionRepository.existsById(questionId)) {
            QuestionEntity questionEntity = question.withId(questionId).toEntity();
            return questionRepository.save(questionEntity).toModel();
        }

        throw new RuntimeException("Question not found");
    }
}
