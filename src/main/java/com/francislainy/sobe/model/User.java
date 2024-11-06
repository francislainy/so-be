package com.francislainy.sobe.model;

import com.francislainy.sobe.entity.UserEntity;
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
public class User {

    private UUID id;
    private String username;
    private String password;

    // map to entity
    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
    }
}
