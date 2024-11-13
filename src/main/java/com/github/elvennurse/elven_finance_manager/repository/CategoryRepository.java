package com.github.elvennurse.elven_finance_manager.repository;

import com.github.elvennurse.elven_finance_manager.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Знаходить всі категорії для конкретного користувача
    List<Category> findByUserId(Long userId);

    // Знаходить категорію за назвою та ідентифікатором користувача
    Optional<Category> findByNameAndUserId(String name, Long userId);

    // Знаходить категорію за ідентифікатором та ідентифікатором користувача
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}
