package com.lovera.diego.restock.models;

public class Type {

    private String Name;
    private String CategoryUuid;
    private String ImageUrl;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategoryUuid() {
        return CategoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        CategoryUuid = categoryUuid;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
