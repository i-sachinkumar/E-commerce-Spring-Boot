package com.ihrsachin.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.ihrsachin.ecommerce.model.Cart}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto{
    Long cartId;
    Double totalPrice;
    List<ProductDTO> products = new ArrayList<>();
}