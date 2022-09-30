package com.spring.store.api.controllers;


import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Account;
import com.spring.store.api.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.spring.store.api.repository.AccountRepository;
import com.spring.store.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder encoder;

    //    get all accounts
    @GetMapping("/accounts")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/accounts/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Account with id = " + id));

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateAccount(@PathVariable("id") long id, @RequestBody Account accountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Account with id = " + id));

        account.setPassword(encoder.encode(accountRequest.getPassword()));
        accountRepository.save(account);
        return ResponseEntity.ok().body(new MessageResponse("Account has been updated successfully!"));
    }

    @PutMapping("/accounts/{id}/mod")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateAccountByMod(@PathVariable("id") long id, @RequestBody Account accountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Account with id = " + id));

        account.setPassword(encoder.encode(accountRequest.getPassword()));
        account.setStatus(accountRequest.getStatus());
        account.setRoles(accountRequest.getRoles());
        accountRepository.save(account);
        return ResponseEntity.ok().body(new MessageResponse("Account has been updated successfully!"));
    }
}
