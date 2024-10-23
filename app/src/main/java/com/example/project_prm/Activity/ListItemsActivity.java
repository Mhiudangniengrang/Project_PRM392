package com.example.project_prm.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project_prm.Adapter.ListItemsAdapter;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.ViewModel.MainViewModel;
import com.example.project_prm.databinding.ActivityListItemsBinding;

import java.util.List;

public class ListItemsActivity extends AppCompatActivity {
    private ActivityListItemsBinding binding;
    private MainViewModel viewModel = new MainViewModel();
    private int id; // Changed to int
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getBundle();
        initList();

        // Load filtered data after getting the bundle
        viewModel.loadFiltered(id);
    }

    private void initList() {
        binding.progressBarList.setVisibility(View.VISIBLE);

        viewModel.getRecommended().observe(this, new Observer<List<ItemsModel>>() {
            @Override
            public void onChanged(List<ItemsModel> items) {
                if (items != null) {
                    binding.viewList.setLayoutManager(new GridLayoutManager(ListItemsActivity.this, 2));
                    binding.viewList.setAdapter(new ListItemsAdapter(ListItemsActivity.this, items));
                } else {
                    // Handle empty or null list
                }
                binding.progressBarList.setVisibility(View.GONE);
            }
        });
    }

    private void getBundle() {
        id = getIntent().getIntExtra("id", 0); // Changed to getIntExtra
        title = getIntent().getStringExtra("title");

        if (title != null) {
            binding.categoryTxt.setText(title);
        }
    }
}