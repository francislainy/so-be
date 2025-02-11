package com.francislainy.sobe.service;

import com.francislainy.sobe.model.Answer;

import java.util.UUID;

public interface AnswerService {

    Answer createAnswer(UUID questionId, Answer answer);

    void deleteAnswer(UUID questionId, UUID answerId);
}
