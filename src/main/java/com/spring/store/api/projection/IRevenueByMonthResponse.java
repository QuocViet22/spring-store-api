package com.spring.store.api.projection;

public interface IRevenueByMonthResponse {
    String getMonth();

    int getTotalPrice();

    String getProductId();

    int getAmountProduct();
}
