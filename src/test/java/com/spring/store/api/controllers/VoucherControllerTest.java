package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Voucher;
import com.spring.store.api.repository.VoucherRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@WebMvcTest(VoucherController.class)
class VoucherControllerTest {
    private static final String END_POINT_PATH = "/api/vouchers";

    @MockBean
    private VoucherRepository voucherRepository;

    @Test
    void getAllVouchers() {
        List<Voucher> voucherList = voucherRepository.findAll();
        Assert.assertEquals(4, voucherList.size());
    }

    @Test
    void getVoucherById() {
        long id = 8;
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Voucher with id = " + id));
        Assert.assertNotNull(voucher);
    }

    @Test
    void createVoucher() {
        Voucher voucher = new Voucher();
        voucher.setValue("test");
        voucher.setStatus("test");
        voucher.setQuantity("5");
        voucherRepository.save(voucher);

        // Verify that the account is created and has a non-null ID
        Assert.assertNotNull(voucher.getId());

        // Retrieve the account from the database and compare its properties
        Voucher retrievedVoucher = voucherRepository.findById(voucher.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Voucher with name test"));

        Assert.assertEquals(voucher.getName(), retrievedVoucher.getName());
        Assert.assertEquals(voucher.getQuantity(), retrievedVoucher.getQuantity());
    }

    @Test
    void updateVoucher() {
        long id = 1;
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Voucher!"));
        voucher.setQuantity("12");
        voucher.setValue("test");
        voucher.setStatus("test");
        voucher.setName("test");
        voucherRepository.save(voucher);

        // Retrieve the account from the database and compare its properties
        Voucher retrievedVoucher = voucherRepository.findById(voucher.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Voucher with name test"));

        Assert.assertEquals(voucher.getName(), retrievedVoucher.getName());
        Assert.assertEquals(voucher.getQuantity(), retrievedVoucher.getQuantity());
    }

    @Test
    void deleteVoucher() {
    }
}