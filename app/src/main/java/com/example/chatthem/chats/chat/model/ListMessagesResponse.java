package com.example.chatthem.chats.chat.model;

import com.example.chatthem.chats.model.Message;

import java.util.List;

public class ListMessagesResponse {
    private List<Message> data;
    private String message;
    private String code;
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<Message> getData() {
        return data;
    }

    public void setData(List<Message> data) {
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
