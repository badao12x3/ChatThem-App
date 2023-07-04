package com.example.chatthem.authentication.model;

import com.example.chatthem.chats.model.UserModel;

public class SignupResponse {
    private User data;
    private String token;
    private String message;
    private String code;
    private String details;


    public SignupResponse(User data, String token, String message, String code, String details) {
        this.data = data;
        this.token = token;
        this.message = message;
        this.code = code;
        this.details = details;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
