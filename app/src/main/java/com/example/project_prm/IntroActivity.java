package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {

    // Declare the binding variable
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the binding
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the button click listener
        binding.startBtn.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}