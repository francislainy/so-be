package com.francislainy.sobe.entity;

import com.francislainy.sobe.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
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
