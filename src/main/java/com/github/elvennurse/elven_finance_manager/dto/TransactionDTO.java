package com.github.elvennurse.elven_finance_manager.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Type is required")
    private String type; // "INCOME" або "EXPENSE"

    private String category; // Назва категорії

    private String description;

    @NotNull(message = "Date is required")
    private LocalDateTime date;
}
