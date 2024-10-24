package com.example.project_prm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm.Helper.ChangeNumberItemsListener;
import com.example.project_prm.Helper.ManagementCart;
import com.example.project_prm.Model.ListItemModel;
import com.example.project_prm.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<ListItemModel> listItemSelected;
    private Context context;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private ManagementCart managementCart;

    public CartAdapter(ArrayList<ListItemModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.context = context;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.managementCart = new ManagementCart(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public ViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItemModel item = listItemSelected.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachTime.setText(String.valueOf(item.getShowRecommended()));
        holder.binding.totalEachTime.setText(String.format("$%s", Math.round(item.getNumberInCart() * item.getPrice())));
        holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberInCart()));

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl()[0])
                .into(holder.binding.pic);

        // Increase item in cart
        holder.binding.plusCartBtn.setOnClickListener(v -> {
            managementCart.plusItem(listItemSelected, position, new ChangeNumberItemsListener() {
                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                    changeNumberItemsListener.onChanged();
                }
            });
        });

        // Decrease item in cart
        holder.binding.minusCartBtn.setOnClickListener(v -> {
            managementCart.minusItem(listItemSelected, position, new ChangeNumberItemsListener() {
                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                    changeNumberItemsListener.onChanged();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }
}


