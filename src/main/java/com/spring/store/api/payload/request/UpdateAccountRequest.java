package com.spring.store.api.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

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

    public String getSetModifiedBy() {
        return setModifiedBy;
    }

    public void setSetModifiedBy(String setModifiedBy) {
        this.setModifiedBy = setModifiedBy;
    }

    public String getGetSetModifiedDate() {
        return getSetModifiedDate;
    }

    public void setGetSetModifiedDate(String getSetModifiedDate) {
        this.getSetModifiedDate = getSetModifiedDate;
    }
}

