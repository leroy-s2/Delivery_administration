package com.silvacorporation_apps.deliveryadministration.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private Long id;
    private String email;
    private String fullName;
    private String role;   // CUSTOMER, ADMIN o EMPLOYEE
    private String phone;
    private String address;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(Long id, String email, String fullName, String role, String phone, String address) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    @Override
    public String toString() {
        return "Id: " + id +
                ", Email: " + email +
                ", Nombre: " + fullName +
                ", Rol: " + role +
                ", Teléfono: " + phone +
                ", Dirección: " + address;
    }
}
