package com.example.chatthem.authentication.login.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.chatthem.cryptophy.ECCc;
import com.example.chatthem.authentication.model.LoginResponse;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class LogInPresenter {
    private LogInContract.ViewInterface viewInterface;
    private LoginResponse mLoginResponse;
    private Disposable mDisposable;
    private PreferenceManager preferenceManager;
    private Context context;



    public LogInPresenter(LogInContract.ViewInterface viewInterface, PreferenceManager preferenceManager, Context context) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        this.context = context;
    }

    public void login(String phone, String password){
        try {
            JSONObject body = new JSONObject();
            body.put(Constants.KEY_PHONE,phone);
            body.put(Constants.KEY_PASSWORD,password);

            APIServices.apiServices.login(phone, password)
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
                                        if (Objects.equals(mLoginResponse.getCode(), "508")) viewInterface.onLoginFail();
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

                            }else viewInterface.onLoginError();
                            Log.e("HP",e.toString());
                        }

                        @Override
                        public void onComplete() {
                            UserModel user = mLoginResponse.getData();
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_USED_ID, user.getId());
                            preferenceManager.putString(Constants.KEY_NAME, user.getUsername());
                            preferenceManager.putString(Constants.KEY_AVATAR, user.getAvatar());
                            if (user.getCover_image() != null){
                                preferenceManager.putString(Constants.KEY_COVERIMAGE, user.getCover_image());
                            }
                            preferenceManager.putString(Constants.KEY_PHONE,user.getPhonenumber());
                            preferenceManager.putString(Constants.KEY_PASSWORD, password);
                            if (user.getPublicKey() != null){
                                preferenceManager.putString(Constants.KEY_PUBLIC_KEY, user.getPublicKey());
                            }
                            if (user.getGender() != null){
                                preferenceManager.putString(Constants.KEY_GENDER, user.getGender());
                            }
                            if (user.getBirthday() != null){
                                preferenceManager.putString(Constants.KEY_BIRTHDAY, user.getBirthday());
                            }
                            if (user.getEmail() != null){
                                preferenceManager.putString(Constants.KEY_EMAIL, user.getEmail());
                            }
                            if (user.getCountry() != null){
                                preferenceManager.putString(Constants.KEY_ADDRESS_COUNTRY, user.getCountry());
                                preferenceManager.putString(Constants.KEY_ADDRESS_CITY, user.getCity());
                                preferenceManager.putString(Constants.KEY_ADDRESS_DETAIL, user.getAddress());
                            }
                            preferenceManager.putString(Constants.KEY_TOKEN, mLoginResponse.getToken());




                            try {
                                //Lấy private Key từ KeyStore
                                PrivateKey priKey = ECCc.getPrivateKeyFromKeyStore(
                                        context,
                                        phone,
                                        password
                                );
                                //Lưu privateKey vào Preference cho dễ gọi lại, tăng hiệu năng máy
                                if (priKey != null) {
                                    String priKeyStr = ECCc.privateKeyToString(priKey);
                                    preferenceManager.putString(Constants.KEY_PRIVATE_KEY, priKeyStr);
                                }
                                viewInterface.onLoginSuccess();

                            } catch (IOException | CertificateException | KeyStoreException |
                                     NoSuchAlgorithmException | SignatureException |
                                     NoSuchProviderException | InvalidKeyException |
                                     UnrecoverableKeyException e) {
                                e.printStackTrace();
                                preferenceManager.clear();
                                viewInterface.onGetPrivateKeyError();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public LoginResponse getLoginResponse() {
        return mLoginResponse;
    }

    public Disposable getDisposable() {
        return mDisposable;
    }
}
