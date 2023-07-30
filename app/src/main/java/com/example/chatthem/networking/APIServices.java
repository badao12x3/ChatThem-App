package com.example.chatthem.networking;

import androidx.annotation.Nullable;

import com.example.chatthem.authentication.model.LoginResponse;
import com.example.chatthem.authentication.model.SignupResponse;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.chats.chat.model.FindChatResponse;
import com.example.chatthem.chats.chat.model.ListMessagesResponse;
import com.example.chatthem.chats.chat.model.SendResponse;
import com.example.chatthem.chats.create_new_group_chat.model.SearchUserResponse;
import com.example.chatthem.chats.group_chat_info.model.GetMemberRes;
import com.example.chatthem.chats.model.ListChatResponse;
import com.example.chatthem.chats.private_chat_info.model.StatusFriendRes;
import com.example.chatthem.contacts.manage_request_friend.model.ListReqRes;
import com.example.chatthem.contacts.model.ListFriendResponse;
import com.example.chatthem.profile.model.ChangePassResponse;
import com.example.chatthem.profile.model.EditProfileResponse;
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
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Observable<SignupResponse> signup(
            @Field("avatar") String avatar,
            @Field("username") String username,
            @Field("phonenumber") String phonenumber,
            @Field("password") String password,
            @Field("publicKey") String publicKey
    );
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("chats/getMessaged")
    Observable<ListChatResponse> getMessaged(
            @Header("Authorization") String token
    );
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("chats/getMessages/{chatId}")
    Observable<ListMessagesResponse> getMessages(
            @Header("Authorization") String token,
            @Path("chatId") String chatId
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("chats/findChat")
    Observable<FindChatResponse> findChat(
            @Header("Authorization") String token,
            @Query("userId1") String userId1,
            @Query("userId2") String userId2
    );
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("chats/getMember")
    Observable<GetMemberRes> getMember(
            @Header("Authorization") String token,
            @Query("chatId") String chatId
    );

    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("chats/send")
    Observable<SendResponse> send(
            @Header("Authorization") String token,
            @Field("chatId") String chatId,
            @Field("content") String content,
            @Field("typeChat") String typeChat,
            @Field("typeMesage") String typeMesage
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("chats/send")
    Observable<SendResponse> createPriAndsend(
            @Header("Authorization") String token,
            @Field("receivedId") String receivedId,
            @Field("content") String content,
            @Field("typeChat") String typeChat,
            @Field("typeMesage") String typeMesage
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("chats/send")
    Observable<SendResponse> createGroupAndsend(
            @Header("Authorization") String token,
            @Field("member") List<String> member,
            @Field("name") String name,
            @Field("content") String content,
            @Field("typeChat") String typeChat,
            @Field("typeMesage") String typeMesage
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("users/show")
    Observable<EditProfileResponse> getMyProfile(
            @Header("Authorization") String token
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("users/show/{id}")
    Observable<EditProfileResponse> getProfile(
            @Header("Authorization") String token,
            @Path("id") String id
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/edit")
    Observable<EditProfileResponse> editAvatar(
            @Header("Authorization") String token,
            @Field("avatar") String avatar
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/edit")
    Observable<EditProfileResponse> editCoverImg(
            @Header("Authorization") String token,
            @Field("cover_image") String cover_image
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/edit")
    Observable<EditProfileResponse> editUsername(
            @Header("Authorization") String token,
            @Field("username") String username
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/edit")
    Observable<EditProfileResponse> editEmail(
            @Header("Authorization") String token,
            @Field("email") String email
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/edit")
    Observable<EditProfileResponse> editAddress(
            @Header("Authorization") String token,
            @Field("address") String address,
            @Field("country") String country,
            @Field("city") String city

    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json"})
    @POST("users/edit")
    Observable<EditProfileResponse> editGender(
            @Header("Authorization") String token,
            @Field("gender") String gender
    );
    @FormUrlEncoded
//    @Headers({ "Content-Type: application/json"})
    @POST("users/edit")
    Observable<EditProfileResponse> editBirthday(
            @Header("Authorization") String token,
            @Field("birthday") String birthday
    );
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @FormUrlEncoded
    @POST("users/change-password")
    Observable<ChangePassResponse> changePassword(
            @Header("Authorization") String token,
            @Field("currentPassword") String currentPassword,
            @Field("newPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("friends/get-status-friend")
    Observable<StatusFriendRes> getStatusFriend(
            @Header("Authorization") String token,
            @Field("receiver") String receiver
    );
    @FormUrlEncoded
    @POST("friends/list")
    Observable<ListFriendResponse> getListFriend(
            @Header("Authorization") String token,
            @Field("user_id") String user_id
    );
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("friends/get-requested-friend")
    Observable<ListReqRes> getRequestFriend(
            @Header("Authorization") String token
    );
    @POST("friends/get-send-request")
    Observable<ListReqRes> getSendRequestFriend(
            @Header("Authorization") String token
    );
    @FormUrlEncoded
    @POST("friends/delete-request")
    Observable<ChangePassResponse> delRequest(
            @Header("Authorization") String token,
            @Field("receiver") String receiver
    );

    @FormUrlEncoded
    @POST("friends/set-requested-friend")
    Observable<ChangePassResponse> setRequestFriend(
            @Header("Authorization") String token,
            @Field("user_id") String user_id
    );
    @FormUrlEncoded
    @POST("friends/set-accept")
    Observable<ChangePassResponse> setAccept(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("is_accept") String is_accept
    );
    @FormUrlEncoded
    @POST("friends/set-remove")
    Observable<ChangePassResponse> setRemove(
            @Header("Authorization") String token,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("users/search")
    Observable<SearchUserResponse> searchUser(
            @Header("Authorization") String token,
            @Field("keyword") String keyword
    );



}
