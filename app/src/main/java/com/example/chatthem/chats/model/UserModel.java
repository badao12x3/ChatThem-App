package com.example.chatthem.chats.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class UserModel implements Serializable {
    private String id;
    private String phonenumber;
    private String username;
    private String password;
    private String gender;
    private String avatar;
    private List<Object> blockedInbox;
    private List<Object> blockedDiary;
    private String publicKey;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private long v;
    private String online;

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getV() {
        return v;
    }

    public void setV(long v) {
        this.v = v;
    }
}
