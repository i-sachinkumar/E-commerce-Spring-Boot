package com.ihrsachin.ecommerce.payload;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ihrsachin.ecommerce.model.CartItem}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto{
    Long cartItemId;
    CartDto cart;
    ProductDTO product;
    Integer quantity;
    Double itemPrice;
    Double discount;
}