package com.silvacorporation_apps.deliveryadministration.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import com.silvacorporation_apps.deliveryadministration.dto.UserDto;
import com.silvacorporation_apps.deliveryadministration.interfaces.LoginInterface;
import com.silvacorporation_apps.deliveryadministration.utils.Constants;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity implements LoginInterface {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            login(email, password);
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void login(String email, String password) {
        new Thread(() -> {
            try {
                URL url = new URL(Constants.BASE_URL + "/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject body = new JSONObject();
                body.put("email", email);
                body.put("password", password);

                OutputStream os = conn.getOutputStream();
                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    String result = new java.util.Scanner(conn.getInputStream()).useDelimiter("\\A").next();
                    JSONObject json = new JSONObject(result);

                    JSONObject userJson = json.getJSONObject("user");
                    String role = userJson.getString("role");
                    if ("ADMIN".equals(role)) {
                        UserDto user = new UserDto();
                        user.setId(userJson.getInt("id"));
                        user.setEmail(userJson.getString("email"));
                        user.setFullName(userJson.getString("fullName"));
                        user.setRole(role);
                        user.setToken(json.getString("token"));

                        runOnUiThread(() -> onLoginSuccess(user));
                    } else {
                        runOnUiThread(() -> onLoginError("Solo usuarios ADMIN pueden acceder"));
                    }
                } else {
                    runOnUiThread(() -> onLoginError("Credenciales inválidas"));
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e("LoginActivity", "Error en login", e);
                runOnUiThread(() -> onLoginError("Error de conexión"));
            }
        }).start();
    }

    @Override
    public void onLoginSuccess(UserDto user) {
        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", user.getFullName());
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}