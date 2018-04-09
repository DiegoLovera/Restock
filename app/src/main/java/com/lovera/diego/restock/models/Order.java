package com.lovera.diego.restock.models;

import com.google.firebase.database.PropertyName;

public class Order {
    private String Lat;
    private String Lng;
    private String Status;
    private String User;

    public Order(){

    }
    @PropertyName("Lat")
    public String getLat() {
        return Lat;
    }

    @PropertyName("Lat")
    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    @PropertyName("Lng")
    public String getLng() {
        return Lng;
    }

    @PropertyName("Lng")
    public void setLng(String Lng) {
        this.Lng = Lng;
    }

    @PropertyName("Status")
    public String getStatus() {
        return Status;
    }

    @PropertyName("Status")
    public void setStatus(String Status) {
        this.Status = Status;
    }

    @PropertyName("User")
    public String getUser() {
        return User;
    }

    @PropertyName("User")
    public void setUser(String User) {
        this.User = User;
    }
}
