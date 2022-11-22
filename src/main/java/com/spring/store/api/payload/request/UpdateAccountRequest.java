package com.spring.store.api.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import javax.validation.constraints.*;

@Getter
@Setter
public class UpdateAccountRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String currentPassword;
    private String newPassword;
    private String password;
    private String status;
    private Set<String> role;
    private String setModifiedBy;
    private String getSetModifiedDate;
}

