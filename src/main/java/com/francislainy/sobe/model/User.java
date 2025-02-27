package com.francislainy.sobe.model;

import com.francislainy.sobe.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

import static com.francislainy.sobe.enums.UserRole.USER;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class User {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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
