package com.example.chatthem.profile.model;

import com.example.chatthem.chats.model.UserModel;

public class EditProfileResponse {
    private UserModel data;
    private String message;

    public UserModel getData() {
        return data;
    }

    public void setData(UserModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
