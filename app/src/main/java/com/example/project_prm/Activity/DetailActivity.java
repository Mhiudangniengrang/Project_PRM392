package com.example.project_prm.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.project_prm.Helper.ManagmentCart;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.R;
import com.example.project_prm.databinding.ActivityDetailBinding;
import com.example.project_prm.Adapter.SelectModelAdapter;
import com.example.project_prm.Adapter.PicAdapter;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private ItemsModel item;
    private int numberOrder = 1;
    private ManagmentCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        managementCart = new ManagmentCart(this);

        getBundle();
        initList();
    }

    private void getBundle() {
        item = getIntent().getParcelableExtra("object");

        if (item != null) {
            binding.titleTxt.setText(item.getTitle());
            binding.descriptionTxt.setText(item.getDescription()); // Fixed description setting
            binding.productImage.setImageResource(R.drawable.grey_bg);  // Set default image for product
            binding.priceTxt.setText("$" + item.getPrice());
            binding.ratingTxt.setText(item.getRating() + " Rating");

            binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setNumberInCart(numberOrder);
                    managementCart.insertFood(item);
                }
            });

            binding.backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            binding.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add logic for the favorite button here if needed
                }
            });
        }
    }

    private void initList() {
        ArrayList<String> modelList = new ArrayList<>();
        if (item != null && item.getModel() != null) {
            modelList.addAll(item.getModel());
        }

        SelectModelAdapter selectModelAdapter = new SelectModelAdapter(this, modelList);
        binding.recyclerView.setAdapter(selectModelAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<String> picList = new ArrayList<>();
        if (item != null && item.getPicUrl() != null) {
            picList.addAll(item.getPicUrl());
        }

        if (!picList.isEmpty()) {
            Glide.with(this).load(picList.get(0)).into(binding.productImage);
        }

        PicAdapter picAdapter = new PicAdapter(picList, new PicAdapter.OnPicSelectedListener() {
            @Override
            public void onPicSelected(String selectedImageUrl) {
                Glide.with(DetailActivity.this).load(selectedImageUrl).into(binding.productImage);
            }
        });
        binding.verticalRecyclerView.setAdapter(picAdapter);
        binding.verticalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}