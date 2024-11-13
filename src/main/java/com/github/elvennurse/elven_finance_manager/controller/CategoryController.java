package com.github.elvennurse.elven_finance_manager.controller;

import com.github.elvennurse.elven_finance_manager.dto.CategoryDTO;
import com.github.elvennurse.elven_finance_manager.dto.ApiResponse;
import com.github.elvennurse.elven_finance_manager.model.Category;
import com.github.elvennurse.elven_finance_manager.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Отримання всіх категорій для поточного користувача.
     * @param principal Принципал аутентифікованого користувача.
     * @return Список категорій.
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        List<Category> categories = categoryService.getAllCategories(userId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Створення нової категорії для поточного користувача.
     * @param dto Об'єкт запиту з даними категорії.
     * @param principal Принципал аутентифікованого користувача.
     * @return Створена категорія.
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDTO dto, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Category category = new Category();
        category.setName(dto.getName());
        Category createdCategory = categoryService.createCategory(userId, category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * Оновлення існуючої категорії.
     * @param id Ідентифікатор категорії.
     * @param dto Об'єкт запиту з новими даними категорії.
     * @param principal Принципал аутентифікованого користувача.
     * @return Оновлена категорія.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        Category updatedCategory = new Category();
        updatedCategory.setName(dto.getName());
        Category category = categoryService.updateCategory(userId, id, updatedCategory);
        return ResponseEntity.ok(category);
    }

    /**
     * Видалення категорії.
     * @param id Ідентифікатор категорії.
     * @param principal Принципал аутентифікованого користувача.
     * @return ApiResponse з повідомленням про успіх.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        categoryService.deleteCategory(userId, id);
        return ResponseEntity.ok(new ApiResponse(true, "Category deleted successfully"));
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
