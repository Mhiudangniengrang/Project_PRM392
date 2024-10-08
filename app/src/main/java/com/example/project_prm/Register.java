package com.example.project_prm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister, buttonBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Setup padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        // Handle register button click
        buttonRegister.setOnClickListener(v -> registerUser());

        // Handle back to intro button click
        buttonBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close RegisterActivity to prevent going back to this screen
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter email !");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter password !");
            return;
        }
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Password does not match !");
            return;
        }

        // Check Internet connection
        if (!isNetworkAvailable()) {
            Toast.makeText(Register.this, "No internet", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable register button to prevent multiple clicks
        buttonRegister.setEnabled(false);

        // Firebase register with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    buttonRegister.setEnabled(true);  // Re-enable the button after the process completes
                    if (task.isSuccessful()) {
                        // Sign up success, navigate to login activity
                        Toast.makeText(Register.this, "Login successfully !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                        finish(); // Close RegisterActivity
                    } else {
                        // If sign up fails, display a message to the user.
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed !";
                        Toast.makeText(Register.this, "Login failed !: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Check Internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}