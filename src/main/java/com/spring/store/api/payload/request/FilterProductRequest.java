package com.spring.store.api.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductRequest {
    String size;
    String price;
}
