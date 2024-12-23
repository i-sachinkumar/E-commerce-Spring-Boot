package com.ihrsachin.ecommerce.service.impl;

import com.ihrsachin.ecommerce.exception.APIException;
import com.ihrsachin.ecommerce.model.Category;
import com.ihrsachin.ecommerce.payload.CategoryDTO;
import com.ihrsachin.ecommerce.payload.CategoryResponse;
import com.ihrsachin.ecommerce.repo.CategoryRepo;
import com.ihrsachin.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;


    @Override
    public CategoryResponse getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);

        List<CategoryDTO> categoryDTOS = categoryPage
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        return CategoryResponse.builder()
                .content(categoryDTOS)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .lasPage(categoryPage.isLast())
                .build();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        Category prevCategory = categoryRepo.findByCategoryName(category.getCategoryName());

        if (prevCategory != null)
            throw new APIException(
                    String.format(
                            "Category with name %s already exist",
                            category.getCategoryName()
                    )
            );
        Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO updatedCategoryDTO) {
        Category savedCategory = categoryRepo.findById(categoryId)
                .orElse(null);
        if (savedCategory == null)
            throw new APIException(
                    String.format(
                            "Category with id: %d does not exist",
                            categoryId
                    )
            );

        Category updatedCategory = modelMapper.map(updatedCategoryDTO, Category.class);
        updatedCategory.setCategoryId(categoryId);
        savedCategory = categoryRepo.save(updatedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category savedCategory = categoryRepo.findById(categoryId)
                .orElse(null);
        if (savedCategory == null)
            throw new APIException(
                    String.format(
                            "Category with id: %d does not exist",
                            categoryId
                    )
            );

        //delete it
        categoryRepo.delete(savedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
