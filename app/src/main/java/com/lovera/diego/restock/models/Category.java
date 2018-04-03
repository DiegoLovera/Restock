package com.lovera.diego.restock.models;

public class Category {

    private String ImageUrl;
    private String Uuid;
    private String Name;

    public Category(){

    }

    public Category(String imageUrl, String uuid, String name){
        this.ImageUrl = imageUrl;
        this.Uuid = uuid;
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

}
