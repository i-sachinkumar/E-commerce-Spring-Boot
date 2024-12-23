package com.ihrsachin.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "products")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;


    @NotNull
    @Size(min = 3)
    private String productName;

    private String description;
    private String image;

    @NotNull
    @Min(1)
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}