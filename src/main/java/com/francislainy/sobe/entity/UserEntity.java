package com.francislainy.sobe.entity;

import com.francislainy.sobe.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String password;

    private String role;

    @OneToMany(mappedBy = "userEntity")
    private List<QuestionEntity> questions;

    @OneToMany(mappedBy = "userEntity")
    private List<AnswerEntity> answers;

    // map to model
    public User toModel() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
