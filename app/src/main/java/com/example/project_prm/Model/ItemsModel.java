package com.example.project_prm.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ItemsModel implements Parcelable {

    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private ArrayList<String> model;
    private double price;
    private double rating;
    private int numberInCart;
    private boolean showRecommended;
    private int categoryId;

    // Default constructor
    public ItemsModel() {
        this.title = "";
        this.description = "";
        this.picUrl = new ArrayList<>();
        this.model = new ArrayList<>();
        this.price = 0.0;
        this.rating = 0.0;
        this.numberInCart = 0;
        this.showRecommended = false;
        this.categoryId = 0;
    }

    // Parcelable constructor
    protected ItemsModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        picUrl = in.createStringArrayList();
        model = in.createStringArrayList();
        price = in.readDouble();
        rating = in.readDouble();
        numberInCart = in.readInt();
        showRecommended = in.readByte() != 0;
        categoryId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(picUrl);
        dest.writeStringList(model);
        dest.writeDouble(price);
        dest.writeDouble(rating);
        dest.writeInt(numberInCart);
        dest.writeByte((byte) (showRecommended ? 1 : 0));
        dest.writeInt(categoryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemsModel> CREATOR = new Creator<ItemsModel>() {
        @Override
        public ItemsModel createFromParcel(Parcel in) {
            return new ItemsModel(in);
        }

        @Override
        public ItemsModel[] newArray(int size) {
            return new ItemsModel[size];
        }
    };

    // Getters and setters (optional)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public ArrayList<String> getModel() {
        return model;
    }

    public void setModel(ArrayList<String> model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public boolean isShowRecommended() {
        return showRecommended;
    }

    public void setShowRecommended(boolean showRecommended) {
        this.showRecommended = showRecommended;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
