package com.spring.store.api.services;

public interface EmailService {
    String sendMail(String email, String number);

    String createRandomNumber();
}
