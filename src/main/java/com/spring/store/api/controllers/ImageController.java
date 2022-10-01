package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Image;
import com.spring.store.api.models.Product;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.ImageRepository;
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
public class ImageController {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    //    retrieve all Images of a Product
    @GetMapping("/product/{productId}/images")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Image>> getAllImagesByProductId(@PathVariable(value = "productId") Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Not found Product with id = " + productId);
        }

        List<Image> images = imageRepository.findByProductId(productId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    //    retrieve a Image by image_id
    @GetMapping("/images/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Image> getImagesByProductId(@PathVariable(value = "id") Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Image with id = " + id));

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    //    create new Image for a Product
    @PostMapping("/product/{productId}/images")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Image> createImage(@PathVariable(value = "productId") Long productId,
                                                 @RequestBody Image imageRequest) {
        Image image = productRepository.findById(productId).map(product -> {
            imageRequest.setProduct(product);
            return imageRepository.save(imageRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + productId));

        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

    //    update a image by image_id
    @PutMapping("/images/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateImage(@PathVariable("id") long id, @RequestBody Image imageRequest) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image Id " + id + "not found"));

        image.setLinkImage(imageRequest.getLinkImage());
        imageRepository.save(image);
        return ResponseEntity.ok().body(new MessageResponse("Image has been updated successfully!"));
    }
}