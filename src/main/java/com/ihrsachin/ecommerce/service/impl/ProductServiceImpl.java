package com.ihrsachin.ecommerce.service.impl;

import com.ihrsachin.ecommerce.exception.APIException;
import com.ihrsachin.ecommerce.exception.ResourceNotFoundException;
import com.ihrsachin.ecommerce.model.Category;
import com.ihrsachin.ecommerce.model.Product;
import com.ihrsachin.ecommerce.payload.CategoryDTO;
import com.ihrsachin.ecommerce.payload.CategoryResponse;
import com.ihrsachin.ecommerce.payload.ProductDTO;
import com.ihrsachin.ecommerce.payload.ProductResponse;
import com.ihrsachin.ecommerce.repo.CategoryRepo;
import com.ihrsachin.ecommerce.repo.ProductRepo;
import com.ihrsachin.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        for(Product product : category.getProducts()){
            if(product.getProductName().equals(productDTO.getProductName())){
                throw new APIException("Product: " + product.getProductName() + " already exists in this category");
            }
        }

        Product product = modelMapper.map(productDTO, Product.class);
        if (product.getProductId() != null) {
            product.setProductId(null); // Ensures productId is not set by the user
        }

        product.setImage("default.png");
        product.setCategory(category);
        Double specialPrize = product.getPrice() -
                (product.getDiscount()*0.01*product.getPrice());
        product.setSpecialPrice(specialPrize);

        Product savedProduct = productRepo.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findAll(pageDetails);

        List<ProductDTO> productDTOS = productPage
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();


        return ProductResponse.builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lasPage(productPage.isLast())
                .build();
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findProductsByCategory(category, pageDetails);

        List<ProductDTO> productDTOS = productPage
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return ProductResponse.builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lasPage(productPage.isLast())
                .build();
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findProductsByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<ProductDTO> productDTOS = productPage
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return ProductResponse.builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lasPage(productPage.isLast())
                .build();
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product savedProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        savedProduct.setProductName(productDTO.getProductName());
        savedProduct.setPrice(productDTO.getPrice());
        savedProduct.setDiscount(productDTO.getDiscount());
        savedProduct.setDescription(productDTO.getDescription());
        savedProduct.setQuantity(productDTO.getQuantity());
        savedProduct.setSpecialPrice(productDTO.getPrice() - (productDTO.getDiscount()*0.01*productDTO.getPrice()));

        return modelMapper.map(productRepo.save(savedProduct), ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepo.deleteById(productId);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) {
        // get product from db
        Product savedProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // upload image to server
        // get image name
        String path = "images/";
        String fileName;
        try {
            fileName = uploadImage(path, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // update product image to product object
        savedProduct.setImage(fileName);

        // save product object
        Product updatedProduct = productRepo.save(savedProduct);

        // return productDTO object
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();

        //generate unique file name
        assert originalFilename != null;
        String filename = UUID.randomUUID().toString().concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + filename;

        //check if path exists, create if not
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        //save image to server
        Files.copy(image.getInputStream(), Paths.get(filePath));

        return filename;
    }
}
