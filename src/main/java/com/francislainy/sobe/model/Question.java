package com.francislainy.sobe.model;

import com.francislainy.sobe.entity.QuestionEntity;
import com.francislainy.sobe.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class Question {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    private UUID userId;

    // map to entity
    public QuestionEntity toEntity() {
        return QuestionEntity.builder()
                .id(id)
                .title(title)
                .content(content)
                .userEntity(UserEntity.builder().id(userId).build())
                .createdAt(createdAt)
                .build();
    }
}
