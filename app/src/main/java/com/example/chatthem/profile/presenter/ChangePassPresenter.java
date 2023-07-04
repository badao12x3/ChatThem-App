package com.example.chatthem.profile.presenter;

import com.example.chatthem.networking.APIServices;
import com.example.chatthem.profile.model.ChangePassResponse;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePassPresenter {
    
    private final ChangePassContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private Disposable disposable;

    public ChangePassPresenter(ChangePassContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public void changePass(String currPass, String newPass) {
        APIServices.apiServices.changePassword("Bearer " + preferenceManager.getString(Constants.KEY_TOKEN), currPass, newPass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChangePassResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull ChangePassResponse changePassResponse) {
                        preferenceManager.putString(Constants.KEY_TOKEN, changePassResponse.getToken());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onChangePassError();
                    }

                    @Override
                    public void onComplete() {
                        preferenceManager.putString(Constants.KEY_PASSWORD, newPass);
                        viewInterface.onChangePassSuccess();
                    }
                });
    }
}
