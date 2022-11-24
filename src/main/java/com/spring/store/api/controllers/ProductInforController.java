package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Image;
import com.spring.store.api.models.ProductInfor;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.ProductInforRepository;
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
public class ProductInforController {
    @Autowired
    ProductInforRepository productInforRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeRepository sizeRepository;


    //    retrieve a ProductInfor by id
    @GetMapping("/productInfor/{id}")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ProductInfor> getProductIdById(@PathVariable(value = "id") Long id) {
        ProductInfor productInfor = productInforRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ProductInfor with id = " + id));

        return new ResponseEntity<>(productInfor, HttpStatus.OK);
    }

    //    create new Infor for a Product
    @PostMapping("/product/{productId}/productInfor")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ProductInfor> createProductInfor(@PathVariable(value = "productId") Long productId,
                                                           @RequestBody ProductInfor productInforRequest) {
//        Long productId = Long.valueOf(productInforRequest.getProduct().getId());
        if (productInforRepository.existsBySizeAndProductId(productInforRequest.getSize(), productId)) {
            throw new ResourceNotFoundException("Already existed size id: " + productInforRequest.getSize());
        }
        ProductInfor productInfor = productRepository.findById(productId).map(product -> {
            productInforRequest.setProduct(product);
            return productInforRepository.save(productInforRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + productId));
        return new ResponseEntity<>(productInfor, HttpStatus.CREATED);

    }

    //    retrieve all infor of a Product
    @GetMapping("/product/{productId}/infors")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAllInforsByProductId(@PathVariable(value = "productId") Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Not found Product with id = " + productId);
        }

//        List<Image> images = imageRepository.findByProductId(productId);
        List<ProductInfor> productInfors = productInforRepository.findByProductId(productId);
        return new ResponseEntity<>(productInfors, HttpStatus.OK);
    }

    //    update a productInfor by productInfor_id
    @PutMapping("/productInfors/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProductInfor(@PathVariable("id") long id, @RequestBody ProductInfor productInforRequest) {
        ProductInfor productInfor = productInforRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product infor Id " + id + "not found"));
        productInfor.setAmount(productInforRequest.getAmount());
        productInfor.setSize(productInforRequest.getSize());
        productInforRepository.save(productInfor);
        return ResponseEntity.ok().body(new MessageResponse("Information of product has been updated successfully!"));
    }

    //    delete User by id
    @DeleteMapping("/productInfor/{id}")
    public ResponseEntity<HttpStatus> deleteDetails(@PathVariable("id") long id) {
        productInforRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
