package com.example.project_prm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Helper.ManagmentCart;
import com.example.project_prm.Helper.ChangeNumberItemsListener;
import com.example.project_prm.R;
import com.example.project_prm.adappter.CartAdapter;

public class CartActivity extends AppCompatActivity {

    private double tax = 0.0;
    private ManagmentCart managementCart;
    private RecyclerView viewCart;
    private TextView emptyTxt, totalFeeTxt, taxTxt, deliveryTxt, totalTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagmentCart(this);

        // Ánh xạ các view
        viewCart = findViewById(R.id.viewCart);
        emptyTxt = findViewById(R.id.emptyTxt);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);

        // Thiết lập sự kiện cho các nút và tính toán giỏ hàng
        setVariable();
        initCartList();
        calculatorCart();
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
        viewCart.setAdapter(adapter);  // Đảm bảo CartAdapter được set đúng cách

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
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100.0;

        totalFeeTxt.setText(String.format("$$%.2f", itemTotal));
        taxTxt.setText(String.format("$$%.2f", tax));
        deliveryTxt.setText(String.format("$$%.2f", delivery));
        totalTxt.setText(String.format("$$%.2f", total));
    }

    private void setVariable() {
        // Thiết lập sự kiện cho các nút nếu cần
        View backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity khi nhấn nút quay lại
            }
        });
    }
}
