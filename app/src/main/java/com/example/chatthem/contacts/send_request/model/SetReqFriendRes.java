package com.example.chatthem.contacts.send_request.model;

public class SetReqFriendRes {
    String code, message;
    Friend data;

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

    public Friend getData() {
        return data;
    }

    public void setData(Friend data) {
        this.data = data;
    }
}
