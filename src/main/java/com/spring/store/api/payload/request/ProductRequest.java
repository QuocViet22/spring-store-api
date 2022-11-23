package com.spring.store.api.payload.request;

import com.spring.store.api.models.Category;
import com.spring.store.api.models.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String price;
    private String amount;
    private String description;
    private Category category;
    private Set<Image> images;
    private Set<Long> sizes;
}
