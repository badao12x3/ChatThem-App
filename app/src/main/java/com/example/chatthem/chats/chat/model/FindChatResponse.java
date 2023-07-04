package com.example.chatthem.chats.chat.model;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.Message;

import java.util.List;

public class FindChatResponse {

    private ChatNoLastMessObj data;
    private String message;
    private String code;

    public ChatNoLastMessObj getData() {
        return data;
    }

    public void setData(ChatNoLastMessObj data) {
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
