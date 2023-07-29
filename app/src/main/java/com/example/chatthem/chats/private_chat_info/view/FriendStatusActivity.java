package com.example.chatthem.chats.private_chat_info.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.chats.private_chat_info.model.StatusFriendRes;
import com.example.chatthem.chats.private_chat_info.presenter.Contract;
import com.example.chatthem.chats.private_chat_info.presenter.FriendStatusPresenter;
import com.example.chatthem.databinding.ActivityFriendStatusBinding;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.profile.model.EditProfileResponse;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FriendStatusActivity extends AppCompatActivity implements Contract.FriendStatusInterface {

    ActivityFriendStatusBinding binding;
    private PreferenceManager preferenceManager;
    private FriendStatusPresenter presenter;
    private UserModel me, you;
    private String status;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();
        preferenceManager = new PreferenceManager(this);
        presenter = new FriendStatusPresenter(this, "Bearer "+ preferenceManager.getString(Constants.KEY_TOKEN));
        String id = getIntent().getStringExtra("id");
        presenter.getStatusFriend( id);
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{onBackPressed();});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }

    @Override
    public void getStatusFriendSuccess() {
        me = presenter.getMe();
        you = presenter.getYou();
        status = presenter.getStatus();
        disposable = presenter.getDisposable();

        binding.textMe.setText(me.getUsername());
        binding.me.setImageBitmap(Helpers.getBitmapFromEncodedString(me.getAvatar()));

        binding.textYou.setText(you.getUsername());
        binding.you.setImageBitmap(Helpers.getBitmapFromEncodedString(you.getAvatar()));

        if (Objects.equals(status, "1")){
            binding.status.setText("Bạn bè");
            binding.sendReqFriend.setText("Xóa bạn");
        }else {
            binding.status.setText("Người lạ");
            binding.sendReqFriend.setText("Gửi kết bạn");
        }
    }

    @Override
    public void getStatusFriendFail() {
        Toast.makeText(getApplicationContext(), "Xảy ra lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
    }

}