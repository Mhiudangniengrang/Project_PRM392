package com.example.project_prm.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.project_prm.Model.CategoryModel;
import com.example.project_prm.adappter.CategoryAdapter;
import com.example.project_prm.Adapter.SliderAdapter;
import com.example.project_prm.Model.SliderModel;
import com.example.project_prm.ViewModel.MainViewModel;
import com.example.project_prm.databinding.ActivityHomeBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private MainViewModel viewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);

        viewModel.getCategories().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                binding.viewCategory.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                binding.viewCategory.setAdapter(new CategoryAdapter(categoryModels));
                binding.progressBarCategory.setVisibility(View.GONE);
            }
        });

        viewModel.loadCategory();
    }
    private void initBanner() {
        // Use the progressBar defined in your XML
        binding.progressBarCategory.setVisibility(View.VISIBLE);

        viewModel.getBanners().observe(this, new Observer<List<SliderModel>>() {
            @Override
            public void onChanged(List<SliderModel> sliderModels) {
                banners(sliderModels);
                binding.progressBarCategory.setVisibility(View.GONE);
            }
        });

        viewModel.loadBanners();
    }

    private void banners(List<SliderModel> image) {
        binding.viewPager2.setAdapter(new SliderAdapter(image, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPager2.setPageTransformer(compositePageTransformer);

        if (image.size() > 1) {
            binding.dotIndicator.setVisibility(View.VISIBLE);
            binding.dotIndicator.attachTo(binding.viewPager2);
        }
    }
}
