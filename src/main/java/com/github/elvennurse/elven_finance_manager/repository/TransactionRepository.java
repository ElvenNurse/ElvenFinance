package com.github.elvennurse.elven_finance_manager.repository;

import com.github.elvennurse.elven_finance_manager.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Знаходить всі транзакції для конкретного користувача
    List<Transaction> findByUserId(Long userId);

    // Знаходить транзакції за користувачем та категорією
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);

    // Знаходить транзакції за користувачем та типом
    List<Transaction> findByUserIdAndType(Long userId, String type);
}
