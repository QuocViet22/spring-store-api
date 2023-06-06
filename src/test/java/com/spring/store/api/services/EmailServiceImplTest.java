package com.spring.store.api.services;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceImplTest {
    @Autowired
    private EmailService emailService;

    @Test
    void sendMailForgetPassword() {
        String userName = "viet";
        String email = "19110315@student.hcmute.edu.vn";
        String newPassword = "123456";
        emailService.sendMailForgetPassword(userName, email, newPassword);
    }

    @Test
    void createRandomNumber() {
        String test = emailService.createRandomNumber();
        System.out.println(test);
    }
}