package com.example.chatthem.chats.presenter;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.UserModel;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class ChatsPresenter {

    private ChatsContract.ViewInterface viewInterface;
    private Disposable disposable;
    private List<Chat> chatList;
    private List<UserModel> userModelList;

    public ChatsPresenter(ChatsContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public List<UserModel> getUserModelList() {
        return userModelList;
    }

    private void getMessaged(){

    }
}
