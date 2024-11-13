package com.github.elvennurse.elven_finance_manager.service;

import com.github.elvennurse.elven_finance_manager.dto.TransactionDTO;
import com.github.elvennurse.elven_finance_manager.exception.ResourceNotFoundException;
import com.github.elvennurse.elven_finance_manager.model.Category;
import com.github.elvennurse.elven_finance_manager.model.Transaction;
import com.github.elvennurse.elven_finance_manager.model.TransactionType;
import com.github.elvennurse.elven_finance_manager.model.User;
import com.github.elvennurse.elven_finance_manager.repository.CategoryRepository;
import com.github.elvennurse.elven_finance_manager.repository.TransactionRepository;
import com.github.elvennurse.elven_finance_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Transaction createTransaction(Long userId, TransactionDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());

        // Перевірка та встановлення типу транзакції
        try {
            transaction.setType(TransactionType.valueOf(dto.getType()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid transaction type.");
        }

        // Встановлення категорії, якщо вказано
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
            Category category = categoryRepository.findByNameAndUserId(dto.getCategory(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
            transaction.setCategory(category);
        }

        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long userId, Long transactionId, TransactionDTO dto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));

        transaction.setAmount(dto.getAmount());

        // Перевірка та встановлення типу транзакції
        try {
            transaction.setType(TransactionType.valueOf(dto.getType()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid transaction type.");
        }

        // Встановлення категорії, якщо вказано
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
            Category category = categoryRepository.findByNameAndUserId(dto.getCategory(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }

        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));
        transactionRepository.delete(transaction);
    }
}
