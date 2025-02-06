package com.francislainy.sobe.entity;

import com.francislainy.sobe.model.Answer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Entity
@Table(name = "answers")
@Getter
@Setter
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String content;

    @ManyToOne
    private QuestionEntity questionEntity;

    // to model
    public Answer toModel() {
        return Answer.builder()
                .id(id)
                .content(content)
                .questionId(questionEntity.getId())
                .build();
    }

}
