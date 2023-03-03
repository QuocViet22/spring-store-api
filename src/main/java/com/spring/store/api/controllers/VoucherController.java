package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Voucher;
import com.spring.store.api.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class VoucherController {
    @Autowired
    VoucherRepository voucherRepository;

    //    get all vouchers
    @GetMapping("/vouchers")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    //    get voucher by id
    @GetMapping("/vouchers/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable("id") long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Voucher with id = " + id));

        return new ResponseEntity<>(voucher, HttpStatus.OK);
    }

//    //    get voucher by name
//    @GetMapping("/vouchers/{id}")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
//    public ResponseEntity<Voucher> getVoucherByName(@PathVariable("id") long id) {
//        Voucher voucher = voucherRepository.findByName(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Voucher with id = " + id));
//
//        return new ResponseEntity<>(voucher, HttpStatus.OK);
//    }
}
