package com.silvacorporation_apps.deliveryadministration.dto;

import java.io.Serializable;

public class AdminDto implements Serializable {

    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;

    // Constructor vacío
    public AdminDto() {
    }

    // Constructor con parámetros
    public AdminDto(String email, String password, String fullName, String phone, String address) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
