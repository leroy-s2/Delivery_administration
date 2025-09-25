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
import com.silvacorporation_apps.deliveryadministration.model.Usuario;
import com.silvacorporation_apps.deliveryadministration.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                        editTextAddress.getText().toString().trim()
                );
                register(dto);
            }
        });
    }

    private void register(AdminDto dto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        crudInterface = retrofit.create(CRUDInterface.class);

        Call<AuthResponse> call = crudInterface.register(dto);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                AuthResponse authResponse = response.body();
                if (authResponse != null) {
                    Toast.makeText(getApplicationContext(),
                            authResponse.getMessage() + " (" + authResponse.getUser().getFullName() + ")",
                            Toast.LENGTH_LONG).show();

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
