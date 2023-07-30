package com.example.chatthem.chats.chat.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.chats.chat.model.ChatNoLastMessObj;
import com.example.chatthem.chats.chat.presenter.ChatContract;
import com.example.chatthem.chats.chat.presenter.ChatPresenter;
import com.example.chatthem.chats.group_chat_info.view.GroupChatInfoActivity;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.Message;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.chats.private_chat_info.view.PrivateChatInfoActivity;
import com.example.chatthem.databinding.ActivityChatBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;

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

        chatPresenter.registerOnMessageEvent();
        messageList = new ArrayList<>();

        binding.shimmerEffect.startShimmerAnimation();

        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
        userModel = (UserModel) getIntent().getSerializableExtra(Constants.KEY_USER);
        if (chat != null){
//            chatPresenter.joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), chat.getId(), chat.getType(), preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
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
            Intent it;

            if (chat!=null ){
                if (Objects.equals(chat.getType(), Constants.KEY_PRIVATE_CHAT)){
                    it = new Intent(getApplicationContext(), PrivateChatInfoActivity.class);
                }else {
                    it = new Intent(getApplicationContext(), GroupChatInfoActivity.class);
                }
                it.putExtra(Constants.KEY_COLLECTION_CHAT, chat);
                startActivity(it);
            }else if(chatNoLastMessObj != null){
                if (Objects.equals(chatNoLastMessObj.getType(), Constants.KEY_PRIVATE_CHAT)){
                    it = new Intent(getApplicationContext(), PrivateChatInfoActivity.class);
                }else {
                    it = new Intent(getApplicationContext(), GroupChatInfoActivity.class);
                }
                it.putExtra(Constants.KEY_COLLECTION_CHAT_NO_LMSG, chatNoLastMessObj);
                startActivity(it);
            }else{
                Toast.makeText(getApplicationContext(),"Hãy gửi tin nhắn đầu tiên để xem thông tin", Toast.LENGTH_SHORT).show();
            }

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
            String content = binding.inputMessage.getText().toString().trim();
            if (content.isEmpty()){
                return;
            }
            if (chat!=null){
                chatPresenter.send(chat.getId(),content, chat.getType(),  Constants.KEY_TYPE_TEXT);
            }else if (chatNoLastMessObj != null){
                chatPresenter.send(chatNoLastMessObj.getId(),content, chatNoLastMessObj.getType(),  Constants.KEY_TYPE_TEXT);
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
//        chatPresenter.joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), chatNoLastMessObj.getId(), chatNoLastMessObj.getType(), preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
        chatPresenter.getMessages(chatNoLastMessObj.getId());
    }
    @Override
    public void onFindChatError() {
        Toast.makeText(getApplicationContext(),"Lỗi khi tìm đoạn chat", Toast.LENGTH_SHORT).show();
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
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.usersRecyclerView.setAdapter(chatAdapter);
        binding.usersRecyclerView.setVisibility(View.VISIBLE);
        binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // call the invalidate()
//                binding.usersRecyclerView.smoothScrollToPosition(0);
//            }
//        });

    }

    @Override
    public void onGetMessagesError() {
        Toast.makeText(getApplicationContext(),"Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onSendError() {
        Toast.makeText(getApplicationContext(),"Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendSuccess(String content, String typeMess) {
        chatNoLastMessObj = chatPresenter.getChatNoLastMessObj();
//        chatPresenter.getMessages(chatNoLastMessObj.getId());
        chatPresenter.sendRealtime(content,typeMess, chatNoLastMessObj.getId());
    }

    @Override
    public void onCreateAndSendSuccess(String content, String typeMess) {

        chatNoLastMessObj = chatPresenter.getChatNoLastMessObj();
        chatPresenter.getMessages(chatNoLastMessObj.getId());
        chatPresenter.joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), chatNoLastMessObj.getId(), chatNoLastMessObj.getType(), preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
        chatPresenter.createChat(chatNoLastMessObj.getId(),chatNoLastMessObj.getType());
        chatPresenter.sendRealtime(content,typeMess, chatNoLastMessObj.getId());

    }

    @Override
    public void receiveNewMsgRealtime(String userId,String username , String avatar,String room,String typeRoom,String publicKey,String text,String typeMess, String time) {
        UserModel userModel1 = new UserModel(userId, username, avatar, publicKey);
        Message message = new Message(room,userModel1,text,typeMess,time);
        messageList.add(message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // call the invalidate()
                chatAdapter.notifyItemRangeInserted(messageList.size(),messageList.size());
                binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            }
        });
//        chatAdapter.addData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(chat!= null){
//            chatPresenter.leaveChat(preferenceManager.getString(Constants.KEY_NAME),chat.getId());
//        }else if ( chatNoLastMessObj != null)
//            chatPresenter.leaveChat(preferenceManager.getString(Constants.KEY_NAME),chatNoLastMessObj.getId());
    }
}