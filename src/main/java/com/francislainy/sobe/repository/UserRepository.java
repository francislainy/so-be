package com.francislainy.sobe.repository;

import com.francislainy.sobe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity registerUser(UserEntity userEntity);
}
