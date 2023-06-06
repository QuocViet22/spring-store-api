package com.spring.store.api.repository;

import com.spring.store.api.models.Product;
import com.spring.store.api.projection.IRecommendProduct;
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

    @Test
    void recommendProducts() {
        List<IRecommendProduct> iRecommendProducts;
        long no1 = 10;
        long no2 = 2;
        long no3 = 3;
        long no4 = 4;
        long no5 = 5;
        iRecommendProducts = productRepository.recommendProducts(no1, no2, no3, no4, no5);
        Assert.assertEquals(5, iRecommendProducts.size());
    }
}