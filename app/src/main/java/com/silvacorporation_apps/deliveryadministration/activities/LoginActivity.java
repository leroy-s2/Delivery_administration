package com.silvacorporation_apps.deliveryadministration.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.silvacorporation_apps.deliveryadministration.MainActivity;
import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.dto.AdminDto;
import com.silvacorporation_apps.deliveryadministration.interfaces.CRUDInterface;
import com.silvacorporation_apps.deliveryadministration.model.AuthResponse;
import com.silvacorporation_apps.deliveryadministration.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    MaterialButton buttonLogin;
    TextView textViewRegister;
    CRUDInterface crudInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminDto dto = new AdminDto(
                        editTextEmail.getText().toString().trim(),
                        editTextPassword.getText().toString().trim(),
                        null, null, null,
                        null // 👈 role no necesario en login
                );

                login(dto);
            }
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(AdminDto dto) {
        crudInterface = ApiClient.getClient().create(CRUDInterface.class);

        Call<AuthResponse> call = crudInterface.login(dto);
        Log.d("LOGIN", "Enviando login con email: " + dto.getEmail());

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("LOGIN", "Código HTTP: " + response.code());

                if (!response.isSuccessful()) {
                    Log.e("LOGIN", "Error response: " + response.message());
                    Toast.makeText(getApplicationContext(), "Credenciales inválidas", Toast.LENGTH_LONG).show();
                    return;
                }

                AuthResponse authResponse = response.body();
                if (authResponse != null && authResponse.getUser() != null) {
                    Log.d("LOGIN", "Respuesta: " + authResponse.getMessage() + " - Usuario: " + authResponse.getUser());

                    if ("ADMIN".equals(authResponse.getUser().getRole())) {
                        Toast.makeText(getApplicationContext(),
                                "Bienvenido Admin " + authResponse.getUser().getFullName(),
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("usuario", authResponse.getUser());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Acceso denegado. Solo admins pueden ingresar.",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("LOGIN", "Respuesta nula o sin usuario");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("LOGIN", "Fallo en la llamada", t);
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
