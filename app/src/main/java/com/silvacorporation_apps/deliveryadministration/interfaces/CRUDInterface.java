package com.silvacorporation_apps.deliveryadministration.interfaces;

import com.silvacorporation_apps.deliveryadministration.dto.AdminDto;
import com.silvacorporation_apps.deliveryadministration.dto.RestaurantDto;
import com.silvacorporation_apps.deliveryadministration.model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CRUDInterface {

    @POST("auth/register")
    Call<AuthResponse> register(@Body AdminDto dto);

    @POST("auth/login")
    Call<AuthResponse> login(@Body AdminDto dto);
    @POST("restaurants")
    Call<RestaurantDto> createRestaurant(@Body RestaurantDto dto);
}
