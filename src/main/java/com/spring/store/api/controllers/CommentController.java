package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Comment;
import com.spring.store.api.models.Image;
import com.spring.store.api.models.User;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.CommentRepository;
import com.spring.store.api.repository.ProductRepository;
import com.spring.store.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    //    retrieve all Comments of a Product
    @GetMapping("/product/{productId}/comments")
    //    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Comment>> getAllCommentsByProductId(@PathVariable(value = "productId") Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Not found Product with id = " + productId);
        }
        List<Comment> comments = commentRepository.findAllByProductId(productId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    //    retrieve a Comment by comment_id
    @GetMapping("/comments/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Comment> getCommentByProductId(@PathVariable(value = "id") Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    //     create comment of a product
    @PostMapping("/product/{productId}/user/{userId}/comments")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Comment> createComment(@PathVariable(value = "productId") Long productId,
                                                 @PathVariable(value = "userId") Long userId,
                                                 @RequestBody Comment commentRequest) {
        Comment comment = productRepository.findById(productId).map(product -> {
            commentRequest.setProduct(product);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
            commentRequest.setUser(user);
            commentRequest.setContent(commentRequest.getContent());
            return commentRepository.save(commentRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + productId));

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    //    update a comment by comment_id
    @PutMapping("/comment/{commentId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") long commentId, @RequestBody Comment commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + commentId + "not found"));
        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
        return ResponseEntity.ok().body(new MessageResponse("Comment has been updated successfully!"));
    }

    //    delete Comment by id
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") long commentId) {
        commentRepository.deleteById(commentId);
        return ResponseEntity.ok().body(new MessageResponse("Comment has been deleted successfully!"));
    }
}
