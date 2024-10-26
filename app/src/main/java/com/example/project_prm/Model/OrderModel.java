package com.example.project_prm.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class OrderModel implements Parcelable {
    private String orderId;
    private ArrayList<ItemsModel> items;
    private double totalFee;
    private Date orderDate;

    public OrderModel() {
        items = new ArrayList<>();
        totalFee = 0.0;
    }

    // Parcelable constructor
    protected OrderModel(Parcel in) {
        orderId = in.readString();
        items = in.createTypedArrayList(ItemsModel.CREATOR);
        totalFee = in.readDouble();
        orderDate = new Date(in.readLong()); // Convert long to Date
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeTypedList(items);
        dest.writeDouble(totalFee);
        dest.writeLong(orderDate != null ? orderDate.getTime() : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    // Getters and setters
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