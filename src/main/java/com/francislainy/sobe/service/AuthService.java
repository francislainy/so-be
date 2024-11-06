package com.francislainy.sobe.service;

import com.francislainy.sobe.model.User;

public interface AuthService {
    User registerUser(User user);

    User loginUser(User user);
}
