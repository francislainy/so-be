package com.francislainy.sobe.model;

import com.francislainy.sobe.entity.AnswerEntity;
import com.francislainy.sobe.entity.QuestionEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class Answer {

    private UUID questionId;

    private UUID id;

    @NotBlank
    private String content;

    // to entity
    public AnswerEntity toEntity() {
        return AnswerEntity.builder()
                .id(id)
                .content(content)
                .questionEntity(QuestionEntity.builder().id(questionId).build())
                .build();
    }
}
