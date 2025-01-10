package com.ihrsachin.ecommerce.controller;

import com.ihrsachin.ecommerce.payload.CartDto;
import com.ihrsachin.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDto cartDto = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }
}
