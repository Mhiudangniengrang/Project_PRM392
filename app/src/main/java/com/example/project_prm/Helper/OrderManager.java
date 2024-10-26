package com.example.project_prm.Helper;
import android.content.Context;
import android.widget.Toast;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.Model.OrderModel;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Date;
public class OrderManager {
    private final TinyDB tinyDB;
    private final Context context;

    public OrderManager(Context context) {
        this.tinyDB = new TinyDB(context);
        this.context = context;
    }

    public void saveOrder(ArrayList<ItemsModel> cartItems, double totalFee) {
        // Retrieve existing order list as JSON strings
        ArrayList<String> orderListJson = tinyDB.getListString("OrderList");

        // Create a new OrderModel instance for the order
        OrderModel newOrder = new OrderModel();
        newOrder.setOrderId("ORDER-" + System.currentTimeMillis());
        newOrder.setItems(new ArrayList<>(cartItems)); // Create a copy of the cart items
        newOrder.setTotalFee(totalFee);
        newOrder.setOrderDate(new Date());

        // Convert the OrderModel to JSON and add it to the list
        String newOrderJson = new Gson().toJson(newOrder);
        orderListJson.add(newOrderJson);

        // Save the updated list of orders back to TinyDB
        tinyDB.putListString("OrderList", orderListJson);

        // Show confirmation
        Toast.makeText(context, "Order has been saved successfully.", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<OrderModel> getOrderList() {
        // Retrieve JSON strings for orders from TinyDB
        ArrayList<String> orderListJson = tinyDB.getListString("OrderList");
        ArrayList<OrderModel> orderList = new ArrayList<>();

        // Convert each JSON string back to an OrderModel object
        Gson gson = new Gson();
        for (String orderJson : orderListJson) {
            OrderModel order = gson.fromJson(orderJson, OrderModel.class);
            orderList.add(order);
        }

        return orderList;
    }

}
