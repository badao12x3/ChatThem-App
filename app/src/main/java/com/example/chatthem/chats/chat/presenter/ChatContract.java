package com.example.chatthem.chats.chat.presenter;

public class ChatContract {
    public interface ViewInterface{
        void onChatNotExist();
        void onFindChatSucces();
        void onGetMessagesSuccess();
        void onGetMessagesError();

        void onFindChatError();

        void onSendError();

        void onSendSuccess(String content, String typeMess);
        void onCreateAndSendSuccess(String content, String typeMess);

        void receiveNewMsgRealtime(String userId,String username,String avatar,String room,String typeRoom,String publicKey,String text,String typeMess, String time);
    }
}
