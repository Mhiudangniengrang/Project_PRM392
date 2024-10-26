package com.example.project_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.OrderAdapter;
import com.example.project_prm.Helper.ManagmentCart;
import com.example.project_prm.Helper.OrderManager;
import com.example.project_prm.Model.OrderModel;
import com.example.project_prm.R;

import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private OrderManager orderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderManager = new OrderManager(this);
        ArrayList<OrderModel> orderList = orderManager.getOrderList();

        if (orderList != null && !orderList.isEmpty()) {
            orderAdapter = new OrderAdapter(orderList, order -> {
                Intent intent = new Intent(MyOrderActivity.this, OrderDetailsActivity.class);
                intent.putExtra("order_key", order); // Use 'order' instead of 'orderModel'
                startActivity(intent);
            });

            recyclerView.setAdapter(orderAdapter);
        } else {
            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
        }
    }
}