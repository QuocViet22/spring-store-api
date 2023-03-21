package com.spring.store.api.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ForgetPasswordRequest {
    @NotBlank
    private String userName;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;
}
