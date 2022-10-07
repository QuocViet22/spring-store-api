package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Product;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.CategoryRepository;
import com.spring.store.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    //    retrieve all Products of a Category
    @GetMapping("/category/{categoryId}/products")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Product>> getAllProductsByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Not found Category with id = " + categoryId);
        }

        List<Product> products = productRepository.findByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //    retrieve a Product by product_id
    @GetMapping("/product/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Product> getProductsByCategoryId(@PathVariable(value = "id") Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + id));

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //    create new Product of a Category
    @PostMapping("/category/{categoryId}/products")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@PathVariable(value = "categoryId") Long categoryId,
                                                 @RequestBody Product productRequest) {
        Product product = categoryRepository.findById(categoryId).map(category -> {
            productRequest.setCategory(category);
            return productRepository.save(productRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + categoryId));

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    //    update a product by product_id
    @PutMapping("/product/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable("id") long id, @RequestBody Product productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Id " + id + "not found"));

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStatus(productRequest.getStatus());
        product.setAmount(productRequest.getAmount());
        product.setDescription(productRequest.getDescription());
        product.setCreatedBy(productRequest.getCreatedBy());
        product.setCreatedDate(productRequest.getCreatedDate());
        product.setModifiedBy(productRequest.getModifiedBy());
        product.setModifiedDate(productRequest.getModifiedDate());
        productRepository.save(product);
        return ResponseEntity.ok().body(new MessageResponse("Product has been updated successfully!"));
    }
}
