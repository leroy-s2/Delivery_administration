package com.silvacorporation_apps.deliveryadministration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.silvacorporation_apps.deliveryadministration.activities.LoginActivity;
import com.silvacorporation_apps.deliveryadministration.adapters.RestaurantAdapter;
import com.silvacorporation_apps.deliveryadministration.dto.RestaurantDto;
import com.silvacorporation_apps.deliveryadministration.fragments.RestaurantDetailActivity;
import com.silvacorporation_apps.deliveryadministration.interfaces.RestaurantInterface;
import com.silvacorporation_apps.deliveryadministration.model.Usuario;
import com.silvacorporation_apps.deliveryadministration.network.ApiClient;
import com.silvacorporation_apps.deliveryadministration.ui.AddRestaurantBottomSheet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewRestaurants;
    Button btnAddRestaurant;
    Usuario usuario; // Usuario logueado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onToolbarMenuItemClick);

        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        btnAddRestaurant = findViewById(R.id.btnAddRestaurant);
        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(this));

        // Usuario recibido desde LoginActivity
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario != null) {
            Toast.makeText(this, "Bienvenido " + usuario.getFullName(), Toast.LENGTH_LONG).show();
        }

        // Acción para agregar restaurante
        btnAddRestaurant.setOnClickListener(v -> {
            Long userId = (usuario != null) ? usuario.getId() : null;

            AddRestaurantBottomSheet sheet = AddRestaurantBottomSheet
                    .newInstance(userId, dto -> {
                        RestaurantInterface api = ApiClient.getClient().create(RestaurantInterface.class);
                        Call<RestaurantDto> call = api.createRestaurant(userId, dto);

                        call.enqueue(new Callback<RestaurantDto>() {
                            @Override
                            public void onResponse(Call<RestaurantDto> call, Response<RestaurantDto> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(MainActivity.this,
                                            "Restaurante creado: " + response.body().getName(),
                                            Toast.LENGTH_LONG).show();
                                    loadRestaurants(); // refrescar lista
                                } else {
                                    Toast.makeText(MainActivity.this,
                                            "Error al crear: " + response.message(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RestaurantDto> call, Throwable t) {
                                Toast.makeText(MainActivity.this,
                                        "Fallo conexión: " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    });

            sheet.show(getSupportFragmentManager(), "AddRestaurantBottomSheet");
        });

        // Cargar restaurantes
        loadRestaurants();
    }

    // Listener menú del Toolbar
    private boolean onToolbarMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            showProfilePopup();
            return true;
        }
        return false;
    }

    // Popup perfil con logout
    private void showProfilePopup() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_profile, null);

        TextView tvUsername = popupView.findViewById(R.id.tvUsername);
        Button btnLogout = popupView.findViewById(R.id.btnLogout);

        if (usuario != null) {
            tvUsername.setText(usuario.getFullName());
        }

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        View root = findViewById(android.R.id.content);
        popupWindow.showAtLocation(root, Gravity.TOP | Gravity.END, dp(16), dp(56));

        btnLogout.setOnClickListener(v -> {
            popupWindow.dismiss();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }

    // 🚀 Traer restaurantes del backend
    private void loadRestaurants() {
        if (usuario == null) {
            Toast.makeText(this, "Usuario no válido", Toast.LENGTH_LONG).show();
            return;
        }

        RestaurantInterface api = ApiClient.getClient().create(RestaurantInterface.class);
        Call<List<RestaurantDto>> call = api.getRestaurantsByUser(usuario.getId());

        call.enqueue(new Callback<List<RestaurantDto>>() {
            @Override
            public void onResponse(Call<List<RestaurantDto>> call, Response<List<RestaurantDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RestaurantDto> restaurants = response.body();
                    recyclerViewRestaurants.setAdapter(
                            new RestaurantAdapter(MainActivity.this, restaurants, restaurant -> {
                                // 🚀 Abrir detalle del restaurante
                                Intent intent = new Intent(MainActivity.this, RestaurantDetailActivity.class);
                                intent.putExtra("restaurantId", restaurant.getId());
                                startActivity(intent);
                            })
                    );
                } else {
                    Toast.makeText(MainActivity.this,
                            "Error al cargar restaurantes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<RestaurantDto>> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Fallo conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
