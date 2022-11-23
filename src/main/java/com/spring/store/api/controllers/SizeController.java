package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Product;
import com.spring.store.api.models.Size;
import com.spring.store.api.repository.ProductRepository;
import com.spring.store.api.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
}
