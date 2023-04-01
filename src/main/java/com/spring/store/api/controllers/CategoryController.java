package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Account;
import com.spring.store.api.models.Category;
import com.spring.store.api.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.store.api.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    //    get all categories
    @GetMapping("/category")
    // @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    //    get all category pageable
    @GetMapping("/category/pageable")
    public Page<Category> getAllCategoryPageable(@RequestParam int page,
                                                 @RequestParam int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("id"));
        return categoryRepository.findAll(paging);
    }

    //    get Category by id
    @GetMapping("/category/{id}")
    // @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + id));

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //    create a New Category
    @PostMapping("/category")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody Category categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setStatus(categoryRequest.getStatus());
        category.setCreatedBy(categoryRequest.getCreatedBy());
        category.setCreatedDate(categoryRequest.getCreatedDate());
        category.setModifiedBy(categoryRequest.getModifiedDate());
        category.setModifiedDate(categoryRequest.getModifiedDate());
        categoryRepository.save(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    //    update a category by id
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id, @RequestBody Category categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + id));

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setStatus(categoryRequest.getStatus());
        category.setCreatedBy(categoryRequest.getCreatedBy());
        category.setCreatedDate(categoryRequest.getCreatedDate());
        category.setModifiedBy(categoryRequest.getModifiedBy());
        category.setModifiedDate(categoryRequest.getModifiedDate());
        categoryRepository.save(category);
        return ResponseEntity.ok().body(new MessageResponse("Category has been updated successfully!"));
    }

}
