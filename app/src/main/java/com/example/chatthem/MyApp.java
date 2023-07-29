package com.example.chatthem;

import android.app.Application;

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
    }
}
