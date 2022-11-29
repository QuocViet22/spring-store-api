package com.spring.store.api.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RevenueByDateRequest {
    @NotBlank
    private String modifiedDate;
}
