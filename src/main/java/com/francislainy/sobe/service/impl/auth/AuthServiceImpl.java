package com.francislainy.sobe.service.impl.auth;

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
        UserEntity userEntity = userRepository.save(user.toEntity());
        return userEntity.toModel();
    }

    @Override
    public User loginUser(User user) {
        return userRepository.findByUsername(user.getUsername())
                .map(UserEntity::toModel)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
