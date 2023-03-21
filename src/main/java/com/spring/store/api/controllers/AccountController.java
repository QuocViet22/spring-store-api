package com.spring.store.api.controllers;


import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Account;
import com.spring.store.api.models.ERole;
import com.spring.store.api.models.Role;
import com.spring.store.api.payload.request.ForgetPasswordRequest;
import com.spring.store.api.payload.request.UpdateAccountRequest;
import com.spring.store.api.payload.response.MessageResponse;

import com.spring.store.api.projection.IForgetPassword;
import com.spring.store.api.repository.RoleRepository;
import com.spring.store.api.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.spring.store.api.repository.AccountRepository;
import com.spring.store.api.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    //    get all accounts
    @GetMapping("/accounts")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    //    get account by id
    @GetMapping("/accounts/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Account with id = " + id));

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    //    update account for user
    @PutMapping("/accounts/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateAccount(@PathVariable("id") long id, @RequestBody UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Account with id = " + id));
        if (encoder.matches(updateAccountRequest.getCurrentPassword(), account.getPassword())) {
            account.setPassword(encoder.encode(updateAccountRequest.getNewPassword()));
            accountRepository.save(account);
            return ResponseEntity.ok().body(new MessageResponse("Account has been updated successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Password is incorrect!"));
    }

    //    update account for mod
    @PutMapping("/accounts/{id}/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAccountByMod(@PathVariable("id") long id,
                                                @RequestBody UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Account with id = " + id));

//        Update password
        String newPassword = encoder.encode(updateAccountRequest.getPassword());
        if (newPassword != account.getPassword()) {
            account.setPassword(newPassword);
        } else {
            account.setPassword(account.getPassword());
        }
        account.setStatus(updateAccountRequest.getStatus());
        account.setModifiedBy(updateAccountRequest.getSetModifiedBy());
        account.setModifiedDate(updateAccountRequest.getGetSetModifiedDate());
//        account.setRoles(updateAccountRequest.getRole());
        // update Role for user account
        Set<String> strRoles = updateAccountRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        account.setRoles(roles);
        accountRepository.save(account);
        return ResponseEntity.ok().body(new MessageResponse("Account has been updated successfully!"));
    }

    //    forget password
    @PutMapping("/accounts/forgetPasword")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) {
        String userName = forgetPasswordRequest.getUserName();
        String phone = forgetPasswordRequest.getPhone();
        String email = forgetPasswordRequest.getEmail();
        IForgetPassword iForgetPassword = accountRepository.forgetPassword(userName, phone, email);
        if (iForgetPassword==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Account not found. Please check again!"));
        }
        Account account = accountRepository.findByUsername(iForgetPassword.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("Account Not Found with username: " + iForgetPassword.getUserName()));
        //  Update password
        String newPassword = emailService.createRandomNumber();
        account.setPassword(encoder.encode(newPassword));
        accountRepository.save(account);
        //  Send mail
        emailService.sendMail(email, newPassword);
        return ResponseEntity.ok().body(new MessageResponse("New password has been sent to your email!"));
    }
}

