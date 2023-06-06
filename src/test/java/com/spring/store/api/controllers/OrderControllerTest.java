package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.LineItem;
import com.spring.store.api.models.ProductInfor;
import com.spring.store.api.models.WishList;
import com.spring.store.api.repository.LineItemRepository;
import com.spring.store.api.repository.ProductInforRepository;
import com.spring.store.api.repository.WishListRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    private ProductInforRepository productInforRepository;
}