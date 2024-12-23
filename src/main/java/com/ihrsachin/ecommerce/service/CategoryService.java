package com.ihrsachin.ecommerce.service;


import com.ihrsachin.ecommerce.payload.CategoryDTO;
import com.ihrsachin.ecommerce.payload.CategoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryResponse getAllCategories(int pageNumber, int itemCount, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO updatedCategory);
    CategoryDTO deleteCategory(Long categoryId);
}
