package com.silvacorporation_apps.deliveryadministration.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.dto.AdminDto;
import com.silvacorporation_apps.deliveryadministration.interfaces.CRUDInterface;
import com.silvacorporation_apps.deliveryadministration.model.AuthResponse;
import com.silvacorporation_apps.deliveryadministration.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextFullName, editTextEmail, editTextPassword, editTextPhone, editTextAddress;
    MaterialButton buttonRegister;
    CRUDInterface crudInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminDto dto = new AdminDto(
                        editTextEmail.getText().toString().trim(),
                        editTextPassword.getText().toString().trim(),
                        editTextFullName.getText().toString().trim(),
                        editTextPhone.getText().toString().trim(),
                        editTextAddress.getText().toString().trim(),
                        "ADMIN" // 👈 ahora lo mandamos explícito
                );

                register(dto);
            }
        });
    }

    private void register(AdminDto dto) {
        crudInterface = ApiClient.getClient().create(CRUDInterface.class);

        Call<AuthResponse> call = crudInterface.register(dto);
        Log.d("REGISTER", "Enviando registro con email: " + dto.getEmail());

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("REGISTER", "Código HTTP: " + response.code());

                if (!response.isSuccessful()) {
                    Log.e("REGISTER", "Error response: " + response.message());
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                AuthResponse authResponse = response.body();
                if (authResponse != null) {
                    Log.d("REGISTER", "Respuesta: " + authResponse.getMessage() + " - Usuario: " + authResponse.getUser());
                    Toast.makeText(getApplicationContext(),
                            authResponse.getMessage() + " (" + authResponse.getUser().getFullName() + ")",
                            Toast.LENGTH_LONG).show();

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Log.e("REGISTER", "Respuesta nula del servidor");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("REGISTER", "Fallo en la llamada", t);
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
