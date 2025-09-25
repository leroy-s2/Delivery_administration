package com.silvacorporation_apps.deliveryadministration.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private String message;
    private Usuario user;

    public AuthResponse() {
    }

    public AuthResponse(String message, Usuario user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}
