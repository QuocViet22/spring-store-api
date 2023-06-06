package com.spring.store.api.repository;

import com.spring.store.api.projection.IRevenueByDateResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RevenueRepositoryTest {

    @Autowired
    private RevenueRepository revenueRepository;
    @Test
    void viewRevenueByDate() {
        List<IRevenueByDateResponse> iRevenueByDateResponses;
        String date = "03/06/2023";
        iRevenueByDateResponses = revenueRepository.viewRevenueByDate(date);
        Assert.assertEquals(4, iRevenueByDateResponses.size());
    }
}