package com.example.chatthem.chats.private_chat_info.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.chatthem.R;
import com.example.chatthem.chats.chat.model.ChatNoLastMessObj;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.databinding.ActivityPrivateChatInfoBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;

public class PrivateChatInfoActivity extends AppCompatActivity {

    ActivityPrivateChatInfoBinding binding;
    private Chat chat;
    private ChatNoLastMessObj chatNoLastMessObj;
    private String receiveId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateChatInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListener();

    }
    private void init(){
        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
        chatNoLastMessObj = (ChatNoLastMessObj) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT_NO_LMSG);

        if (chat != null){
            binding.textName.setText(chat.getName());
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
            receiveId = chat.getReceivedID();
        }else if (chatNoLastMessObj != null){
            binding.textName.setText(chatNoLastMessObj.getName());
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(chatNoLastMessObj.getAvatar()));
            receiveId = chatNoLastMessObj.getMember().get(0);
        }
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.btnDetailProfile.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), ProfileDetailActivity.class);
            if (receiveId == null)
                Log.d("PrivateChatInfoActivity  + btnDetailProfile", "null");
            it.putExtra("id", receiveId);
            startActivity(it);
        });

        binding.statusFriend.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), FriendStatusActivity.class);
            if (receiveId == null)
                Log.d("PrivateChatInfoActivity statusFriend", "null");
            it.putExtra("id", receiveId);
            startActivity(it);
        });
    }

}