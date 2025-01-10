package com.ihrsachin.ecommerce.repo;

import com.ihrsachin.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
}
