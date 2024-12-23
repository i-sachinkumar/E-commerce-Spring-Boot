package com.ihrsachin.ecommerce.controller;

import com.ihrsachin.ecommerce.config.AppConstants;
import com.ihrsachin.ecommerce.payload.CategoryDTO;
import com.ihrsachin.ecommerce.payload.CategoryResponse;
import com.ihrsachin.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            ) Integer pageNumber,
            @RequestParam(
                    name = "pageSize",
                    defaultValue = AppConstants.PAGE_SIZE,
                    required = false
            ) Integer itemCount,
            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_CATEGORIES_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_CATEGORIES_DIRECTION,
                    required = false
            ) String sortOrder
    ) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, itemCount, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @PutMapping("admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO updatedCategoryDTO, @PathVariable Long categoryId) {
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryId, updatedCategoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }

}