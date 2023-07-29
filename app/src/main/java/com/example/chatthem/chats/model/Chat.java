package com.example.chatthem.chats.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class Chat implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private List<String> member;
    private String type;
    private List<Seen> seen;
    private String createdAt;
    private String updatedAt;
    @SerializedName("__v")
    private long v;
    private Message lastMessage;
    private String receivedId;
    private String avatar;
    private String online;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMember() {
        return member;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Seen> getSeen() {
        return seen;
    }

    public void setSeen(List<Seen> seen) {
        this.seen = seen;
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

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getReceivedID() {
        return receivedId;
    }

    public void setReceivedID(String receivedID) {
        this.receivedId = receivedID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
