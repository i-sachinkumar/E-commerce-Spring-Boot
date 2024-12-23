package com.ihrsachin.ecommerce.controller;

import com.ihrsachin.ecommerce.config.AppConstants;
import com.ihrsachin.ecommerce.payload.ProductDTO;
import com.ihrsachin.ecommerce.payload.ProductResponse;
import com.ihrsachin.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class ProductController {

    private final ProductService productService;

    @PostMapping("admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
        ProductDTO savedProductDTO = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = "0",
                    required = false
            ) Integer pageNumber,
            @RequestParam(
                    name = "pageSize",
                    defaultValue = "50",
                    required = false
            ) Integer pageSize,
            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_PRODUCTS_DIRECTION,
                    required = false
            ) String sortOrder
    ) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = "0",
                    required = false
            ) Integer pageNumber,
            @RequestParam(
                    name = "pageSize",
                    defaultValue = "50",
                    required = false
            ) Integer pageSize,
            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_PRODUCTS_DIRECTION,
                    required = false
            ) String sortOrder
    ) {
        ProductResponse productResponse = productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("public/products/search/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = "0",
                    required = false
            ) Integer pageNumber,
            @RequestParam(
                    name = "pageSize",
                    defaultValue = "50",
                    required = false
            ) Integer pageSize,
            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_PRODUCTS_DIRECTION,
                    required = false
            ) String sortOrder) {
        ProductResponse productResponse = productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO updatedProductDTO = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }

    @PutMapping("admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) {
        ProductDTO updatedProductDTO = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }
}