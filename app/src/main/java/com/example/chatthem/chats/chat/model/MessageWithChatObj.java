package com.example.chatthem.chats.chat.model;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.UserModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageWithChatObj implements Serializable {
    @SerializedName("_id")
    private String id;
    private ChatNoLastMessObj chat;
    private UserModel user;
    private String content;
    private String type;
    private String createdAt;
    private String updatedAt;
    @SerializedName("__v")
    private long v;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatNoLastMessObj getChat() {
        return chat;
    }

    public void setChat(ChatNoLastMessObj chat) {
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
