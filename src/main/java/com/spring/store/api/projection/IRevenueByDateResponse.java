package com.spring.store.api.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IRevenueByDateResponse {
    String getModifiedDate();

    int getTotalPrice();

    String getStatus();

    String getProductId();

    int getProductAmount();

}
