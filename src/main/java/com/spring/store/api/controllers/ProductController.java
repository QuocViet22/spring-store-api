package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Category;
import com.spring.store.api.models.Product;
import com.spring.store.api.payload.request.FilterProductRequest;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.projection.IFilterProductResponse;
import com.spring.store.api.repository.CategoryRepository;
import com.spring.store.api.repository.ProductRepository;

import com.spring.store.api.repository.SizeRepository;
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

    @Autowired
    private SizeRepository sizeRepository;

    //    get all products
    @GetMapping("/products")
    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
        //        return productRepository.findAllProduct();
    }

    //    get all products by size
    @GetMapping("/products/{size}/size")
    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Product> getAllProductsBySize(@PathVariable(value = "size") String size) {
        return productRepository.findProductBySize(size);
        //        return productRepository.findAllProduct();
    }

    //    get all product by size and price
//    public List<Product> getAllProductsBySizeAndPrice(@RequestBody FilterProductRequest filterProductRequest) {
//        //        return productRepository.findAllProduct();
//
//        if (filterProductRequest.getSize() == null) {
//            return productRepository.findProductByPrice(filterProductRequest.getPrice());
//        } else if (filterProductRequest.getPrice() == null) {
//            return productRepository.findProductBySize(filterProductRequest.getSize());
//        } else
//            return productRepository.findProductBySizeAndPrice(filterProductRequest.getSize(), filterProductRequest.getPrice());
//    }
    @PostMapping("/products/filter")
    public List<Product> getAllProductsBySizeAndPrice(@RequestBody FilterProductRequest filterProductRequest) {
        //        return productRepository.findAllProduct();
//        List<IFilterProductResponse> iFilterProductResponses = productRepository.findProductBySizeAndPrice(filterProductRequest.getSize(), filterProductRequest.getPrice());
//        return iFilterProductResponses;
        if (filterProductRequest.getSize() == null && filterProductRequest.getPrice() == null)
            return productRepository.findAll();
        else if (filterProductRequest.getSize() == null) {
            return productRepository.findProductByPrice(filterProductRequest.getPrice());
        } else if (filterProductRequest.getPrice() == null) {
            return productRepository.findProductBySize(filterProductRequest.getSize());
        } else
            return productRepository.findProductByPriceAndSize(filterProductRequest.getPrice(), filterProductRequest.getSize());
    }

    //    retrieve all Products of a Category
    @GetMapping("/category/{categoryId}/products")
    // @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Product>> getAllProductsByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Not found Category with id = " + categoryId);
        }
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //    retrieve a Product by product_id
    @GetMapping("/product/{id}")
    //    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
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
            if (productRepository.existsByName(productRequest.getName())) {
                throw new ResourceNotFoundException("This product's name has been existed!");
            }
            if (Integer.valueOf(productRequest.getPrice()) < 0) {
                throw new ResourceNotFoundException("Price is not valid!");
            }
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
//        Boolean productName = Boolean.valueOf(product.getName());
//        if (productRepository.existsByName(productRequest.getName()) && productName) {
//            throw new ResourceNotFoundException("This product's name has been existed!");
//        }
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStatus(productRequest.getStatus());
//        product.setAmount(productRequest.getAmount());
        product.setDescription(productRequest.getDescription());
        product.setCreatedBy(productRequest.getCreatedBy());
        product.setCreatedDate(productRequest.getCreatedDate());
        product.setModifiedBy(productRequest.getModifiedBy());
        product.setModifiedDate(productRequest.getModifiedDate());
        productRepository.save(product);
        return ResponseEntity.ok().body(new MessageResponse("Product has been updated successfully!"));
    }
}
