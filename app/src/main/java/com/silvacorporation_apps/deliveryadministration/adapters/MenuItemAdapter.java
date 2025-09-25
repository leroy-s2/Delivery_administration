package com.silvacorporation_apps.deliveryadministration.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.dto.MenuItemDto;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private Context context;
    private List<MenuItemDto> menuList;

    public MenuItemAdapter(Context context, List<MenuItemDto> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItemDto menu = menuList.get(position);

        holder.tvName.setText(menu.getName());
        holder.tvPrice.setText("$" + menu.getPrice());
        holder.tvCategory.setText(menu.getCategory() != null ? menu.getCategory() : "Sin categoría");

        // Imagen
        if (menu.getImageUrl() != null && !menu.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(menu.getImageUrl())
                    .placeholder(R.drawable.ic_restaurant_placeholder)
                    .into(holder.ivMenuImage);
        } else {
            holder.ivMenuImage.setImageResource(R.drawable.ic_restaurant_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return menuList != null ? menuList.size() : 0;
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvCategory;
        ImageView ivMenuImage;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMenuName);
            tvPrice = itemView.findViewById(R.id.tvMenuPrice);
            tvCategory = itemView.findViewById(R.id.tvMenuCategory);
            ivMenuImage = itemView.findViewById(R.id.ivMenuImage);
        }
    }
}
