package com.lovera.diego.restock.models;

import com.google.firebase.database.PropertyName;

public class Order {
    private String Lat;
    private String Lng;
    private String Status;
    private String User;
    private String Total;
    private String Date;
    private String Uuid;

    public Order(){

    }

    public Order(String lat, String lng, String status, String user, String total, String date){
        this.Lat = lat;
        this.Lng = lng;
        this.Status = status;
        this.User = user;
        this.Total = total;
        this.Date = date;
    }

    @PropertyName("Uuid")
    public String getUuid() { return Uuid; }

    @PropertyName("Uuid")
    public void setUuid(String uuid) {
        Uuid = uuid;
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

    @PropertyName("Total")
    public String getTotal() {
        return Total;
    }

    @PropertyName("Total")
    public void setTotal(String total) {
        Total = total;
    }

    @PropertyName("Date")
    public String getDate() { return Date; }

    @PropertyName("Date")
    public void setDate(String date) { Date = date; }

}
