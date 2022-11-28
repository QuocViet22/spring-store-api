package com.spring.store.api.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IRevenueByDateResponse {
    String getCreatedDate();

    int getTotalPrice();

    String getStatus();

    String getProductId();

    int getProductAmount();

}
