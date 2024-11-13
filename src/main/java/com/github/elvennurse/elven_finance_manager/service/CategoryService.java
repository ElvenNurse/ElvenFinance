package com.github.elvennurse.elven_finance_manager.service;

import com.github.elvennurse.elven_finance_manager.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories(Long userId);
    Category createCategory(Long userId, Category category);
    Category updateCategory(Long userId, Long categoryId, Category updatedCategory);
    void deleteCategory(Long userId, Long categoryId);
}
