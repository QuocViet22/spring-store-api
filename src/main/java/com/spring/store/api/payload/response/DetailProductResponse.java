package com.spring.store.api.payload.response;

import com.spring.store.api.models.Product;
import com.spring.store.api.projection.IRecommendProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailProductResponse {
    private Product product;
    private List<IRecommendProduct> iRecommendProducts;
}
