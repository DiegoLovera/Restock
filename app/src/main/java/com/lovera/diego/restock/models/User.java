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
    private String Username;
    private String PhoneNumber;
    private String ProfilePicture;

    public User() {

    }
    public User (String email) {
        this.Email = email;
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
    @PropertyName("Username")
    public String getUsernameame() {
        return Username;
    }
    @PropertyName("Username")
    public void setUsername(String username) {
        this.Username = username;
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
