package com.example.project_prm.adappter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.project_prm.R;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm.Model.CategoryModel;
import com.example.project_prm.databinding.ViewholderCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> items;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    public CategoryAdapter(List<CategoryModel> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;

        public ViewHolder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        lastSelectedPosition = selectedPosition;
                        selectedPosition = position;
                        notifyItemChanged(lastSelectedPosition);
                        notifyItemChanged(selectedPosition);
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryModel item = items.get(position);
        holder.binding.titleTxt.setText(item.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl())
                .into(holder.binding.pic);

        if (selectedPosition == position) {
            holder.binding.pic.setBackgroundResource(0);
            holder.binding.mainLayout.setBackgroundResource(R.drawable.green_button_bg);
            ImageViewCompat.setImageTintList(holder.binding.pic, ColorStateList.valueOf(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.white)
            ));
            holder.binding.titleTxt.setVisibility(View.VISIBLE);
            holder.binding.titleTxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.binding.pic.setBackgroundResource(R.drawable.grey_bg);
            holder.binding.mainLayout.setBackgroundResource(0);
            ImageViewCompat.setImageTintList(holder.binding.pic, ColorStateList.valueOf(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.black)
            ));
            holder.binding.titleTxt.setVisibility(View.GONE);
            holder.binding.titleTxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}