package com.spring.store.api.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

public class UpdateAccountRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String password;
    private String status;
    private Set<String> role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}

