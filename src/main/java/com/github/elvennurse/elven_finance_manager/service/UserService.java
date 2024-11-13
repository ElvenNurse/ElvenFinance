package com.github.elvennurse.elven_finance_manager.service;

import com.github.elvennurse.elven_finance_manager.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
