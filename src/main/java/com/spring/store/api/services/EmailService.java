package com.spring.store.api.services;

import com.spring.store.api.models.Order;

public interface EmailService {
//    String sendMail(String email, String number);

    String sendMailForgetPassword(String name, String email, String newPassword);

    String createRandomNumber();

    String sendMail(String name, String email, String address, String orderId, String createdDate, String estimatedDate);
}
