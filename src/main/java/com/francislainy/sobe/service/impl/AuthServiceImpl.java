package com.francislainy.sobe.service.impl;

import com.francislainy.sobe.entity.UserEntity;
import com.francislainy.sobe.model.User;
import com.francislainy.sobe.repository.UserRepository;
import com.francislainy.sobe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        UserEntity userEntity = userRepository.registerUser(user.toEntity());
        return userEntity.toModel();
    }
}
