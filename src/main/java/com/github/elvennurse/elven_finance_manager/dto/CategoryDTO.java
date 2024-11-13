package com.github.elvennurse.elven_finance_manager.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CategoryDTO {

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must be less than 50 characters")
    private String name;
}
