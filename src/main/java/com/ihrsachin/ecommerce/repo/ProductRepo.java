package com.ihrsachin.ecommerce.repo;

import com.ihrsachin.ecommerce.model.Category;
import com.ihrsachin.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findProductsByCategory(Category category);
    Page<Product> findProductsByCategory(Category category, Pageable pageable);
    Page<Product> findProductsByProductNameLikeIgnoreCase(String keyword, Pageable pageable);
}
