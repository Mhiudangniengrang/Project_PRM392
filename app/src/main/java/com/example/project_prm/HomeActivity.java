package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.project_prm.Adapter.SliderAdapter;
import com.example.project_prm.Model.SliderModel;
import com.example.project_prm.ViewModel.MainViewModel;
import com.example.project_prm.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogout;
    private ImageView profileImageView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final MutableLiveData<List<SliderModel>> _banner = new MutableLiveData<>();
    public LiveData<List<SliderModel>> getBanners() {
        return _banner;
    }


    private ActivityHomeBinding binding;
    private MainViewModel viewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Reference Logout button
        buttonLogout = findViewById(R.id.buttonLogout);

        // Handle Logout button click
        buttonLogout.setOnClickListener(v -> {
            // Firebase sign out
            mAuth.signOut();

            // Navigate back to MainActivity
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish(); // Close HomeActivity
        });
        initBanner();
    }

    private void initBanner() {
        // Use the progressBar defined in your XML
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getBanners().observe(this, new Observer<List<SliderModel>>() {
            @Override
            public void onChanged(List<SliderModel> sliderModels) {
                banners(sliderModels);
                binding.progressBar.setVisibility(View.GONE);
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
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//
//        // Reference Logout button
//        buttonLogout = findViewById(R.id.buttonLogout);
//
//        // Handle Logout button click
//        buttonLogout.setOnClickListener(v -> {
//            // Firebase sign out
//            mAuth.signOut();
//
//            // Navigate back to MainActivity
//            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish(); // Close HomeActivity
//        });
//    }
}
