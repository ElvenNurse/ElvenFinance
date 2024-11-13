package com.github.elvennurse.elven_finance_manager.repository;

import com.github.elvennurse.elven_finance_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Знаходить користувача за username або email
    Optional<User> findByUsernameOrEmail(String username, String email);

    // Перевіряє наявність користувача за username
    Boolean existsByUsername(String username);

    // Перевіряє наявність користувача за email
    Boolean existsByEmail(String email);
}
