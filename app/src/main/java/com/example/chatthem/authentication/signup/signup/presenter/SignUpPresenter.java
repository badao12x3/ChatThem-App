package com.example.chatthem.authentication.signup.signup.presenter;

import android.content.Context;
import android.util.Log;

import com.example.chatthem.ECC.ECCc;
import com.example.chatthem.authentication.login.presenter.LogInContract;
import com.example.chatthem.authentication.model.LoginResponse;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignUpPresenter {

    private Context mContext;
    private SignUpContract.ViewInterface viewInterface;
    private LoginResponse mLoginResponse;
    private Disposable mDisposable;
    private PreferenceManager preferenceManager;
    private String priKeyStr = "";
    private String publicKeyString = "";
    private KeyPair keyPair;



    public SignUpPresenter(Context context, SignUpContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        this.mContext = context;

    }

    public String getPublicKeyStr(){
        keyPair = ECCc.generateECKeys();
        try {
//            System.out.println(ECCc.privateKeyToString(keyPair.getPrivate()));
            if (keyPair != null) {
                priKeyStr = ECCc.privateKeyToString(keyPair.getPrivate());
                publicKeyString = ECCc.publicKeyToString(keyPair.getPublic());
            }else viewInterface.onGenKeyPairSignUpError();
        } catch (IOException e) {
            e.printStackTrace();
            viewInterface.onGenKeyPairSignUpError();
        }
        return publicKeyString;
    }
    public void signup(String avatar, String username,String phonenumber, String password, String publicKey){
        APIServices.apiServices.signup(avatar, username,phonenumber, password, publicKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mDisposable = d;
                        }

                        @Override
                        public void onNext(@NonNull LoginResponse loginResponse) {
                            mLoginResponse = loginResponse;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
//                            Http
//                            if (Objects.equals(, "Password or phone is incorrect")){
//                                viewInterface.onLoginFail();
//                            }else {
//                                viewInterface.onLoginError();
//                            }

//                            Log.e("HP", e);

                            if(e instanceof HttpException) {
                                HttpException httpException = (HttpException) e;

                                try {
                                    if (httpException.response() != null && httpException.response().errorBody() != null) {
                                        String error = httpException.response().errorBody().string();

                                        mLoginResponse = new Gson().fromJson(error, LoginResponse.class);
                                        if (Objects.equals(mLoginResponse.getCode(), "9997")) viewInterface.onPhoneExistSignUpFail();

                                        Log.e("HP",error);
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
//                                if(httpException.code() == 400)
//                                    Log.d(TAG, "onError: BAD REQUEST");
//                                else if(httpException.code() == 401)
//                                    Log.d(TAG, "onError: NOT AUTHORIZED");
//                                else if(httpException.code() == 403)
//                                    Log.d(TAG, "onError: FORBIDDEN");
//                                else if(httpException.code() == 404)
//                                    Log.d(TAG, "onError: NOT FOUND");
//                                else if(httpException.code() == 500)
//                                    Log.d(TAG, "onError: INTERNAL SERVER ERROR");
//                                else if(httpException.code() == 502)
//                                    Log.d(TAG, "onError: BAD GATEWAY");

                            }else viewInterface.onSignUpError();

                            Log.e("HP",e.toString());
                        }

                        @Override
                        public void onComplete() {

                            //lưu private key
                            try {
                                ECCc.savePrivateKey(mContext,
                                        phonenumber,
                                        password,
                                        keyPair
                                );
                            } catch (CertificateException | KeyStoreException | IOException |
                                     NoSuchAlgorithmException | SignatureException |
                                     NoSuchProviderException | InvalidKeyException e) {
                                e.printStackTrace();
                                viewInterface.onSavePriKeySignUpError();
                                return;
                            }

                            User user = mLoginResponse.getData();
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_USED_ID, user.getId());
                            preferenceManager.putString(Constants.KEY_NAME, user.getUsername());
                            preferenceManager.putString(Constants.KEY_AVATAR, user.getAvatar());
                            if (user.getCoverImage() != null){
                                preferenceManager.putString(Constants.KEY_COVERIMAGE, user.getCoverImage());
                            }
                            preferenceManager.putString(Constants.KEY_PHONE,user.getPhone());
                            preferenceManager.putString(Constants.KEY_PASSWORD, password);
                            if (user.getPublicKey() != null){
                                preferenceManager.putString(Constants.KEY_PUBLIC_KEY, user.getPublicKey());
                            }
                            preferenceManager.putString(Constants.KEY_TOKEN, mLoginResponse.getToken());


                            viewInterface.onSignUpSuccess();

                        }
                    });
    }

//    public boolean savePrivateKey(Context context, String phonenumber, String password) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
//        //lưu private key
//        ECCc.savePrivateKey(context,
//                phonenumber,
//                password,
//                keyPair
//        );
//    }

    public LoginResponse getLoginResponse() {
        return mLoginResponse;
    }

    public Disposable getDisposable() {
        return mDisposable;
    }
}

