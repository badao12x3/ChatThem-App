package com.example.chatthem.chats.private_chat_info.model;

import com.example.chatthem.chats.model.UserModel;

public class StatusFriendRes {
    private String code, message, status;
    private UserModel me, you;

    public UserModel getMe() {
        return me;
    }

    public void setMe(UserModel me) {
        this.me = me;
    }

    public UserModel getYou() {
        return you;
    }

    public void setYou(UserModel you) {
        this.you = you;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
