package com.francislainy.sobe.entity;

import com.francislainy.sobe.model.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
@With
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

    @OneToMany
    private List<AnswerEntity> answers;

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
