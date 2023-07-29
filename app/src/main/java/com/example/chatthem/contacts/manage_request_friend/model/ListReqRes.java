package com.example.chatthem.contacts.manage_request_friend.model;

import com.example.chatthem.chats.model.UserModel;

import java.util.List;

public class ListReqRes {
    private String code, message;
    private List<UserModel> data;

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

    public List<UserModel> getData() {
        return data;
    }

    public void setData(List<UserModel> data) {
        this.data = data;
    }
}
