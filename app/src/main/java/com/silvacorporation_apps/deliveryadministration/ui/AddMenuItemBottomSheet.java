package com.silvacorporation_apps.deliveryadministration.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.dto.MenuItemDto;
import com.silvacorporation_apps.deliveryadministration.interfaces.MenuItemInterface;
import com.silvacorporation_apps.deliveryadministration.network.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMenuItemBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_RESTAURANT_ID = "restaurant_id";

    private Long restaurantId;
    private Uri imageUri;
    private ImageView ivPreview;

    public static AddMenuItemBottomSheet newInstance(Long restaurantId) {
        AddMenuItemBottomSheet sheet = new AddMenuItemBottomSheet();
        Bundle args = new Bundle();
        args.putLong(ARG_RESTAURANT_ID, restaurantId);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet_add_menu_item, container, false);

        TextInputEditText etName = v.findViewById(R.id.etName);
        TextInputEditText etDescription = v.findViewById(R.id.etDescription);
        TextInputEditText etCategory = v.findViewById(R.id.etCategory);
        TextInputEditText etPrice = v.findViewById(R.id.etPrice);
        MaterialCheckBox cbAvailable = v.findViewById(R.id.cbAvailable);
        MaterialButton btnSelectImage = v.findViewById(R.id.btnSelectImage);
        ivPreview = v.findViewById(R.id.ivPreview);
        MaterialButton btnSave = v.findViewById(R.id.btnSaveMenuItem);
        MaterialButton btnCancel = v.findViewById(R.id.btnCancel);

        if (getArguments() != null) {
            restaurantId = getArguments().getLong(ARG_RESTAURANT_ID);
        }

        // Seleccionar imagen
        ActivityResultLauncher<Intent> imagePickerLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                                imageUri = result.getData().getData();
                                try (InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri)) {
                                    ivPreview.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

        btnSelectImage.setOnClickListener(v1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Guardar menú
        btnSave.setOnClickListener(view -> {
            String name = safe(etName);
            String description = safe(etDescription);
            String category = safe(etCategory);
            String priceStr = safe(etPrice);
            boolean available = cbAvailable.isChecked();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
                Toast.makeText(getContext(), "Nombre y precio son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            // Construir JSON
            MenuItemDto dto = new MenuItemDto();
            dto.setName(name);
            dto.setDescription(description);
            dto.setCategory(category);
            dto.setPrice(price);
            dto.setAvailable(available);
            dto.setRestaurantId(restaurantId);

            MenuItemInterface api = ApiClient.getClient().create(MenuItemInterface.class);

            if (imageUri != null) {
                try {
                    // Abrir la imagen con ContentResolver
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int nRead;
                    while ((nRead = Objects.requireNonNull(inputStream).read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    byte[] imageBytes = buffer.toByteArray();

                    RequestBody requestFile = RequestBody.create(
                            imageBytes,
                            MediaType.parse(requireContext().getContentResolver().getType(imageUri))
                    );

                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                            "image",
                            "menu_item.jpg",
                            requestFile
                    );

                    RequestBody menuItemJson = RequestBody.create(
                            ApiClient.getGson().toJson(dto),
                            MediaType.parse("application/json")
                    );

                    Call<MenuItemDto> call = api.createMenuItemWithImage(menuItemJson, imagePart);
                    call.enqueue(new Callback<MenuItemDto>() {
                        @Override
                        public void onResponse(Call<MenuItemDto> call, Response<MenuItemDto> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(getContext(), "Plato creado: " + response.body().getName(), Toast.LENGTH_LONG).show();
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), "Error al crear: " + response.message(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MenuItemDto> call, Throwable t) {
                            Toast.makeText(getContext(), "Fallo conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al procesar imagen", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Sin imagen
                Call<MenuItemDto> call = api.createMenuItem(dto);
                call.enqueue(new Callback<MenuItemDto>() {
                    @Override
                    public void onResponse(Call<MenuItemDto> call, Response<MenuItemDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "Plato creado: " + response.body().getName(), Toast.LENGTH_LONG).show();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Error al crear: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MenuItemDto> call, Throwable t) {
                        Toast.makeText(getContext(), "Fallo conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(v12 -> dismiss());

        return v;
    }

    private String safe(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
