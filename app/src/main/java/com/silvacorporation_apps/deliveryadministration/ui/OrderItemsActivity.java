package com.silvacorporation_apps.deliveryadministration.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.adapters.OrderItemAdapter;
import com.silvacorporation_apps.deliveryadministration.dto.OrderItemDto;
import com.silvacorporation_apps.deliveryadministration.interfaces.OrderItemInterface;
import com.silvacorporation_apps.deliveryadministration.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        Toolbar toolbar = findViewById(R.id.toolbarOrderItems);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));

        Long orderId = getIntent().getLongExtra("orderId", -1L);
        Long menuItemId = getIntent().getLongExtra("menuItemId", -1L);

        if (orderId != -1L) {
            loadOrderItemsByOrder(orderId);
        } else if (menuItemId != -1L) {
            loadOrderItemsByMenu(menuItemId);
        } else {
            Toast.makeText(this, "Parámetros inválidos", Toast.LENGTH_LONG).show();
        }
    }

    private void loadOrderItemsByOrder(Long orderId) {
        OrderItemInterface api = ApiClient.getClient().create(OrderItemInterface.class);
        api.getOrderItemsByOrder(orderId).enqueue(new Callback<List<OrderItemDto>>() {
            @Override
            public void onResponse(Call<List<OrderItemDto>> call, Response<List<OrderItemDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerViewOrderItems.setAdapter(
                            new OrderItemAdapter(OrderItemsActivity.this, response.body())
                    );
                } else {
                    Toast.makeText(OrderItemsActivity.this,
                            "Error cargando items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItemDto>> call, Throwable t) {
                Toast.makeText(OrderItemsActivity.this,
                        "Fallo conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadOrderItemsByMenu(Long menuItemId) {
        OrderItemInterface api = ApiClient.getClient().create(OrderItemInterface.class);
        api.getOrderItemsByMenu(menuItemId).enqueue(new Callback<List<OrderItemDto>>() {
            @Override
            public void onResponse(Call<List<OrderItemDto>> call, Response<List<OrderItemDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerViewOrderItems.setAdapter(
                            new OrderItemAdapter(OrderItemsActivity.this, response.body())
                    );
                } else {
                    Toast.makeText(OrderItemsActivity.this,
                            "Error cargando items por menú", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItemDto>> call, Throwable t) {
                Toast.makeText(OrderItemsActivity.this,
                        "Fallo conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
