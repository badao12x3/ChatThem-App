package com.example.chatthem.contacts.send_request.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.chats.chat.view.ChatActivity;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.contacts.send_request.presenter.ProfileScanUserContract;
import com.example.chatthem.contacts.send_request.presenter.ProfileScanUserPresenter;
import com.example.chatthem.databinding.ActivityProfileScanUserBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.Objects;

public class ProfileScanUserActivity extends AppCompatActivity implements ProfileScanUserContract.ViewInterface {

    ActivityProfileScanUserBinding binding;

    ProfileScanUserPresenter presenter;
    PreferenceManager preferenceManager;

    UserModel you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileScanUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListener();
        presenter.getStatusFriend(getIntent().getStringExtra("userId"));
        binding.shimmerEffect.startShimmerAnimation();
    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new ProfileScanUserPresenter(this, preferenceManager.getString(Constants.KEY_TOKEN));

    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v-> onBackPressed());
        binding.addFriend.setOnClickListener(v->{
            presenter.setReqFriend(you.getId());
        });

        binding.btnAccept.setOnClickListener(v->{
            presenter.setAcceptFriend(you.getId(),"1");
        });

        binding.btnDecline.setOnClickListener(v->{
            presenter.setAcceptFriend(you.getId(),"2");
        });

        binding.btnCancel.setOnClickListener(v->{
            presenter.delReq(you.getId());
        });
        binding.btnChat.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, you);
            startActivity(intent);
        });

        binding.btnChat1.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, you);
            startActivity(intent);
        });

    }

    @Override
    public void getStatusFriendSuccess() {

        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.imageProfile.setVisibility(View.VISIBLE);
        binding.cardView2.setVisibility(View.VISIBLE);

        you =  presenter.getYou();
        if (you.getCover_image() == null){
            binding.coverImg.setImageResource(R.drawable.cover_img_placeholder);
        }else binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(you.getCover_image()));

        binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(you.getAvatar()));
        binding.textName.setText(you.getUsername());
        if (Objects.equals(presenter.getStatus(), "2") || Objects.equals(presenter.getStatus(), "3")||Objects.equals(presenter.getStatus(), "-1")){
            binding.before.setVisibility(View.VISIBLE);
        }else if (Objects.equals(presenter.getStatus(), "1")){
            binding.before.setVisibility(View.VISIBLE);
            binding.addFriend.setVisibility(View.GONE);
        }else {
            if (presenter.getSend()){
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.afterSend.setVisibility(View.VISIBLE);
            }else {
                binding.afterReceive.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getStatusFriendFail() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void setReqFriendSuccess() {
        binding.before.setVisibility(View.GONE);
        binding.textMessage.setVisibility(View.VISIBLE);
        binding.afterSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void setReqFriendFail() {
        Toast.makeText(this, "Gửi kết bạn thất bại, hãy thử lại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAcceptSuccess(String status) {
        binding.afterReceive.setVisibility(View.GONE);
        binding.before.setVisibility(View.VISIBLE);
        if (status == "1")
            binding.addFriend.setVisibility(View.GONE);
        else
            binding.addFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAcceptFail() {
        Toast.makeText(this, "Thao tác thất bại, hãy thử lại!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void delReqFail() {
        Toast.makeText(this, "Hủy yêu cầu thất bại, hãy thử lại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delReqSuccess() {
        binding.textMessage.setVisibility(View.GONE);
        binding.afterSend.setVisibility(View.GONE);
        binding.before.setVisibility(View.VISIBLE);

    }
}