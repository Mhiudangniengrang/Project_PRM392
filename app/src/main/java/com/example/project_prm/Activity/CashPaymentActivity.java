package com.example.project_prm.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.Helper.ManagmentCart;
import com.example.project_prm.Helper.OrderManager;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.R;

import java.util.ArrayList;

public class CashPaymentActivity extends AppCompatActivity {
    private EditText addressEditText, phoneEditText;
    private Button confirmPaymentButton;
    private ManagmentCart managementCart;
    private OrderManager orderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_payment);

        // Initialize UI elements
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        // Initialize ManagementCart and OrderManager
        managementCart = new ManagmentCart(this);
        orderManager = new OrderManager(this);

        // Confirm Payment button listener
        confirmPaymentButton.setOnClickListener(v -> {
            String address = addressEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Get items from the cart and calculate the total
                ArrayList<ItemsModel> cartItems = managementCart.getListCart();
                double totalAmount = managementCart.getTotalFee();

                // Save the order
                orderManager.saveOrder(cartItems, totalAmount);

                // Clear the cart
                managementCart.clearCart();

                // Show success message and finish activity
                Toast.makeText(this, "Payment confirmed! Thank you.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
