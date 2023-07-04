package com.example.chatthem.chats.presenter;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.ListChatResponse;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatsPresenter {

    private final ChatsContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private Disposable disposable;
    private List<Chat> chatList;
    private List<UserModel> userModelList;

    public ChatsPresenter(ChatsContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
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

    public void getMessaged(){
        APIServices.apiServices.getMessaged("Bearer " + preferenceManager.getString(Constants.KEY_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListChatResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull ListChatResponse listChatResponse) {
                        chatList = listChatResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onGetMessagedError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onGetMessagedSuccess();
                    }
                });

    }
}
