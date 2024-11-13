package com.github.elvennurse.elven_finance_manager.repository;

import com.github.elvennurse.elven_finance_manager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Знаходить роль за назвою
    Optional<Role> findByName(String name);
}
