package com.example.chatthem.chats.chat.presenter;

public class ChatContract {
    public interface ViewInterface{
        void onChatNotExist();
        void onFindChatSucces();
        void onGetMessagesSuccess();
        void onGetMessagesError();

        void onFindChatError();

        void onSendError(int pos);

        void onSendSuccess(String content, String typeMess, int pos);
        void onCreateAndSendSuccess(String content, String typeMess, int pos);

        void receiveNewMsgRealtime(String userId,String username,String avatar,String room,String typeRoom,String publicKey,String text,String typeMess, String time);

        void showToast(String msg);
    }
}
