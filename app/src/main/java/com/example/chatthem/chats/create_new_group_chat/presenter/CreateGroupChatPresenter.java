package com.example.chatthem.chats.create_new_group_chat.presenter;

import com.example.chatthem.chats.chat.model.SendResponse;
import com.example.chatthem.chats.create_new_group_chat.model.SearchUserResponse;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.contacts.model.ListFriendResponse;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.profile.model.ChangePassResponse;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateGroupChatPresenter {
    private final CreateGroupChatContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private String token;
    private List<Disposable> disposables;
    private List<UserModel> userModelList;

    public CreateGroupChatPresenter(CreateGroupChatContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        token ="Bearer " + preferenceManager.getString(Constants.KEY_TOKEN);
        disposables = new ArrayList<>();
    }

    public List<Disposable> getDisposables() {
        return disposables;
    }

    public List<UserModel> getUserModelList() {
        return userModelList;
    }

    public void searchUser (String keyword){
        APIServices.apiServices.searchUser(token, keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchUserResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(SearchUserResponse searchUserResponse) {
                        userModelList = searchUserResponse.getData();
                        for (UserModel u:userModelList){
                            u.setChecked(false);
                        }
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

    public void getListFriend (String user_id){
        APIServices.apiServices.getListFriend(token, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListFriendResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(ListFriendResponse listFriendResponse) {
                        userModelList = listFriendResponse.getData();
                        for (UserModel u:userModelList){
                            u.setChecked(false);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onGetListFriendError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onGetListFriendSuccess();
                    }
                });
    }
}
