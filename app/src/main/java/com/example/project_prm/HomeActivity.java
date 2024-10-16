package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
    }
}
