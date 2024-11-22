package com.francislainy.sobe.controller.question;

import com.francislainy.sobe.model.Question;
import com.francislainy.sobe.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question) {
        return new ResponseEntity<>(questionService.createQuestion(question), HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<Question> updateQuestion(@PathVariable UUID questionId, @Valid @RequestBody Question question) {
        return new ResponseEntity<>(questionService.updateQuestion(questionId, question), HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID questionId) {
        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
