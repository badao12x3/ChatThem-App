package com.example.chatthem.profile.presenter;

import android.content.Context;

import com.example.chatthem.cryptophy.ECCc;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.profile.model.ChangePassResponse;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.security.PrivateKey;
import java.security.PublicKey;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePassPresenter {
    
    private final ChangePassContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private Disposable disposable;
    private Context context;

    public ChangePassPresenter(ChangePassContract.ViewInterface viewInterface, PreferenceManager preferenceManager, Context context) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        this.context = context;
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

                        PrivateKey privateKey = null;
                        PublicKey publicKey = null;

                        try {
                            ECCc.deletePrivateKeyFromKeyStore( context,preferenceManager.getString(Constants.KEY_PHONE));
                            privateKey = ECCc.stringToPrivateKey(preferenceManager.getString(Constants.KEY_PRIVATE_KEY));
                            publicKey = ECCc.stringToPublicKey(preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
                            ECCc.savePrivateKey2(context, preferenceManager.getString(Constants.KEY_PHONE), newPass, privateKey, publicKey);

                        } catch (Exception e) {
                            e.printStackTrace();
                            viewInterface.onChangePassError();
                        }

                        viewInterface.onChangePassSuccess();
                    }
                });
    }
}
