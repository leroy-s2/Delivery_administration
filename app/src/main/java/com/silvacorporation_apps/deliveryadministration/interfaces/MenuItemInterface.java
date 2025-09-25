package com.silvacorporation_apps.deliveryadministration.interfaces;

import com.silvacorporation_apps.deliveryadministration.dto.MenuItemDto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MenuItemInterface {

    // Traer menús de un restaurante
    @GET("/api/v1/menu-items/restaurant/{restaurantId}")
    Call<List<MenuItemDto>> getMenuItemsByRestaurant(@Path("restaurantId") Long restaurantId);

    // Crear menú sin imagen
    @POST("/api/v1/menu-items")
    Call<MenuItemDto> createMenuItem(@Body MenuItemDto menuItemDto);

    // Crear menú con imagen (Multipart para Azure)
    @Multipart
    @POST("/api/v1/menu-items/upload")
    Call<MenuItemDto> createMenuItemWithImage(
            @Part("menuItem") RequestBody menuItemJson,
            @Part MultipartBody.Part image
    );

}
