package com.example.chatthem.chats.chat.presenter;

public class ChatContract {
    public interface ViewInterface{
        void onChatNotExist();
        void onFindChatSucces();
        void onGetMessagesSuccess();
        void onGetMessagesError();

        void onFindChatError();
    }
}
