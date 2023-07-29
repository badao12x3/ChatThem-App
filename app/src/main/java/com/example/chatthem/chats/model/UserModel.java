package com.example.chatthem.chats.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    @SerializedName("_id")
    private String id;
    private String phonenumber;
    private String username;
    private String password;
    private String gender;
    private String avatar;
    private String birthday;
    private String email;
    private String cover_image;
    private String address;
    private String country;
    private String city;
    @SerializedName("blocked_inbox")

    private List<Object> blockedInbox;
    @SerializedName("blocked_diary")

    private List<Object> blockedDiary;
    @SerializedName("public_key")

    private String publicKey;
    private String createdAt;
    private String updatedAt;
    @SerializedName("__v")
    private long v;
    private String online;
    private Boolean checked = false;

    public UserModel(String id, String username, String avatar, String publicKey) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.publicKey = publicKey;
    }


    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Object> getBlockedInbox() {
        return blockedInbox;
    }

    public void setBlockedInbox(List<Object> blockedInbox) {
        this.blockedInbox = blockedInbox;
    }

    public List<Object> getBlockedDiary() {
        return blockedDiary;
    }

    public void setBlockedDiary(List<Object> blockedDiary) {
        this.blockedDiary = blockedDiary;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getV() {
        return v;
    }

    public void setV(long v) {
        this.v = v;
    }
}
