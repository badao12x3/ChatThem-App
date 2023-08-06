package com.example.chatthem.contacts.presenter;

import com.example.chatthem.chats.create_new_group_chat.model.SearchUserResponse;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.networking.APIServices;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactsPresenter {
    private ContactsContract.ViewInterface viewInterface;
    private String token;
    private Disposable disposable;
    UserModel userModels;

    public UserModel getUserModels() {
        return userModels;
    }

    public ContactsPresenter(ContactsContract.ViewInterface viewInterface, String token) {
        this.viewInterface = viewInterface;
        this.token = token;
    }

    public void searchUser(String keyword){
        APIServices.apiServices.searchUser("Bearer " + token, keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchUserResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable= d;
                    }

                    @Override
                    public void onNext(SearchUserResponse searchUserResponse) {
                        userModels = searchUserResponse.getData().get(0);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onSearchUserError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onSearchUserSuccess();
                    }
                });
    }
}
