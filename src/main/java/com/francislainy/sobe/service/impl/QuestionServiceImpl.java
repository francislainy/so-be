package com.francislainy.sobe.service.impl;

import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.repository.QuestionRepository;
import com.francislainy.sobe.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question.toEntity()).toModel();
    }
}
