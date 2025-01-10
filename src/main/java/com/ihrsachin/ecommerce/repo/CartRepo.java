package com.ihrsachin.ecommerce.repo;

import com.ihrsachin.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
}
