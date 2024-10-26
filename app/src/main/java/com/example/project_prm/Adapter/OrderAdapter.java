package com.example.project_prm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Model.OrderModel;
import com.example.project_prm.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final ArrayList<OrderModel> orderList;
    private final OnOrderClickListener listener;

    public OrderAdapter(ArrayList<OrderModel> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    public interface OnOrderClickListener {
        void onOrderClick(OrderModel order);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Set order details (e.g., ID, date, total fee)
        holder.textOrderId.setText("Order ID: " + order.getOrderId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.textOrderDate.setText("Date: " + dateFormat.format(order.getOrderDate()));

        holder.textOrderTotal.setText("Total: $" + order.getTotalFee());

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textOrderDate, textOrderTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.textOrderId);
            textOrderDate = itemView.findViewById(R.id.textOrderDate);
            textOrderTotal = itemView.findViewById(R.id.textTotalAmount);
        }
    }
}
