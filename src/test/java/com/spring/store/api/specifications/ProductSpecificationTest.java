package com.spring.store.api.specifications;

import com.spring.store.api.models.Product;
import com.spring.store.api.models.QueryOperator;
import com.spring.store.api.payload.request.FilterRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProductSpecificationTest {
    @Autowired
    private ProductSpecification productSpecification;

    @Test
    void testDynamicSpecification() {
        FilterRequest price = new FilterRequest();
        price.setField("price");
        price.setOperator(QueryOperator.LIKE);
        price.setValue("249");

        FilterRequest name = new FilterRequest();
        name.setField("name");
        name.setOperator(QueryOperator.LIKE);
        name.setValue("Adidas XPLR White");

        FilterRequest category = new FilterRequest();
        name.setField("category_id");
        name.setOperator(QueryOperator.LIKE);
        name.setValue("1");

        List<FilterRequest> filters = new ArrayList<>();
//        filters.add(price);
//        filters.add(name);
//        filters.add(category);
        List<Product> products = productSpecification.getQueryResult(filters);
        Assert.assertEquals(1, products.size());
    }
}