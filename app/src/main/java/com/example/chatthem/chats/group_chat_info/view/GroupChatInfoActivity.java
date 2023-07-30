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
import com.example.chatthem.utilities.PreferenceManager;

import java.util.Objects;

public class GroupChatInfoActivity extends AppCompatActivity {

    ActivityGroupChatInfoBinding binding;

    private PreferenceManager preferenceManager;
    private Chat chat;
    private ChatNoLastMessObj chatNoLastMessObj;
    private String chatId;
    private String avatar;
    private String name;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListener();

    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());

        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
        chatNoLastMessObj = (ChatNoLastMessObj) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT_NO_LMSG);

        if (chat != null){
            binding.textName.setText(chat.getName());
            name = chat.getName();
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
            avatar = chat.getAvatar();
            chatId = chat.getId();
            isAdmin = Objects.equals(preferenceManager.getString(Constants.KEY_USED_ID), chat.getMember().get(chat.getMember().size() - 1));
        }else if (chatNoLastMessObj != null){
            binding.textName.setText(chatNoLastMessObj.getName());
            name = chatNoLastMessObj.getName();
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(chatNoLastMessObj.getAvatar()));
            avatar = chatNoLastMessObj.getAvatar();
            chatId = chatNoLastMessObj.getId();
            isAdmin = Objects.equals(preferenceManager.getString(Constants.KEY_USED_ID), chatNoLastMessObj.getMember().get(chatNoLastMessObj.getMember().size() - 1));
        }
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.btnManageMember.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), MemberActivity.class);
            it.putExtra("chatId", chatId);
            it.putExtra("isAdmin", isAdmin);
            startActivity(it);
        });
        binding.btnChangeInfo.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), ChangeInfoGroupActivity.class);
            it.putExtra("chatId", chatId);
            it.putExtra("name", name);
            it.putExtra("avatar", avatar);
            startActivity(it);
        });
    }
}