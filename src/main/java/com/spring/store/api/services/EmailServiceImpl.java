package com.spring.store.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender javaMailSender;

    @Override
    public String sendMail(String email, String number) {
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("E-Shop email");
        message.setText("Thank you for shopping in E-Shop!\n" +
                "Please keep this code in order to receive your shoes.\n" +
                "Your code is " + number);

        // Send Message!
        this.javaMailSender.send(message);
        return "Email Sent!";
    }

    @Override
    public String createRandomNumber() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
