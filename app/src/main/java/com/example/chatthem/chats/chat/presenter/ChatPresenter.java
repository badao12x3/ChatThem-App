package com.example.chatthem.chats.chat.presenter;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;

import com.example.chatthem.authentication.model.LoginResponse;
import com.example.chatthem.chats.chat.model.ChatNoLastMessObj;
import com.example.chatthem.chats.chat.model.FindChatResponse;
import com.example.chatthem.chats.chat.model.ListMessagesResponse;
import com.example.chatthem.chats.chat.model.SendResponse;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.Message;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.networking.SocketManager;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.HttpException;

public class ChatPresenter {

    private final ChatContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private List<Disposable> disposables;
    private List<Message> messageList;
    private Chat chat;
    private ChatNoLastMessObj chatNoLastMessObj;
    private String token;
    private SocketManager socketManager ;
    private Socket socket;

    public ChatPresenter(ChatContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        disposables = new ArrayList<>();
        token = "Bearer " + preferenceManager.getString(Constants.KEY_TOKEN);
        //socket
        socketManager = SocketManager.getInstance();
        socket = socketManager.getSocket();

    }

    public void joinChat(String userId, String username,String avatar, String chatId, String typeRoom, String publicKey){
        // Gửi emit
        JSONObject data = new JSONObject();
        try {
            data.put("userId", userId);
            data.put("username", username);
            data.put("avatar", avatar);
            data.put("typeRoom", typeRoom);
            data.put("publicKey", publicKey);
            data.put("room", chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            socket.emit("joinRoom", data);
        } else {
            // Xử lý trường hợp Socket.IO object là null
        }
    }
    public void createChat(String chatId, String typeRoom){
        // Gửi emit
        JSONObject data = new JSONObject();
        try {
            data.put("room", chatId);
            data.put("typeRoom", typeRoom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            socket.emit("createRoom", data);
        } else {
            // Xử lý trường hợp Socket.IO object là null
        }
    }
    public void registerOnMessageEvent(){
        // Đăng ký lắng nghe sự kiện
        if (socket != null) {
            socket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Xử lý dữ liệu nhận được từ sự kiện
                    if (args.length > 0) {
                        JSONObject receivedData = (JSONObject) args[0];
                        try {
                            String room = receivedData.getString("room");
                            String username = receivedData.getString("username");
                            String text = receivedData.getString("text");
                            String time = receivedData.getString("time");
                            String userId = receivedData.getString("userId");
                            String avatar = receivedData.getString("avatar");
                            String typeRoom = receivedData.getString("typeRoom");
                            String publicKey = receivedData.getString("publicKey");
                            String typeMess = receivedData.getString("typeMess");
                            // Xử lý message nhận được từ sự kiện

                            viewInterface.receiveNewMsgRealtime(userId, username, avatar, room, typeRoom, publicKey, text, typeMess, time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            // Xử lý trường hợp Socket.IO object là null
        }
    }
    public void leaveChat(String username, String chatId){
        // Gửi emit
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("room", chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            socket.emit("leaveRoom", data);
        } else {
            // Xử lý trường hợp Socket.IO object là null
        }
    }
    public List<Disposable> getDisposable() {
        return disposables;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public Chat getChat() {
        return chat;
    }

    public ChatNoLastMessObj getChatNoLastMessObj() {
        return chatNoLastMessObj;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void getMessages(String chatId){
        APIServices.apiServices.getMessages(token,chatId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListMessagesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add( d);
                    }

                    @Override
                    public void onNext(@NonNull ListMessagesResponse listMessagesResponse) {
                        messageList = listMessagesResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onGetMessagesError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onGetMessagesSuccess();
                    }
                });
    }

    public void findChat(String userId) {
        APIServices.apiServices.findChat(token, userId, preferenceManager.getString(Constants.KEY_USED_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FindChatResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add( d);
                    }

                    @Override
                    public void onNext(@NonNull FindChatResponse findChatResponse) {
                        chatNoLastMessObj = findChatResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;

                            try {
                                if (httpException.response() != null && httpException.response().errorBody() != null) {
                                    String error = httpException.response().errorBody().string();

                                    FindChatResponse findChatResponse = new Gson().fromJson(error, FindChatResponse.class);
                                    if (Objects.equals(findChatResponse.getCode(), "9992")) viewInterface.onChatNotExist();

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

                        }else viewInterface.onFindChatError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onFindChatSucces();
                    }
                });
    }

    public void createAndSendPrivate(String receiveId, String content,String typeChat, String typeMess) {
        APIServices.apiServices.createPriAndsend(token,receiveId,content,typeChat,typeMess)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add( d);
                    }

                    @Override
                    public void onNext(@NonNull SendResponse sendResponse) {
                        chatNoLastMessObj = sendResponse.getData().getChat();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();

                        viewInterface.onSendError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onCreateAndSendSuccess(content, typeMess);
                    }
                });
    }

    public void send(String receiveId, String content,String typeChat, String typeMess) {
        APIServices.apiServices.send(token,receiveId,content,typeChat,typeMess)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull SendResponse sendResponse) {
                        chatNoLastMessObj = sendResponse.getData().getChat();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        viewInterface.onSendError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onSendSuccess(content, typeMess);

                    }
                });

    }
    public void createAndSendGroup(List<String> member, String name,String content, String typeChat, String typeMess) {
        APIServices.apiServices.createGroupAndsend(token,member,name,content,typeChat, typeMess)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull SendResponse sendResponse) {
                        chatNoLastMessObj = sendResponse.getData().getChat();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();

                        viewInterface.onSendError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onCreateAndSendSuccess(content, typeMess);

                    }
                });
    }

    public void sendRealtime(String content,String typeMess, String room) {
        // Gửi emit
        JSONObject data = new JSONObject();
        try {
            data.put("msg", content);
            data.put("typeMess", typeMess);
            data.put("room", room);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            socket.emit("chatMessage", data);
        } else {
            // Xử lý trường hợp Socket.IO object là null
        }
    }
}
