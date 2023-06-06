package com.spring.store.api.repository;

import com.spring.store.api.models.Product;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByCategory() {

    }

    @Test
    void findByCategoryId() {
        long id = 1;
        List<Product> productList = productRepository.findByCategoryId(id);
        Assert.assertEquals(5, productList.size());
    }

    @Test
    void findProductsByName() {
        String name = "nike";
        List<Product> products = productRepository.findProductsByName(name);
        Assert.assertEquals(4, products.size());
    }

    @Test
    void existsByName() {
        String name = "Nike Jordan 3";
        // String name = "Nike";
        Assert.assertTrue(productRepository.existsByName(name));
    }

    @Test
    void findProductBySize() {
        String size = "3.5";
        List<Product> products;
        products = productRepository.findProductBySize(size);
        Assert.assertEquals(5, products.size());
    }

    @Test
    void findProductByPrice() {
        int price = 249;
        List<Product> products;
        products = productRepository.findProductByPrice(price);
        Assert.assertEquals(4, products.size());
    }

    @Test
    void findProductByPriceAndSize() {
        int price = 246;
        String size = "3.5";
        List<Product> products;
        products = productRepository.findProductByPriceAndSize(price, size);
        Assert.assertEquals(2, products.size());
    }
}