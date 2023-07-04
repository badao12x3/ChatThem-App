package com.example.chatthem.chats.create_new_group_chat.presenter;

import com.example.chatthem.chats.model.UserModel;

public class CreateGroupChatContract {
    public interface ViewInterface{

        void onSearchUserError();

        void onSearchUserSuccess();

        void onGetListFriendSuccess();

        void onGetListFriendError();

        void onUserClicked();
    }
}
