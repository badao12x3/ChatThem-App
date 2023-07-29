package com.example.chatthem.chats.private_chat_info.presenter;

import android.widget.Toast;

import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.chats.private_chat_info.model.StatusFriendRes;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FriendStatusPresenter {

    private Contract.FriendStatusInterface friendStatusInterface;

    private String token;
    private Disposable disposable;
    private UserModel me, you;
    private String status;

    public FriendStatusPresenter(Contract.FriendStatusInterface friendStatusInterface, String token) {
        this.friendStatusInterface = friendStatusInterface;
        this.token = token;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public UserModel getMe() {
        return me;
    }

    public UserModel getYou() {
        return you;
    }

    public String getStatus() {
        return status;
    }

    public void getStatusFriend(String receiver){
        APIServices.apiServices.getStatusFriend(token, receiver)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusFriendRes>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull StatusFriendRes statusFriendRes) {
                        status = statusFriendRes.getStatus();
                        me = statusFriendRes.getMe();
                        you = statusFriendRes.getYou();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        friendStatusInterface.getStatusFriendFail();

                    }

                    @Override
                    public void onComplete() {
                        friendStatusInterface.getStatusFriendSuccess();
                    }
                });
    }
}
