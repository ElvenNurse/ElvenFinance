package com.github.elvennurse.elven_finance_manager.service;

import com.github.elvennurse.elven_finance_manager.dto.TransactionDTO;
import com.github.elvennurse.elven_finance_manager.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions(Long userId);
    Transaction createTransaction(Long userId, TransactionDTO dto);
    Transaction updateTransaction(Long userId, Long transactionId, TransactionDTO dto);
    void deleteTransaction(Long userId, Long transactionId);
}
