package com.example.chatthem.chats.chat.view;

import static com.example.chatthem.utilities.Helpers.encodeImage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.chats.chat.model.ChatNoLastMessObj;
import com.example.chatthem.chats.chat.presenter.ChatContract;
import com.example.chatthem.chats.chat.presenter.ChatPresenter;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.Message;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.ActivityChatBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity implements ChatContract.ViewInterface {

    private ActivityChatBinding binding;
    private ChatPresenter chatPresenter;
    private PreferenceManager preferenceManager;
    private Chat chat;
    private ChatNoLastMessObj chatNoLastMessObj;
    private UserModel userModel;

    private List<Message> messageList;
    private ChatAdapter chatAdapter;
    private boolean isSearchShow = false;

    private ArrayList<String> receivedList;
    private String group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(),this);

        init();
        setListener();



    }
    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatPresenter = new ChatPresenter(this, preferenceManager);

        messageList = new ArrayList<>();

        binding.shimmerEffect.startShimmerAnimation();

        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
        userModel = (UserModel) getIntent().getSerializableExtra(Constants.KEY_USER);
        if (chat != null){
            binding.textName.setText(chat.getName());
            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
            binding.textOnline.setText(Objects.equals(chat.getOnline(), "1") ? "Đang hoạt động": "Ngoại tuyến");
            binding.textOnline.setTextColor(Objects.equals(chat.getOnline(), "1") ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
            chatPresenter.getMessages(chat.getId());
        }else if (userModel != null){
            binding.textName.setText(userModel.getUsername());
            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textOnline.setText(Objects.equals(userModel.getOnline(), "1") ? "Đang hoạt động": "Ngoại tuyến");
            binding.textOnline.setTextColor(Objects.equals(userModel.getOnline(), "1") ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
            chatPresenter.findChat(userModel.getId());
        }else if (getIntent().getExtras()!=null){
            receivedList = getIntent().getExtras().getStringArrayList("create_group_chat");
            group_name = getIntent().getStringExtra(Constants.KEY_NAME);
            binding.textName.setText(group_name);
            binding.textOnline.setText("Ngoại tuyến");
            binding.textOnline.setTextColor( getResources().getColor(R.color.seed) );
            binding.shimmerEffect.stopShimmerAnimation();
            binding.shimmerEffect.setVisibility(View.GONE);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);

        }


    }
    private void setListener() {
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.searchBtn.setOnClickListener(v->{
            if (isSearchShow){
                binding.searchFrame.setVisibility(View.GONE);
            }else {
                binding.searchFrame.setVisibility(View.VISIBLE);
            }
            isSearchShow= !isSearchShow;
        });
        binding.imageInfo.setOnClickListener(v->{
//            if (chat!=null){
//
//            }else{
//                Toast.makeText(getApplicationContext(),"Nhóm chưa có thông tin")
//            }
//            Intent intent = new Intent(getApplicationContext(), );
        });
        binding.camBtn.setOnClickListener(v->{
            ImagePicker.with(this)
                    .cameraOnly()
                    .crop()//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(intent -> {
                        startForImageResult.launch(intent);
                        return null;
                    });
        });
        binding.galleryBtn.setOnClickListener(v->{
            ImagePicker.with(this)
                    .galleryOnly()
                    .crop()//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(intent -> {
                        startForImageResult.launch(intent);
                        return null;
                    });
        });
        binding.sendBtn.setOnClickListener(v->{

            String content = binding.inputMessage.getText().toString();
            if (chat!=null){
                chatPresenter.send(chat.getId(),content, chat.getType(),  Constants.KEY_TYPE_TEXT);
            }else if (userModel!=null){
                chatPresenter.createAndSendPrivate(userModel.getId(),content, Constants.KEY_PRIVATE_CHAT, Constants.KEY_TYPE_TEXT);
            }else if(receivedList != null){
                chatPresenter.createAndSendGroup(receivedList, group_name, content, Constants.KEY_GROUP_CHAT, Constants.KEY_TYPE_TEXT);
            }
            binding.inputMessage.setText("");
        });
        binding.sendImgBtn.setOnClickListener(v->{


        });
        binding.exitImg.setOnClickListener(v->{
            binding.containerText.setVisibility(View.VISIBLE);
            binding.containerImg.setVisibility(View.GONE);
        });
        binding.inputMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final Handler handler = new Handler();
                if (hasFocus) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TransitionManager.beginDelayedTransition(binding.containerInput, new AutoTransition());
                            binding.camBtn.setVisibility(View.GONE);
                            binding.galleryBtn.setVisibility(View.GONE);
                        }
                    }, 200);

                }
                else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TransitionManager.beginDelayedTransition(binding.containerInput, new AutoTransition());
                            binding.camBtn.setVisibility(View.VISIBLE);
                            binding.galleryBtn.setVisibility(View.VISIBLE);
                        }
                    }, 200);

                }
            }
        });
    }


    private final ActivityResultLauncher<Intent> startForImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    Uri resultUri = data.getData();

                    binding.imageContent.setImageURI(resultUri);
                    binding.containerText.setVisibility(View.GONE);
                    binding.containerImg.setVisibility(View.VISIBLE);

//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(resultUri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        encodedImage = encodeImage(bitmap);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                    Toast.makeText(getApplicationContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    public void onChatNotExist() {
//        chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), "PRIVATE_CHAT");
//        binding.usersRecyclerView.setAdapter(chatAdapter);
//        binding.usersRecyclerView.setVisibility(View.VISIBLE);
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
    }

    @Override
    public void onFindChatSucces() {
        chatNoLastMessObj = chatPresenter.getChatNoLastMessObj();
        chatPresenter.getMessages(chatNoLastMessObj.getId());
    }

    @Override
    public void onGetMessagesSuccess() {

        messageList = chatPresenter.getMessageList();
        if (chat!=null){
            chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chat.getType());
        }
        if (chatNoLastMessObj != null){
            chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chatNoLastMessObj.getType());

        }
        binding.usersRecyclerView.setAdapter(chatAdapter);
        binding.usersRecyclerView.setVisibility(View.VISIBLE);
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);

    }

    @Override
    public void onGetMessagesError() {
        Toast.makeText(getApplicationContext(),"Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFindChatError() {
        Toast.makeText(getApplicationContext(),"Lỗi khi tìm đoạn chat", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendError() {
        Toast.makeText(getApplicationContext(),"Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendSuccess() {
        chatNoLastMessObj = chatPresenter.getChatNoLastMessObj();
        chatPresenter.getMessages(chatNoLastMessObj.getId());
    }
}