package com.example.project_prm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_prm.Model.SliderModel;
import com.example.project_prm.databinding.SliderItemContainerBinding;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderModel> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems = sliderItems;
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(List<SliderModel> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {

        private SliderItemContainerBinding binding;

        public SliderViewHolder(SliderItemContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setImage(SliderModel sliderItems, Context context) {
            Glide.with(context)
                    .load(sliderItems.getUrl())
                    .apply(new RequestOptions().transform(new CenterInside()))
                    .into(binding.imageSlide);
        }
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        SliderItemContainerBinding binding = SliderItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new SliderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position), context);
        if (position == sliderItems.size() - 1) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
}

