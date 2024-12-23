package com.ihrsachin.ecommerce.repo;

import com.ihrsachin.ecommerce.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCategoryName(@NotBlank @Size(min = 3) String categoryName);
}
