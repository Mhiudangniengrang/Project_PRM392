package com.example.project_prm.adappter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Helper.ChangeNumberItemsListener;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<ItemsModel> cartItems;
    private Context context;
    private ChangeNumberItemsListener changeNumberItemsListener;

    // Update the constructor to accept three parameters
    public CartAdapter(List<ItemsModel> cartItems, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.cartItems = cartItems;
        this.context = context;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Get the current item from the cart
        ItemsModel item = cartItems.get(position);

        // Set the product name, price, and quantity to the appropriate views
        holder.productName.setText(item.getTitle());
        holder.productPrice.setText(String.format("$$%.2f", item.getPrice()));
        holder.productQuantity.setText(String.valueOf(item.getNumberInCart()));

        // Load the product image using Glide
        if (item.getPicUrl() != null && !item.getPicUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getPicUrl().get(0))  // Load the first image in the list
                    .into(holder.productImage);
        }

        // Set up the increase button functionality
        holder.buttonIncrease.setOnClickListener(v -> {
            item.setNumberInCart(item.getNumberInCart() + 1);  // Increase the quantity in the cart
            notifyItemChanged(position);  // Update the item in the RecyclerView
            changeNumberItemsListener.onChanged();  // Notify the listener about the change
        });

        // Set up the decrease button functionality
        holder.buttonDecrease.setOnClickListener(v -> {
            if (item.getNumberInCart() > 1) {  // Ensure the quantity doesn't go below 1
                item.setNumberInCart(item.getNumberInCart() - 1);  // Decrease the quantity in the cart
                notifyItemChanged(position);  // Update the item in the RecyclerView
                changeNumberItemsListener.onChanged();  // Notify the listener about the change
            }
        });

        // Set up the remove item functionality (if necessary)
        holder.removeItem.setOnClickListener(v -> {
            cartItems.remove(position);  // Remove the item from the list
            notifyItemRemoved(position);  // Notify the RecyclerView that an item was removed
            changeNumberItemsListener.onChanged();  // Notify the listener about the change
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage, removeItem;
        Button buttonIncrease, buttonDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productImage = itemView.findViewById(R.id.product_image);
            removeItem = itemView.findViewById(R.id.remove_item);
            buttonIncrease = itemView.findViewById(R.id.button_increase);
            buttonDecrease = itemView.findViewById(R.id.button_decrease);
        }
    }
}
