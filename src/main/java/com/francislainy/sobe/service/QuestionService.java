package com.francislainy.sobe.service;

import com.francislainy.sobe.model.Question;

import java.util.UUID;

public interface QuestionService {
    Question createQuestion(Question question);

    Question getQuestion(UUID questionID);

    Question updateQuestion(UUID questionID, Question question);

    void deleteQuestion(UUID questionID);
}
