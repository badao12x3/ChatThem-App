package com.example.chatthem.contacts.send_request.presenter;

import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.chats.private_chat_info.model.StatusFriendRes;
import com.example.chatthem.chats.private_chat_info.presenter.Contract;
import com.example.chatthem.contacts.send_request.model.Friend;
import com.example.chatthem.contacts.send_request.model.SetReqFriendRes;
import com.example.chatthem.networking.APIServices;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileScanUserPresenter {

    private ProfileScanUserContract.ViewInterface viewInterface;

    private String token;
    private Disposable disposable;
    private UserModel me, you;
    private String status;
    private Boolean isSend;

    private Friend friend;

    public ProfileScanUserPresenter(ProfileScanUserContract.ViewInterface viewInterface, String token) {
        this.viewInterface = viewInterface;
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

    public Boolean getSend() {
        return isSend;
    }

    public void getStatusFriend(String receiver){
        APIServices.apiServices.getStatusFriend("Bearer "+token, receiver)
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
                        isSend = statusFriendRes.getSend();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.getStatusFriendFail();

                    }

                    @Override
                    public void onComplete() {
                        viewInterface.getStatusFriendSuccess();
                    }
                });
    }

    public void setReqFriend(String userId){
        APIServices.apiServices.setRequestFriend("Bearer "+token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SetReqFriendRes>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull SetReqFriendRes setReqFriendRes) {
                        friend = setReqFriendRes.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.setReqFriendFail();

                    }

                    @Override
                    public void onComplete() {
                        viewInterface.setReqFriendSuccess();
                    }
                });
    }

    public void setAcceptFriend(String userId, String status){
        APIServices.apiServices.setAccept("Bearer "+token, userId,status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SetReqFriendRes>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull SetReqFriendRes setReqFriendRes) {
                        friend = setReqFriendRes.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.setAcceptFail();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.setAcceptSuccess(status);
                    }
                });
    }

    public void delReq(String userId){
        APIServices.apiServices.delRequest("Bearer "+ token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SetReqFriendRes>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull SetReqFriendRes setReqFriendRes) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.delReqFail();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.delReqSuccess();
                    }
                });
    }

}
