package com.silvacorporation_apps.deliveryadministration.interfaces;

import com.silvacorporation_apps.deliveryadministration.dto.RestaurantDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestaurantInterface {

    // 🔹 Listar restaurantes de un usuario
    @GET("restaurants/user/{userId}")
    Call<List<RestaurantDto>> getRestaurantsByUser(@Path("userId") Long userId);

    // 🔹 Crear restaurante para un usuario
    @POST("restaurants/user/{userId}")
    Call<RestaurantDto> createRestaurant(
            @Path("userId") Long userId,
            @Body RestaurantDto dto
    );

}
