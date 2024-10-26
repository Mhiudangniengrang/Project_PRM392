package com.example.project_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Helper.ManagmentCart;
import com.example.project_prm.Helper.ChangeNumberItemsListener;
import com.example.project_prm.R;
import com.example.project_prm.adappter.CartAdapter;

public class CartActivity extends AppCompatActivity {

    private double tax = 0.0;
    private double totalAmount = 0.0;
    private ManagmentCart managementCart;
    private RecyclerView viewCart;
    private TextView emptyTxt, totalFeeTxt, taxTxt, deliveryTxt, totalTxt;
    private LinearLayout method1, method2; // Cash and Bank Transfer methods
    private View checkOutButton;
    private boolean isBankTransferSelected = false; // Track payment method selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagmentCart(this);

        // Initialize views
        viewCart = findViewById(R.id.viewCart);
        emptyTxt = findViewById(R.id.emptyTxt);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        method1 = findViewById(R.id.method1); // Cash payment
        method2 = findViewById(R.id.method2); // Bank Transfer payment
        checkOutButton = findViewById(R.id.button6); // Check Out button

        setVariable();
        initCartList();
        calculatorCart();

        // Set up listeners for payment methods
        method1.setOnClickListener(v -> {
            isBankTransferSelected = false;
            method1.setBackgroundResource(R.drawable.green_bg_selected);
            method2.setBackgroundResource(R.drawable.grey_bg_selected);
        });

        method2.setOnClickListener(v -> {
            isBankTransferSelected = true;
            method2.setBackgroundResource(R.drawable.green_bg_selected);
            method1.setBackgroundResource(R.drawable.grey_bg_selected);
        });

        // Check Out button listener
        checkOutButton.setOnClickListener(v -> {
            if (isBankTransferSelected) {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                startActivity(intent);
            } else {
                // Launch CashPaymentActivity with total amount for cash payment
                Intent intent = new Intent(CartActivity.this, CashPaymentActivity.class);
                intent.putExtra("TOTAL_AMOUNT", totalAmount);
                startActivity(intent);
            }
        });
    }

    private void initCartList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewCart.setLayoutManager(layoutManager);

        CartAdapter adapter = new CartAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void onChanged() {
                calculatorCart();
            }
        });
        viewCart.setAdapter(adapter);

        if (managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            viewCart.setVisibility(View.VISIBLE);
        }
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10.0;

        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0;
        totalAmount = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100.0;

        totalFeeTxt.setText(String.format("$$%.2f", itemTotal));
        taxTxt.setText(String.format("$$%.2f", tax));
        deliveryTxt.setText(String.format("$$%.2f", delivery));
        totalTxt.setText(String.format("$$%.2f", totalAmount));
        managementCart.updateTinyDB();
    }

    private void setVariable() {
        View backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(v -> finish());
    }
}
