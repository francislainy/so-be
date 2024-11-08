package com.francislainy.sobe.service.impl;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public Question createQuestion(Question question) {
        QuestionEntity questionEntity = question.withCreatedAt(LocalDateTime.now()).toEntity();
        return questionRepository.save(questionEntity).toModel();
    }
}
