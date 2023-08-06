package com.example.chatthem.firebase;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chatthem.MainActivity;
import com.example.chatthem.R;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.chats.chat.view.ChatActivity;
import com.example.chatthem.cryptophy.ECCc;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.profile.model.EditProfileResponse;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.crypto.SecretKey;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessagingService extends FirebaseMessagingService {
    public static String channelId = "chat_message";
    private PreferenceManager preferenceManager;

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM - HP", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token) {

        preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        APIServices.apiServices.updateFCMToken("Bearer " + preferenceManager.getString(Constants.KEY_TOKEN), token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull EditProfileResponse editProfileResponse) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(MessagingService.this, "FCM token thay đổi! Cập nhật FCM token lên server thất bại", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MessagingService.this, "FCM token thay đổi! Cập nhật FCM token lên server thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> data = message.getData();

        // Kiểm tra các trường dữ liệu tùy chỉnh để phân biệt các loại thông báo
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, ChatActivity.class);
        resultIntent.putExtra("ChatId", data.get("ChatId"));
        resultIntent.putExtra(Constants.KEY_SENDER_NAME, data.get(Constants.KEY_SENDER_NAME));
        resultIntent.putExtra(Constants.KEY_NAME, data.get(Constants.KEY_NAME));
        resultIntent.putExtra(Constants.KEY_FCM_TOKEN, data.get(Constants.KEY_FCM_TOKEN));
        resultIntent.putExtra(Constants.KEY_MESSAGE, data.get(Constants.KEY_MESSAGE));
        resultIntent.putExtra(Constants.TYPE_MESSAGES_SEND, data.get(Constants.TYPE_MESSAGES_SEND));
        resultIntent.putExtra(Constants.KEY_PUBLIC_KEY, data.get(Constants.KEY_PUBLIC_KEY));
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(data.get(Constants.KEY_NAME));


        int notificationId = new Random().nextInt();
        String plainMess = "";
        if (data.containsKey("TypeChat")) {
            String notificationType = data.get("TypeChat");
            if (Objects.equals(notificationType, Constants.KEY_GROUP_CHAT)) {

                if (Objects.equals(data.get(Constants.TYPE_MESSAGES_SEND), Constants.KEY_TYPE_TEXT)){
                    plainMess =  data.get(Constants.KEY_MESSAGE);
                }else{
                    plainMess = "Hình ảnh";
                }
                builder.setContentText(data.get(Constants.KEY_SENDER_NAME) +": "+ plainMess) ;

            } else if (Objects.equals(notificationType, Constants.KEY_PRIVATE_CHAT)) {
                //Giải mã văn bản

                if (Objects.equals(data.get(Constants.TYPE_MESSAGES_SEND), Constants.KEY_TYPE_TEXT)){
                    try {
                        String priKeyStr = new PreferenceManager(getApplicationContext()).getString(Constants.KEY_PRIVATE_KEY);
                        PrivateKey priKey = ECCc.stringToPrivateKey(priKeyStr);
                        PublicKey pubKey = ECCc.stringToPublicKey(data.get(Constants.KEY_PUBLIC_KEY));
                        SecretKey secretKey = ECCc.generateSharedSecret(priKey, pubKey);
                        plainMess = ECCc.decryptString(secretKey,message.getData().get(Constants.KEY_MESSAGE));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    builder.setContentText(plainMess) ;
                }else{
                    plainMess = "Hình ảnh";
                }
            }
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                    plainMess
            ));
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setContentIntent(resultPendingIntent);
            builder.setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, builder.build());
        }


    }
}
