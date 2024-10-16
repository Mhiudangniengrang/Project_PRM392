package com.example.project_prm.adappter;

public class CartItem {
    private String name;
    private String imageUrl; // Or int imageResId if you're using a local drawable resource
    private String price;
    private int quantity;

    // Constructor
    public CartItem(String name, String imageUrl, String price, int quantity) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to increase quantity
    public void increaseQuantity() {
        this.quantity++;
    }

    // Method to decrease quantity (with minimum of 1)
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

}
