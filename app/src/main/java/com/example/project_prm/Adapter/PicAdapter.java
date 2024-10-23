package com.example.project_prm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm.R;
import com.example.project_prm.databinding.ViewholderPicBinding;

import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private final List<String> items;
    private final OnPicSelectedListener onPicSelectedListener;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    // Corrected interface name to use consistently
    public interface OnPicSelectedListener {
        void onPicSelected(String imageUrl);
    }

    public PicAdapter(List<String> items, OnPicSelectedListener onPicSelectedListener) {
        this.items = items;
        this.onPicSelectedListener = onPicSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderPicBinding binding = ViewholderPicBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        loadImage(holder.binding.pic, item, holder.itemView.getContext());

        holder.binding.getRoot().setOnClickListener(v -> {
            lastSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectedPosition);
            onPicSelectedListener.onPicSelected(item);
        });

        if (selectedPosition == holder.getAdapterPosition()) {
            holder.binding.picLayout.setBackgroundResource(R.drawable.green_bg_selected);
        } else {
            holder.binding.picLayout.setBackgroundResource(R.drawable.default_background);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderPicBinding binding;

        public ViewHolder(ViewholderPicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void loadImage(ImageView imageView, String url, Context context) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }
}
