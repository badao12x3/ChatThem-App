package com.example.chatthem.chats.presenter;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.UserModel;

public class ChatsContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {
        // Interface này dành cho SignUpActivity
        void onConversionClicked(Chat chat);

        void onAddNewChatClick();

        void onRecentUserChatClick(UserModel userModel);

        void onGetMessagedSuccess();

        void onGetMessagedError();

        void onNewChatCreate();

        void receiveNewMsgRealtime();
    }
}
