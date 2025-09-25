package com.silvacorporation_apps.deliveryadministration.interfaces;

import com.silvacorporation_apps.deliveryadministration.dto.UserDto;

public interface LoginInterface {
    void onLoginSuccess(UserDto user);
    void onLoginError(String error);
}