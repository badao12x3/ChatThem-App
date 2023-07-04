package com.example.chatthem.chats.create_new_private_chat.presenter;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.UserModel;

public class CreateChatPrivateContract {
    public interface ViewInterface{

        void onUserClicked(UserModel userModel);

        void onSearchUserError();

        void onSearchUserSuccess();

        void onGetListFriendError();

        void onGetListFriendSuccess();
    }
}
