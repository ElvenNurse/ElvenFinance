package com.github.elvennurse.elven_finance_manager.service;

import com.github.elvennurse.elven_finance_manager.exception.ResourceNotFoundException;
import com.github.elvennurse.elven_finance_manager.model.Role;
import com.github.elvennurse.elven_finance_manager.model.User;
import com.github.elvennurse.elven_finance_manager.repository.RoleRepository;
import com.github.elvennurse.elven_finance_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(User user) {
        // Хешування пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Призначення ролей
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("User Role not set."));
        user.setRoles(new HashSet<>(Set.of(userRole)));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
