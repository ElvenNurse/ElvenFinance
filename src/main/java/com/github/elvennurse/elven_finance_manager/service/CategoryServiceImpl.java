package com.github.elvennurse.elven_finance_manager.service;

import com.github.elvennurse.elven_finance_manager.exception.ResourceNotFoundException;
import com.github.elvennurse.elven_finance_manager.model.Category;
import com.github.elvennurse.elven_finance_manager.model.User;
import com.github.elvennurse.elven_finance_manager.repository.CategoryRepository;
import com.github.elvennurse.elven_finance_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Category createCategory(Long userId, Category category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        category.setUser(user);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long userId, Long categoryId, Category updatedCategory) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        categoryRepository.delete(category);
    }
}
