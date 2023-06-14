package com.example.chatthem.networking;

import com.example.chatthem.authentication.model.LoginResponse;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.utilities.Constants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIServices {

    HttpLoggingInterceptor LoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(LoggingInterceptor);

    APIServices apiServices = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okBuilder.build())
            .build()
            .create(APIServices.class);
    @FormUrlEncoded
    @POST("users/login")
    Observable<LoginResponse> login(
            @Field("phonenumber") String phonenumber,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("users/register")
    Observable<LoginResponse> signup(
            @Field("avatar") String avatar,
            @Field("username") String username,
            @Field("phonenumber") String phonenumber,
            @Field("password") String password,
            @Field("publicKey") String publicKey
    );


}
