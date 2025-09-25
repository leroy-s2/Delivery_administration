package com.silvacorporation_apps.deliveryadministration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.silvacorporation_apps.deliveryadministration.activities.LoginActivity;
import com.silvacorporation_apps.deliveryadministration.model.Usuario;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewRestaurants;
    Button btnAddRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        btnAddRestaurant = findViewById(R.id.btnAddRestaurant);

        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(this));

        // Recibir usuario desde LoginActivity
        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (usuario != null) {
            Toast.makeText(this, "Bienvenido " + usuario.getFullName(), Toast.LENGTH_LONG).show();
        }

        btnAddRestaurant.setOnClickListener(v -> {
            Toast.makeText(this, "Funcionalidad de registrar restaurante en desarrollo", Toast.LENGTH_SHORT).show();

            // Ejemplo: volver al login como "logout"
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
