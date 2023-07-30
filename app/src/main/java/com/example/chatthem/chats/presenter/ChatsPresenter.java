package com.example.chatthem.chats.presenter;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.ListChatResponse;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.networking.SocketManager;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatsPresenter {

    private final ChatsContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private Disposable disposable;
    private List<Chat> chatList;
    private List<UserModel> userModelList;
    private SocketManager socketManager ;
    private Socket socket;



    public ChatsPresenter(ChatsContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;

        //socket
        socketManager = SocketManager.getInstance();
        socket = socketManager.getSocket();
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public List<UserModel> getUserModelList() {
        return userModelList;
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
    public void registerOnMessageEvent(){
        // Đăng ký lắng nghe sự kiện
        if (socket != null) {
            socket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Xử lý dữ liệu nhận được từ sự kiện
                    viewInterface.receiveNewMsgRealtime();
                }
            });
        } else {
            // Xử lý trường hợp Socket.IO object là null
        }
    }
    public void registerOnCreateRoomEvent(){
        // Đăng ký lắng nghe sự kiện
        if (socket != null) {
            socket.on("createRoom", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Xử lý dữ liệu nhận được từ sự kiện
                    if (args.length > 0) {
                        JSONObject receivedData = (JSONObject) args[0];
                        try {
                            String room = receivedData.getString("room");
                            String typeRoom = receivedData.getString("typeRoom");
                            // Xử lý message nhận được từ sự kiện
                            viewInterface.onNewChatCreate();
                            joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), room, typeRoom, preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
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
    public void getMessaged(){
        APIServices.apiServices.getMessaged("Bearer " + preferenceManager.getString(Constants.KEY_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListChatResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull ListChatResponse listChatResponse) {
                        chatList = listChatResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onGetMessagedError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onGetMessagedSuccess();
                    }
                });

    }
}
