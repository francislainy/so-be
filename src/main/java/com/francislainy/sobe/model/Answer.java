package com.francislainy.sobe.model;

import com.francislainy.sobe.entity.AnswerEntity;
import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotBlank
    private String content;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID questionId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID userId;

    // to entity
    public AnswerEntity toEntity() {
        return AnswerEntity.builder()
                .id(id)
                .content(content)
                .questionEntity(QuestionEntity.builder().id(questionId).build())
                .userEntity(UserEntity.builder().id(userId).build())
                .build();
    }
}
