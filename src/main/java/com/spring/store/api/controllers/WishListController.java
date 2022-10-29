package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.LineItem;
import com.spring.store.api.models.WishList;
import com.spring.store.api.repository.LineItemRepository;
import com.spring.store.api.repository.UserRepository;
import com.spring.store.api.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class WishListController {
    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    //    retrieve a wishList by wishList_id
    @GetMapping("/wishList/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<WishList> getWishListById(@PathVariable(value = "id") Long id) {
        WishList wishList = wishListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found LineItem with id = " + id));

        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }

}
