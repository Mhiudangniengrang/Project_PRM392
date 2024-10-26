package com.example.project_prm.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_prm.R;

public class CashPaymentActivity extends AppCompatActivity {
    private EditText addressEditText, phoneEditText;
    private Button confirmPaymentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cash_payment);

        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        // Confirm Payment button listener
        confirmPaymentButton.setOnClickListener(v -> {
            String address = addressEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Process cash payment here or show a success message
                Toast.makeText(this, "Payment confirmed! Thank you.", Toast.LENGTH_SHORT).show();
                finish(); // End the activity after confirmation
            }
        });
    }
}