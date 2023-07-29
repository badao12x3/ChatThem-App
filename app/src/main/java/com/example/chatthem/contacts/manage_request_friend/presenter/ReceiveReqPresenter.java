package com.example.chatthem.contacts.manage_request_friend.presenter;

import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.contacts.manage_request_friend.model.ListReqRes;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReceiveReqPresenter {
    private ReceiveReqContract.ViewInterface viewInterface;
    private List<UserModel> userModelList;

    private Disposable disposable;

    public List<UserModel> getUserModelList() {
        return userModelList;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public ReceiveReqPresenter(ReceiveReqContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public void getReceiveReq(String token){
        APIServices.apiServices.getRequestFriend("Bearer " +token)
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
                        viewInterface.getReceiveReqFail();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.getReceiveReqSuccess();
                    }
                });
    }
}
