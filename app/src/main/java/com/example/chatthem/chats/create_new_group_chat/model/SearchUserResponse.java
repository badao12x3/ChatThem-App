package com.example.chatthem.chats.create_new_group_chat.model;

import com.example.chatthem.chats.model.UserModel;

import java.util.List;

public class SearchUserResponse {
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
