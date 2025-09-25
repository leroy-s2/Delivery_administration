package com.silvacorporation_apps.deliveryadministration.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvacorporation_apps.deliveryadministration.R;
import com.silvacorporation_apps.deliveryadministration.dto.OrderItemDto;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderItemDto> orderItems;

    public OrderItemAdapter(Context context, List<OrderItemDto> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItemDto item = orderItems.get(position);
        holder.tvName.setText(item.getMenuItemName());
        holder.tvQuantity.setText("Cantidad: " + item.getQuantity());
        holder.tvPrice.setText("$" + item.getPrice());
        holder.tvInstructions.setText(item.getSpecialInstructions() != null ? item.getSpecialInstructions() : "");
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice, tvInstructions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvOrderItemName);
            tvQuantity = itemView.findViewById(R.id.tvOrderItemQuantity);
            tvPrice = itemView.findViewById(R.id.tvOrderItemPrice);
            tvInstructions = itemView.findViewById(R.id.tvOrderItemInstructions);
        }
    }
}
