package com.silvacorporation_apps.deliveryadministration.interfaces;

import com.silvacorporation_apps.deliveryadministration.dto.OrderItemDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderItemInterface {

    @GET("order-items/order/{orderId}")
    Call<List<OrderItemDto>> getOrderItemsByOrder(@Path("orderId") Long orderId);

    @GET("order-items/menu/{menuItemId}")
    Call<List<OrderItemDto>> getOrderItemsByMenu(@Path("menuItemId") Long menuItemId);

}
