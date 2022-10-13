package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.User;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.UserRepository;
import com.spring.store.api.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    //    get User by Account_id
    @GetMapping({"/users/{id}"})
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with Account_id = " + id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //    update User for User only
    @PutMapping({"/users/{id}"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id,
                                           @RequestBody User userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id user " + id + " not found"));

        user.setAddress(userRequest.getAddress());
        user.setAge(userRequest.getAge());
        user.setEmail(userRequest.getEmail());
        user.setGender(userRequest.getGender());
        user.setName(userRequest.getName());
        user.setPhone(userRequest.getPhone());
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

    //    update User for Moderator
    @PutMapping({"/users/{id}/mod"})
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUserByMod(@PathVariable("id") long id,
                                                @RequestBody User userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id user " + id + " not found"));

        user.setAddress(userRequest.getAddress());
        user.setAge(userRequest.getAge());
        user.setEmail(userRequest.getEmail());
        user.setGender(userRequest.getGender());
        user.setName(userRequest.getName());
        user.setPhone(userRequest.getPhone());
        user.setStatus(userRequest.getStatus());
        user.setType(userRequest.getType());
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

    //    delete User by id
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<HttpStatus> deleteDetails(@PathVariable("id") long id) {
//        userRepository.deleteById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    //    delete User of An Account
//    @DeleteMapping("/users/{userId}/account")
//    public ResponseEntity<User> deleteDetailsOfTutorial(@PathVariable(value = "userId") Long accountId) {
//        if (!accountRepository.existsById(accountId)) {
//            throw new ResourceNotFoundException("Not found User with id = " + accountId);
//        }
//
//        userRepository.deleteByAccountId(accountId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
