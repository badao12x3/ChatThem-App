package com.example.chatthem.chats.group_chat_info.model;

import com.example.chatthem.chats.model.UserModel;

import java.util.List;

public class GetMemberRes {
    private List<UserModel> data;
    private String message;
    private String code;

    public List<UserModel> getData() {
        return data;
    }

    public void setData(List<UserModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
