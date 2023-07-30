package com.example.chatthem.chats.group_chat_info.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatthem.R;
import com.example.chatthem.chats.group_chat_info.model.GetMemberRes;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.ActivityMemberBinding;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MemberActivity extends AppCompatActivity {

    ActivityMemberBinding binding;
    private PreferenceManager preferenceManager;
    private boolean isAdmin;
    private String chatId;
    private List<UserModel> userModelList;
    private MemberAdapter adapter;
    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListener();

    }
    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        userModelList = new ArrayList<>();
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        chatId = getIntent().getStringExtra("chatId");

        getMember();

    }

    private void getMember(){
        APIServices.apiServices.getMember("Bearer " + preferenceManager.getString(Constants.KEY_TOKEN), chatId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetMemberRes>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull GetMemberRes getMemberRes) {
                        userModelList = getMemberRes.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.swipeLayout.setRefreshing(false);
                        binding.textErrorMessage.setText("Lỗi khi lấy dữ liệu, vui lòng thử lại!");
                        binding.textErrorMessage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        binding.swipeLayout.setRefreshing(false);
                        binding.textErrorMessage.setVisibility(View.GONE);
                        adapter = new MemberAdapter(userModelList,isAdmin);
                        binding.recyclerview.setAdapter(adapter);
                    }
                });
    }
    private void setListener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Xử lý logic khi làm mới dữ liệu, ví dụ tải dữ liệu mới
                getMember();
            }
        });
        binding.addMem.setOnClickListener(v -> {
            Intent it = new Intent(getApplicationContext(), AddMemActivity.class);
            startActivity(it);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
    }
}