package com.example.project_prm.Model;

import java.util.ArrayList;
import java.util.Date;

public class OrderModel {
    private String orderId;
    private ArrayList<ItemsModel> items;
    private double totalFee;
    private Date orderDate;

    // Getters and Setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<ItemsModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsModel> items) {
        this.items = items;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}