package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Category;
import com.spring.store.api.models.Product;
import com.spring.store.api.models.Size;
import com.spring.store.api.payload.request.ProductRequest;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.payload.response.ProductInforResponse;
import com.spring.store.api.repository.CategoryRepository;
import com.spring.store.api.repository.ProductRepository;

import com.spring.store.api.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @GetMapping("/products/{sizeId}")
    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Product> getAllProductsBySize(@PathVariable(value = "sizeId") Long sizeId) {
        return productRepository.findProductBySize(sizeId);
        //        return productRepository.findAllProduct();
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
                                                 @RequestBody ProductRequest productRequest) {
//        Product product = categoryRepository.findById(categoryId).map(category -> {
//            productRequest.setCategory(category);
//
//            return productRepository.save(productRequest);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + categoryId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category id " + categoryId + " not found!"));
        Product product = new Product();
        product.setCategory(category);
        Set<String> strSizes = productRequest.getSizes();
        Set<Size> sizes = new HashSet<>();
        strSizes.forEach(size -> {
            switch (size) {
                case "3.5":
                    Size size1 = sizeRepository.findByValue("3.5")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size1);
                    break;
                case "4":
                    Size size2 = sizeRepository.findByValue("4")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size2);
                    break;
                case "4.5":
                    Size size3 = sizeRepository.findByValue("4.5")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size3);
                    break;
                case "5":
                    Size size4 = sizeRepository.findByValue("5")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size4);
                    break;
                case "5.5":
                    Size size5 = sizeRepository.findByValue("5.5")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size5);
                    break;
                case "6":
                    Size size6 = sizeRepository.findByValue("6")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size6);
                    break;
                case "6.5":
                    Size size7 = sizeRepository.findByValue("6.5")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size7);
                    break;
                case "7":
                    Size size8 = sizeRepository.findByValue("7")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size8);
                    break;
                case "7.5":
                    Size size9 = sizeRepository.findByValue("7.5")
                            .orElseThrow(() -> new RuntimeException("Error: Size is not found."));
                    sizes.add(size9);
                    break;
                default:
            }
            product.setSizes(sizes);
            productRepository.save(product);
        });
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
