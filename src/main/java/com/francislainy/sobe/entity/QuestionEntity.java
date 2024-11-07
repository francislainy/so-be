package com.francislainy.sobe.entity;

import com.francislainy.sobe.model.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    private UserEntity userEntity;

    // map to model
    public Question toModel() {
        return Question.builder()
                .id(id)
                .title(title)
                .content(content)
                .userId(userEntity.getId())
                .createdAt(createdAt)
                .build();
    }

}
