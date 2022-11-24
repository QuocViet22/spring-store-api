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
    public ResponseEntity<?> createLineItem(@PathVariable(value = "wishListId") Long wishListId,
                                            @PathVariable(value = "productId") Long productId,
                                            @RequestBody LineItem lineItemRequest) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Wish List with id = " + wishListId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + productId));
        int amountOfRequest = Integer.valueOf(lineItemRequest.getAmount());
        if (Integer.valueOf(product.getAmount()) < amountOfRequest) {
            throw new ResourceNotFoundException(product.getName() + "'s amount is " + Integer.valueOf(product.getAmount()) + " .Please choose again!");
        }
        if (lineItemRepository.existsByProductAndWishListIdAndSize(product, wishListId, lineItemRequest.getSize())) {
            LineItem lineItem = lineItemRepository.findByProductAndWishListId(product, wishListId);
            //  Update amount
            int oldAmount = Integer.parseInt(lineItem.getAmount());
            if (Integer.valueOf(product.getAmount()) < amountOfRequest + oldAmount) {
                throw new ResourceNotFoundException(product.getName() + "'s amount is " + Integer.valueOf(product.getAmount()) + " .Please choose again!");
            }
            int newAmount = Integer.parseInt(lineItemRequest.getAmount());
            String totalAmount = String.valueOf(oldAmount + newAmount);
            lineItem.setAmount(totalAmount);
            //  Update Total
            int oldTotal = Integer.parseInt(lineItem.getTotal());
            int totalOfProduct = Integer.valueOf(product.getPrice());
            int amountOfLineItem = Integer.valueOf(lineItemRequest.getAmount());
            int newTotal = totalOfProduct * amountOfLineItem;
            String total = String.valueOf(oldTotal + newTotal);
            lineItem.setTotal(total);
            //  Update lineItem
            lineItemRepository.save(lineItem);
            return new ResponseEntity<>(lineItem, HttpStatus.CREATED);
        } else {
            LineItem lineItem = new LineItem();
            lineItem.setAmount(lineItemRequest.getAmount());
            lineItem.setSize(lineItemRequest.getSize());
            lineItem.setProduct(product);
            lineItem.setWishList(wishList);
            // Calculate total of LineItem
            int totalOfProduct = Integer.valueOf(product.getPrice());
            int amountOfLineItem = Integer.valueOf(lineItemRequest.getAmount());
            int total = totalOfProduct * amountOfLineItem;
            lineItem.setTotal(String.valueOf(total));
            lineItemRepository.save(lineItem);
            return new ResponseEntity<>(lineItem, HttpStatus.CREATED);
        }
    }


    //    update a lineItem by lineItem_id
    @PutMapping("/lineItem/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateLineItem(@PathVariable("id") long id, @RequestBody LineItem lineItemRequest) {
        LineItem lineItem = lineItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Line item Id " + id + "not found"));
        //        Update amount
        if (Integer.valueOf(lineItem.getProduct().getAmount()) < Integer.valueOf(lineItemRequest.getAmount())) {
            throw new ResourceNotFoundException(lineItem.getProduct().getName() + "'s amount is " + Integer.valueOf(lineItem.getProduct().getAmount()) + " .Please choose again!");
        }
        int priceOfProduct = Integer.valueOf(lineItem.getProduct().getPrice());
        int amountOfLineItem = Integer.valueOf(lineItemRequest.getAmount());
        int total = priceOfProduct * amountOfLineItem;
        lineItem.setAmount(lineItemRequest.getAmount());
        lineItem.setTotal(String.valueOf(total));
        lineItemRepository.save(lineItem);
        return ResponseEntity.ok().body(new MessageResponse("Line item has been updated successfully!"));
    }

    //    update status of LineItem
    @PutMapping("/lineItem/status/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateStatusLineItem(@PathVariable("id") long id, @RequestBody LineItem lineItemRequest) {
        LineItem lineItem = lineItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Line item Id " + id + "not found"));
        lineItem.setStatus("1");
        lineItemRepository.save(lineItem);
        return ResponseEntity.ok().body(new MessageResponse("Line item status has been updated successfully!"));
    }

    //    delete LineItem by id
    @DeleteMapping("/lineItem/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> deleteLineItem(@PathVariable("id") long id) {
        lineItemRepository.deleteById(id);
        return ResponseEntity.ok().body(new MessageResponse("Line item has been deleted successfully!"));
    }

    //    delete all LineItems
    @DeleteMapping("/lineItems/{wishListId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> deleteAllLineItemOfWishList(@PathVariable("wishListId") long wishListId) {
        if (!wishListRepository.existsById(wishListId)) {
            throw new ResourceNotFoundException("Not found Wish List with id = " + wishListId);
        }
        lineItemRepository.deleteAllByWishListId(wishListId);
        return ResponseEntity.ok().body(new MessageResponse("All line items have been deleted successfully!"));
    }
}
