package com.spring.store.api.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RevenuePerMonthRequest {
    @NotBlank
    private int year;
}
