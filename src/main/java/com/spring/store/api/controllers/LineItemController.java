package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.LineItem;
import com.spring.store.api.models.Product;
import com.spring.store.api.models.WishList;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.LineItemRepository;
import com.spring.store.api.repository.ProductRepository;
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
public class LineItemController {
    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    //    retrieve all LineItems of a WishList
    @GetMapping("/wishList/{wishListId}/lineItems")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<LineItem>> getLineItemsByWishListId(@PathVariable(value = "wishListId") Long wishListId) {
        if (!wishListRepository.existsById(wishListId)) {
            throw new ResourceNotFoundException("Not found Wish List with id = " + wishListId);
        }

        List<LineItem> lineItems = lineItemRepository.findByWishListId(wishListId);
        return new ResponseEntity<>(lineItems, HttpStatus.OK);
    }

    //    retrieve a LineItem by lineItem_id
    @GetMapping("/lineItem/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<LineItem> getLineItemById(@PathVariable(value = "id") Long id) {
        LineItem lineItem = lineItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found LineItem with id = " + id));

        return new ResponseEntity<>(lineItem, HttpStatus.OK);
    }

    //    create new LineItem of a WishList
    @PostMapping("/wishList/{wishListId}/lineItems/{productId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<LineItem> createLineItem(@PathVariable(value = "wishListId") Long wishListId,
                                                   @PathVariable(value = "productId") Long productId,
                                                   @RequestBody LineItem lineItemRequest) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Wish List with id = " + wishListId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + productId));
        LineItem lineItem = new LineItem();
        lineItem.setAmount(lineItemRequest.getAmount());
        lineItem.setTotal(lineItemRequest.getTotal());
        lineItem.setProduct(product);
        lineItem.setWishList(wishList);
        lineItemRepository.save(lineItem);

        return new ResponseEntity<>(lineItem, HttpStatus.CREATED);


//        LineItem lineItem = wishListRepository.findById(wishListId).map(wishList -> {
//            Product product = productRepository.findById(productId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + productId));
//            lineItemRequest.setAmount(lineItemRequest.getAmount());
//            lineItemRequest.setTotal(lineItemRequest.getTotal());
//            lineItemRequest.setProduct(product);
//            return lineItemRepository.save(lineItemRequest);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found WishList with id = " + wishListId));
//        return new ResponseEntity<>(lineItem, HttpStatus.CREATED);
    }

    //    update a lineItem by lineItem_id
    @PutMapping("/lineItem/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateLineItem(@PathVariable("id") long id, @RequestBody LineItem lineItemRequest) {
        LineItem lineItem = lineItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Line item Id " + id + "not found"));

        lineItem.setAmount(lineItemRequest.getAmount());
        lineItem.setTotal(lineItem.getTotal());
        lineItemRepository.save(lineItem);
        return ResponseEntity.ok().body(new MessageResponse("Line item has been updated successfully!"));
    }

    //    delete LineItem by id
    @DeleteMapping("/lineItem/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> deleteLineItem(@PathVariable("id") long id) {
        lineItemRepository.deleteById(id);
        return ResponseEntity.ok().body(new MessageResponse("Line item has been deleted successfully!"));
    }

    //    delete all LineItems
    @DeleteMapping("/lineItem")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> deleteAllLineItem() {
        lineItemRepository.deleteAll();
        return ResponseEntity.ok().body(new MessageResponse("All line items has been deleted successfully!"));
    }
}
