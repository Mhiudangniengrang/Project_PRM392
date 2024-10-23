package com.example.project_prm.Model;

public class CategoryModel {
    private String title;
    private int id;
    private String picUrl;

    // Default constructor
    public CategoryModel() {
        this.title = "";
        this.id = 0;
        this.picUrl = "";
    }

    // Constructor with parameters
    public CategoryModel(String title, int id, String picUrl) {
        this.title = title;
        this.id = id;
        this.picUrl = picUrl;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}