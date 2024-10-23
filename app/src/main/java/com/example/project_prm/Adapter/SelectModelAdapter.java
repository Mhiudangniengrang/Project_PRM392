package com.example.project_prm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.R;

import java.util.List;

public class SelectModelAdapter extends RecyclerView.Adapter<SelectModelAdapter.ViewHolder> {

    private final List<String> items;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;
    private final Context context;

    public SelectModelAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewhoolder_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.modelTxt.setText(items.get(position));

        holder.itemView.setOnClickListener(v -> {
            lastSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectedPosition);
        });

        if (selectedPosition == holder.getAdapterPosition()) {
            holder.modelLayout.setBackgroundResource(R.drawable.green_bg_selected);
            holder.modelTxt.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.modelLayout.setBackgroundResource(R.drawable.grey_bg);
            holder.modelTxt.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView modelTxt;
        View modelLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            modelTxt = itemView.findViewById(R.id.modelTxt);
            modelLayout = itemView.findViewById(R.id.modelLayout);
        }
    }
}
