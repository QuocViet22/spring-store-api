package com.spring.store.api.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductRequest {
    int price;
    String size;
}
