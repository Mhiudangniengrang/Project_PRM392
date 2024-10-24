package com.example.project_prm.Model;

public class ListItemModel {
    private String title;
    private double price;
    private int numberInCart;
    private String[] picUrl;
    private boolean showRecommended;

    // Constructor
    public ListItemModel(String title, double price, int numberInCart, String[] picUrl, boolean showRecommended) {
        this.title = title;
        this.price = price;
        this.numberInCart = numberInCart;
        this.picUrl = picUrl;
        this.showRecommended = showRecommended;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public String[] getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String[] picUrl) {
        this.picUrl = picUrl;
    }

    public boolean getShowRecommended() {
        return showRecommended;
    }

    public void setShowRecommended(boolean showRecommended) {
        this.showRecommended = showRecommended;
    }
}
