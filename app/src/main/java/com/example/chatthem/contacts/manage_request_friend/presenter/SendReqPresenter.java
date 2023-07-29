package com.example.chatthem.contacts.manage_request_friend.presenter;

import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.contacts.manage_request_friend.model.ListReqRes;
import com.example.chatthem.networking.APIServices;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SendReqPresenter {
    private SendReqContract.ViewInterface viewInterface;
    private List<UserModel> userModelList;

    private Disposable disposable;

    public List<UserModel> getUserModelList() {
        return userModelList;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public SendReqPresenter(SendReqContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }
    public void getSendReq(String token){
        APIServices.apiServices.getSendRequestFriend("Bearer " +token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListReqRes>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull ListReqRes listReqRes) {
                        userModelList = listReqRes.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.getSendReqFail();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.getSendReqSuccess();
                    }
                });
    }

}
