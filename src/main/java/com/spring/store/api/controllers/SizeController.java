package com.spring.store.api.controllers;

import com.spring.store.api.models.Size;
import com.spring.store.api.repository.ProductRepository;
import com.spring.store.api.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class SizeController {
    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    ProductRepository productRepository;

    //    get all sizes
    @GetMapping("/sizes")
    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

//    //    get all sizes
//    @GetMapping("/sizes/{productId}")
//    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
//    public List<ProductSize> getProducts(@PathVariable("productId") Long productId) {
//        List<ProductSize> productSizes = sizeRepository.findProductByProductId(productId);
//        return sizeRepository.findProductByProductId(productId);
//    }
}
