package com.silvacorporation_apps.deliveryadministration.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.dto.RestaurantDto;

public class AddRestaurantBottomSheet extends BottomSheetDialogFragment {

    public interface OnRestaurantCreatedListener {
        void onRestaurantCreated(RestaurantDto dto);
    }

    private OnRestaurantCreatedListener listener;
    private Long currentUserId;

    public static AddRestaurantBottomSheet newInstance(Long userId, OnRestaurantCreatedListener listener) {
        AddRestaurantBottomSheet sheet = new AddRestaurantBottomSheet();
        Bundle b = new Bundle();
        if (userId != null) b.putLong("userId", userId);
        sheet.setArguments(b);
        sheet.setListener(listener);
        return sheet;
    }

    public void setListener(OnRestaurantCreatedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet_add_restaurant, container, false);

        TextInputEditText etName = v.findViewById(R.id.etName);
        TextInputEditText etDescription = v.findViewById(R.id.etDescription);
        TextInputEditText etAddress = v.findViewById(R.id.etAddress);
        TextInputEditText etPhone = v.findViewById(R.id.etPhone);
        TextInputEditText etCapacity = v.findViewById(R.id.etCapacity);
        TextInputEditText etOpenTime = v.findViewById(R.id.etOpenTime);
        TextInputEditText etCloseTime = v.findViewById(R.id.etCloseTime);
        TextInputEditText etImageUrl = v.findViewById(R.id.etImageUrl);
        MaterialButton btnSave = v.findViewById(R.id.btnSaveRestaurant);
        MaterialButton btnCancel = v.findViewById(R.id.btnCancel);

        if (getArguments() != null && getArguments().containsKey("userId")) {
            currentUserId = getArguments().getLong("userId");
        }

        btnSave.setOnClickListener(view -> {
            String name = safe(etName);
            String address = safe(etAddress);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
                Toast.makeText(getContext(), "Nombre y dirección son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            String description = safe(etDescription);
            String phone = safe(etPhone);
            String imageUrl = safe(etImageUrl);
            String openTime = safe(etOpenTime);
            String closeTime = safe(etCloseTime);

            Integer capacity = null;
            try {
                String capStr = safe(etCapacity);
                if (!TextUtils.isEmpty(capStr)) capacity = Integer.parseInt(capStr);
            } catch (NumberFormatException ignored) {}

            RestaurantDto dto = new RestaurantDto(
                    name, description, address, phone, imageUrl, capacity, openTime, closeTime, currentUserId
            );

            if (listener != null) listener.onRestaurantCreated(dto);
            dismiss();
        });

        btnCancel.setOnClickListener(v1 -> dismiss());

        return v;
    }

    private String safe(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
