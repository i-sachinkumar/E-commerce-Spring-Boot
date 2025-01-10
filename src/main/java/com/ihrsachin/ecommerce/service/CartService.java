package com.ihrsachin.ecommerce.service;


import com.ihrsachin.ecommerce.payload.CartDto;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    CartDto addProductToCart(Long productId, Integer quantity);
}
