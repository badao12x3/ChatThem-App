package com.example.chatthem.chats.chat.model;

import com.example.chatthem.chats.model.Message;
import com.example.chatthem.chats.model.Seen;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatNoLastMessObj implements Serializable {
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
    private String lastMessage;
    private String receivedID;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getReceivedID() {
        return receivedID;
    }

    public void setReceivedID(String receivedID) {
        this.receivedID = receivedID;
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
