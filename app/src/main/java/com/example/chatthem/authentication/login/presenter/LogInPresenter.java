package com.example.chatthem.authentication.login.presenter;

import android.util.Log;

import com.example.chatthem.authentication.model.LoginResponse;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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



    public LogInPresenter(LogInContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;

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
                            viewInterface.onLoginSuccess();

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
