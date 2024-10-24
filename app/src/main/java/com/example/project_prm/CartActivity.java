package com.example.project_prm;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project_prm.Helper.ManagementCart;
import com.example.project_prm.databinding.ActivityCardBinding;

public class CartActivity extends AppCompatActivity {
    private ActivityCardBinding binding;
    private ManagementCart managementCart;
    private double tax = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card);
        managementCart = new ManagementCart(this);

        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.method1.setOnClickListener(v -> {
            binding.method1.setBackgroundResource(R.drawable.green_bg_selected);
            binding.methodIc1.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CartActivity.this, R.color.green)));
            binding.methodTitle1.setTextColor(getResources().getColor(R.color.green));
            binding.methodSubTitle1.setTextColor(getResources().getColor(R.color.green));

            binding.method2.setBackgroundResource(R.drawable.green_bg_selected);
            binding.methodIc2.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CartActivity.this, R.color.black)));
            binding.methodTitle2.setTextColor(getResources().getColor(R.color.black));
            binding.methodSubTitle2.setTextColor(getResources().getColor(R.color.grey));
        });

        binding.method2.setOnClickListener(v -> {
            binding.method2.setBackgroundResource(R.drawable.green_bg_selected);
            binding.methodIc2.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CartActivity.this, R.color.green)));
            binding.methodTitle2.setTextColor(getResources().getColor(R.color.green));
            binding.methodSubTitle2.setTextColor(getResources().getColor(R.color.green));

            binding.method1.setBackgroundResource(R.drawable.green_bg_selected);
            binding.methodIc1.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(CartActivity.this, R.color.black)));
            binding.methodTitle1.setTextColor(getResources().getColor(R.color.black));
            binding.methodSubTitle1.setTextColor(getResources().getColor(R.color.grey));
        });

    }
}