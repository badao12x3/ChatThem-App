package com.example.chatthem.utilities;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final String KEY_PREFERENCE_NAME = "ChatThem";
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS_CITY = "city";
    public static final String KEY_ADDRESS_COUNTRY = "country";
    public static final String KEY_ADDRESS_DETAIL = "address";
    public static final String KEY_PHONE = "phonenumber";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USED_ID = "userId";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_COVERIMAGE = "coverImage";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER = "user";
    public static final String KEY_ROOM = "room";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_COLLECTION_CHAT_NO_LMSG = "chatNoLastMsg";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String MESS_SENDER_ID = "messSenderId";
    public static final String MESS_RECEIVER_ID = "messReceiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String isSeen = "isSeen";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";

    public static final String KEY_CONVERSATION_ID = "conversationID_it";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-type";

    public static final String REMOTE_MSG_DATA = "data";
    public static final String TYPE_MESSAGES_SEND = "type";
//    public static final List<UserGroup> userGroups = new ArrayList<>();
//    public static final UserGroup userCurrent = new UserGroup();
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remotoMsgHeaders = null;

    public static final String KEY_PUBLIC_KEY = "publicKey";
    public static final String KEY_RECEIVER_PUBLIC_KEY = "receiverPublicKey";
    public static final String KEY_SENDER_PUBLIC_KEY = "senderPublicKey";

    public static final String KEY_PRIVATE_KEY = "privateKey";
    public static final String KEY_PRIVATE_CHAT = "PRIVATE_CHAT";
    public static final String KEY_GROUP_CHAT = "GROUP_CHAT";
    public static final String KEY_TYPE_TEXT = "TEXT";
    public static final String KEY_TYPE_IMAGE = "IMAGE";
    public static final String BASE_URL = "http://192.168.1.41:8000/api/v1/";
    public static final String BASE_URL_SOCKET = "http://192.168.1.41:16000";

    public static HashMap<String, String> getRemoteMsgHeaders(){
        if(remotoMsgHeaders == null){
            remotoMsgHeaders = new HashMap<>();
            remotoMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAANdlZ-WY:APA91bFJJ0vstI2-8E-OQeelSbg45jjWBrUT4vyVeGCb1-nEaqjqMuCspO0rBPL-e5EmbS9gD0ybmXfFyr4VUb6lnPgz0b1LcMYDZMF68D8KTaL4jDrIkLaNlayJ7Pj0oOYUAJIc7N1m"
            );
            remotoMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remotoMsgHeaders;
    }


}
