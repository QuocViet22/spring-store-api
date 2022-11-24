package com.spring.store.api.payload.response;

import com.spring.store.api.models.Image;
import com.spring.store.api.models.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProductInforResponse {
    private Long id;
    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private String modifiedDate;
    private String status;
    private String name;
    private String price;
    private String amount;
    private String description;
    private Set<Image> images;
    private Set<Size> sizes;

    public ProductInforResponse() {
    }
}
