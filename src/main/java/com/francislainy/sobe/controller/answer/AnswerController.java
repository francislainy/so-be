package com.francislainy.sobe.controller.answer;

import com.francislainy.sobe.model.Answer;
import com.francislainy.sobe.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questions/{questionId}/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<Answer> createAnswer(@PathVariable UUID questionId, @Valid @RequestBody Answer answer) {
        return new ResponseEntity<>(answerService.createAnswer(questionId, answer), HttpStatus.CREATED);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable UUID questionId, @PathVariable UUID answerId) {
        answerService.deleteAnswer(questionId, answerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
