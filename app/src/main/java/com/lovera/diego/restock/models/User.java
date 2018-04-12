package com.lovera.diego.restock.models;

import com.google.firebase.database.PropertyName;

/**
 * Created by Federico Lopez Pellicer on 01/04/2018.
 */

public class User {

    private String Email;
    private String DateOfBirth;
    private String Lat;
    private String Lng;
    private String Name;
    private String PhoneNumber;
    private String ProfilePicture;

    public User() {

    }
    public User (String email, String dateOfBirth, String lat, String lng,
                 String name, String phoneNumber, String profilePicture) {
        this.Email = email;
        this.DateOfBirth = dateOfBirth;
        this.Lat = lat;
        this.Lng = lng;
        this.Name = name;
        this.PhoneNumber = phoneNumber;
        this.ProfilePicture = profilePicture;
    }

    @PropertyName("Email")
    public String getEmail() {
        return Email;
    }
    @PropertyName("Email")
    public void setEmail(String email) {
        this.Email = email;
    }
    @PropertyName("DateOfBirth")
    public String getDateOfBirth() {
        return DateOfBirth;
    }
    @PropertyName("DateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.DateOfBirth = dateOfBirth;
    }
    @PropertyName("Lat")
    public String getLat() {
        return Lat;
    }
    @PropertyName("Lat")
    public void setLat(String lat) {
        this.Lat = lat;
    }
    @PropertyName("Lng")
    public String getLng() {
        return Lng;
    }
    @PropertyName("Lng")
    public void setLng(String lng) {
        this.Lng = lng;
    }
    @PropertyName("Name")
    public String getName() {
        return Name;
    }
    @PropertyName("Name")
    public void setName(String name) {
        this.Name = name;
    }
    @PropertyName("PhoneNumber")
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    @PropertyName("PhoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }
    @PropertyName("ProfilePicture")
    public String getProfilePicture() {
        return ProfilePicture;
    }
    @PropertyName("ProfilePicture")
    public void setProfilePicture(String profilePicture) {
        this.ProfilePicture = profilePicture;
    }

}