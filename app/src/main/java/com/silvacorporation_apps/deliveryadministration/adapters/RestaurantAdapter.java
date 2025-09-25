package com.silvacorporation_apps.deliveryadministration.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.dto.RestaurantDto;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    public interface OnRestaurantClickListener {
        void onRestaurantClick(RestaurantDto restaurant);
    }

    private final Context context;
    private final List<RestaurantDto> restaurantList;
    private final OnRestaurantClickListener listener;

    public RestaurantAdapter(Context context, List<RestaurantDto> restaurantList, OnRestaurantClickListener listener) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantDto restaurant = restaurantList.get(position);

        holder.tvName.setText(restaurant.getName());
        holder.tvAddress.setText(restaurant.getAddress());
        holder.tvPhone.setText(
                restaurant.getPhone() != null && !restaurant.getPhone().isEmpty()
                        ? restaurant.getPhone()
                        : "Sin teléfono"
        );

        // Imagen
        if (restaurant.getImageUrl() != null && !restaurant.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(restaurant.getImageUrl())
                    .placeholder(R.drawable.ic_restaurant_placeholder)
                    .into(holder.ivRestaurant);
        } else {
            holder.ivRestaurant.setImageResource(R.drawable.ic_restaurant_placeholder);
        }

        // 🔹 Clic en el item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRestaurantClick(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList != null ? restaurantList.size() : 0;
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRestaurant;
        TextView tvName, tvAddress, tvPhone;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRestaurant = itemView.findViewById(R.id.ivRestaurant);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
