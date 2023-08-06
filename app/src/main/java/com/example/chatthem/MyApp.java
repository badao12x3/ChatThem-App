package com.example.chatthem;

import static com.example.chatthem.firebase.MessagingService.channelId;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.chatthem.networking.SocketManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.net.Socket;
import java.security.Security;

public class MyApp extends Application {
    static {
        Security.removeProvider("BC");
        // Confirm that positioning this provider at the end works for your needs!
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo Socket.IO object khi ứng dụng được chạy
        SocketManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Chat Message";
            String channelDescription = "This notification channel is used for chat message notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Security.removeProvider("BC");
            Security.addProvider(new BouncyCastleProvider());

        }
    }
}
