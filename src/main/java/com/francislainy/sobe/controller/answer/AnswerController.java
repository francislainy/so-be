package com.francislainy.sobe.controller.answer;

import com.francislainy.sobe.model.Answer;
import com.francislainy.sobe.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questions/{questionId}/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @Operation(summary = "Create a new answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Answer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Answer> createAnswer(@PathVariable UUID questionId, @Valid @RequestBody Answer answer) {
        return new ResponseEntity<>(answerService.createAnswer(questionId, answer), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an answer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Answer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Answer not found")
    })
    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable UUID questionId, @PathVariable UUID answerId) {
        answerService.deleteAnswer(questionId, answerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}