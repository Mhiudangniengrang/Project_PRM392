package com.example.project_prm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.R;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private final ArrayList<ItemsModel> itemsList;

    public ItemsAdapter(ArrayList<ItemsModel> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsModel item = itemsList.get(position);

        holder.textItemName.setText(item.getTitle());
        holder.textItemPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.textItemQuantity.setText("Quantity: " + item.getNumberInCart());

        if (!item.getPicUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getPicUrl().get(0)) // Load the first image URL in the list
                    .placeholder(R.drawable.ic_launcher_background) // Optional placeholder
                    .error(R.drawable.ic_launcher_background) // Optional error image
                    .into(holder.imageItem);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName, textItemPrice, textItemQuantity;
        ImageView imageItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemPrice = itemView.findViewById(R.id.textItemPrice);
            textItemQuantity = itemView.findViewById(R.id.textItemQuantity);
            imageItem = itemView.findViewById(R.id.imageItem);
        }
    }
}
