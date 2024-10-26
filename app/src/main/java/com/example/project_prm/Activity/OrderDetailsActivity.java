package com.example.project_prm.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.ItemsAdapter;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.Model.OrderModel;
import com.example.project_prm.R;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    private RecyclerView itemsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private ArrayList<ItemsModel> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        itemsRecyclerView = findViewById(R.id.recyclerViewOrderItems);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the selected order data
        OrderModel order = getIntent().getParcelableExtra("order_key");

        if (order != null) {
            itemsList = order.getItems();
            itemsAdapter = new ItemsAdapter(itemsList);
            itemsRecyclerView.setAdapter(itemsAdapter);
        } else {
            Toast.makeText(this, "Failed to load order details", Toast.LENGTH_SHORT).show();
        }
    }
}
