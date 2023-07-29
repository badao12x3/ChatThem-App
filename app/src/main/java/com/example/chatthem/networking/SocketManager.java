package com.example.chatthem.networking;

import com.example.chatthem.utilities.Constants;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;

    private SocketManager() {
        // Khởi tạo Socket.IO object và kết nối với server
        try {
            IO.Options options = new IO.Options();
            // Cấu hình options nếu cần thiết (ví dụ: thiết lập URL server)
            socket = IO.socket(Constants.BASE_URL_SOCKET, options);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static synchronized SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }
}
