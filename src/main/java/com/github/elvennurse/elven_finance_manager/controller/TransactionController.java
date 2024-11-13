package com.github.elvennurse.elven_finance_manager.controller;

import com.github.elvennurse.elven_finance_manager.dto.ApiResponse;
import com.github.elvennurse.elven_finance_manager.dto.TransactionDTO;
import com.github.elvennurse.elven_finance_manager.model.Transaction;
import com.github.elvennurse.elven_finance_manager.service.TransactionService;
import com.github.elvennurse.elven_finance_manager.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Отримання всіх транзакцій для поточного користувача.
     * @param principal Принципал аутентифікованого користувача.
     * @return Список транзакцій.
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        List<Transaction> transactions = transactionService.getAllTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Створення нової транзакції для поточного користувача.
     * @param dto Об'єкт запиту з даними транзакції.
     * @param principal Принципал аутентифікованого користувача.
     * @return Створена транзакція.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO dto, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Transaction transaction = transactionService.createTransaction(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * Оновлення транзакції що існує.
     * @param id Ідентифікатор транзакції.
     * @param dto Об'єкт запиту з новими даними транзакції.
     * @param principal Принципал аутентифікованого користувача.
     * @return Оновлена транзакція.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO dto, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Transaction updatedTransaction = transactionService.updateTransaction(userId, id, dto);
        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * Видалення транзакції.
     * @param id Ідентифікатор транзакції.
     * @param principal Принципал аутентифікованого користувача.
     * @return ApiResponse з повідомленням про успіх.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTransaction(@PathVariable Long id, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        transactionService.deleteTransaction(userId, id);
        return ResponseEntity.ok(new ApiResponse(true, "Transaction deleted successfully"));
    }

    /**
     * Метод для отримання userId з Principal через Spring Security.
     * @param principal Принципал аутентифікованого користувача.
     * @return Ідентифікатор користувача.
     */
    private Long getUserIdFromPrincipal(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        }
        throw new RuntimeException("Invalid principal");
    }
}
