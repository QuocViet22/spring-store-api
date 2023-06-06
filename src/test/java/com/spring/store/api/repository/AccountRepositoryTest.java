package com.spring.store.api.repository;

import com.spring.store.api.models.Account;
import com.spring.store.api.projection.IForgetPassword;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findByUsername() {
        String userName = "quocviet";
        Account account = accountRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Account Not Found with username: " + userName));
        Assert.assertEquals("quocviet", account.getUsername());
    }

    @Test
    void existsByUsername() {
        String userName = "khanhvan";
        Assert.assertTrue(accountRepository.existsByUsername(userName));
    }

    @Test
    void forgetPassword() {
        String userName = "quocviet";
        String phone = "0344976004";
        String email = "19110315@student.hcmute.edu.vn";
        IForgetPassword iForgetPassword;
        iForgetPassword = accountRepository.forgetPassword(userName, phone, email);
        Assert.assertEquals("quocviet1", iForgetPassword.getUserName());
    }
}