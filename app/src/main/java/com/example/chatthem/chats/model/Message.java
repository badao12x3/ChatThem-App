package com.example.chatthem.chats.model;

import com.example.chatthem.authentication.model.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class Message implements Serializable {
    @SerializedName("_id")
    private String id;
    private String chat;
    private Chat chatObject;
    private UserModel user;
    private String content;
    private String type;
    private String createdAt;
    private String updatedAt;
    @SerializedName("__v")
    private long v;

    public Message(String chat, UserModel user, String content, String type, String updatedAt) {
        this.chat = chat;
        this.user = user;
        this.content = content;
        this.type = type;
        this.updatedAt = updatedAt;
    }

    public Chat getChatObject() {
        return chatObject;
    }

    public void setChatObject(Chat chatObject) {
        this.chatObject = chatObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
