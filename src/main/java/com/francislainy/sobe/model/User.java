package com.francislainy.sobe.model;

import com.francislainy.sobe.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

import static com.francislainy.sobe.enums.UserType.USER;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class User {

    private UUID id;
    private String username;
    private String password;
    private String role = USER.toString();

    // map to entity
    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
