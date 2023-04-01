package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Voucher;
import com.spring.store.api.payload.request.VoucherRequest;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //    get all products pageable
    @GetMapping("/vouchers/pageable")
    public Page<Voucher> getAllVouchersPageable(@RequestParam int page,
                                                @RequestParam int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("id"));
        return voucherRepository.findAll(paging);
    }

    //    get voucher by id
    @GetMapping("/vouchers/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable("id") long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Voucher with id = " + id));

        return new ResponseEntity<>(voucher, HttpStatus.OK);
    }

    //    get voucher by name
    @PostMapping("/vouchers/search")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Voucher> getVoucherByName(@RequestBody VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findByName(voucherRequest.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Not found voucher: " + voucherRequest.getName()));
        return new ResponseEntity<>(voucher, HttpStatus.OK);
    }

    //     create voucher
    @PostMapping("/voucher")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucherRequest) {
        Voucher voucher = new Voucher();
        voucher.setName(voucherRequest.getName());
        voucher.setQuantity(voucherRequest.getQuantity());
        voucher.setValue(voucherRequest.getValue());
        voucher.setStatus(voucherRequest.getStatus());
        voucherRepository.save(voucher);
        return new ResponseEntity<>(voucher, HttpStatus.CREATED);
    }

    //    update voucher by id
    @PutMapping("/voucher/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateVoucher(@PathVariable("id") long id, @RequestBody Voucher voucherRequest) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher Id " + id + "not found"));
        voucher.setName(voucherRequest.getName());
        voucher.setQuantity(voucherRequest.getQuantity());
        voucher.setValue(voucherRequest.getValue());
        voucher.setStatus(voucherRequest.getStatus());
        voucherRepository.save(voucher);
        return ResponseEntity.ok().body(new MessageResponse("Voucher has been updated successfully!"));
    }

    //    delete voucher by id
    @DeleteMapping("/voucher/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") long id) {
        voucherRepository.deleteById(id);
        return ResponseEntity.ok().body(new MessageResponse("Voucher has been deleted successfully!"));
    }
}
