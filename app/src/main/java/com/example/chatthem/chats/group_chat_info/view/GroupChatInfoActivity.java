package com.example.chatthem.chats.group_chat_info.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatthem.R;
import com.example.chatthem.chats.chat.model.ChatNoLastMessObj;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.databinding.ActivityGroupChatInfoBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;

public class GroupChatInfoActivity extends AppCompatActivity {

    ActivityGroupChatInfoBinding binding;
    private Chat chat;
    private ChatNoLastMessObj chatNoLastMessObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatInfoBinding.inflate(getLayoutInflater());
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
        }else if (chatNoLastMessObj != null){
            binding.textName.setText(chatNoLastMessObj.getName());
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(chatNoLastMessObj.getAvatar()));
        }
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.btnManageMember.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), MemberActivity.class);
            startActivity(it);
        });
        binding.btnChangeInfo.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), ChangeInfoGroupActivity.class);
            startActivity(it);
        });
    }
}