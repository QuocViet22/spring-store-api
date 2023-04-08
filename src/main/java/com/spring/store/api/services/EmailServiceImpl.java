package com.spring.store.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String sendMailForgetPassword(String name, String email, String number) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setFrom("tlcn092022@gmail.com");
                messageHelper.setTo(email);
                messageHelper.setSubject("E-Shop email");
                Context context = new Context();
                context.setVariable("name", name);
                context.setVariable("number", number);
                String content = templateEngine.process("reset_password", context);
                messageHelper.setText(content, true);
            }
        };
        this.javaMailSender.send(preparator);
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

    @Override
    public String sendMail(String name, String email, String address, String orderId, String createdDate) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setFrom("tlcn092022@gmail.com");
                messageHelper.setTo(email);
                messageHelper.setSubject("E-Shop email");
                Context context = new Context();
                context.setVariable("address", address);
                context.setVariable("name", name);
                String link = "https://shoe-store-van-viet.vercel.app/order-detail/" + orderId;
                context.setVariable("link", link);
                context.setVariable("createdDate", createdDate);
                String content = templateEngine.process("order_email", context);
                messageHelper.setText(content, true);
            }
        };
        this.javaMailSender.send(preparator);
        return "Email Sent!";
    }
}
