package com.example.chatthem.chats.group_chat_info.presenter;

public class AddMemContract {
    public interface ViewInterface{

        void onSearchUserError();

        void onSearchUserSuccess();

        void onGetListFriendSuccess();

        void onGetListFriendError();

        void onUserClicked();
    }
}
