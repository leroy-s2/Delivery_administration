package com.silvacorporation_apps.deliveryadministration.fragments;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.adapters.MenuItemAdapter;
import com.silvacorporation_apps.deliveryadministration.dto.MenuItemDto;
import com.silvacorporation_apps.deliveryadministration.interfaces.MenuItemInterface;
import com.silvacorporation_apps.deliveryadministration.network.ApiClient;
import com.silvacorporation_apps.deliveryadministration.ui.AddMenuItemBottomSheet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailActivity extends AppCompatActivity {

    private Long restaurantId;
    private RecyclerView recyclerViewMenuItems;
    private FloatingActionButton fabAddMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        recyclerViewMenuItems = findViewById(R.id.recyclerViewMenuItems);
        fabAddMenuItem = findViewById(R.id.fabAddMenuItem);

        recyclerViewMenuItems.setLayoutManager(new LinearLayoutManager(this));

        // Recibir el ID del restaurante desde MainActivity
        restaurantId = getIntent().getLongExtra("restaurantId", -1);
        if (restaurantId == -1) {
            Toast.makeText(this, "Restaurante no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar los menús al iniciar
        loadMenuItems();

        // Abrir BottomSheet para crear menú
        fabAddMenuItem.setOnClickListener(v -> {
            AddMenuItemBottomSheet sheet = AddMenuItemBottomSheet.newInstance(restaurantId);
            sheet.show(getSupportFragmentManager(), "AddMenuItemBottomSheet");
        });
    }

    private void loadMenuItems() {
        MenuItemInterface api = ApiClient.getClient().create(MenuItemInterface.class);
        Call<List<MenuItemDto>> call = api.getMenuItemsByRestaurant(restaurantId);

        call.enqueue(new Callback<List<MenuItemDto>>() {
            @Override
            public void onResponse(Call<List<MenuItemDto>> call, Response<List<MenuItemDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MenuItemDto> menuItems = response.body();
                    recyclerViewMenuItems.setAdapter(new MenuItemAdapter(RestaurantDetailActivity.this, menuItems));
                } else {
                    Toast.makeText(RestaurantDetailActivity.this,
                            "Error al cargar menú", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MenuItemDto>> call, Throwable t) {
                Toast.makeText(RestaurantDetailActivity.this,
                        "Fallo conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
