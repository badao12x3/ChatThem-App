package com.example.chatthem.chats.create_new_private_chat.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import com.example.chatthem.R;
import com.example.chatthem.chats.chat.view.ChatActivity;
import com.example.chatthem.chats.create_new_group_chat.presenter.CreateGroupChatPresenter;
import com.example.chatthem.chats.create_new_group_chat.view.GroupUserAdapter;
import com.example.chatthem.chats.create_new_private_chat.presenter.CreateChatPrivateContract;
import com.example.chatthem.chats.create_new_private_chat.presenter.CreateChatPrivatePresenter;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.ActivityCreateGroupChatBinding;
import com.example.chatthem.databinding.ActivityCreatePrivateChatBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreatePrivateChatActivity extends AppCompatActivity implements CreateChatPrivateContract.ViewInterface {
    private ActivityCreatePrivateChatBinding binding;
    private PreferenceManager preferenceManager;
    private CreateChatPrivatePresenter createChatPrivatePresenter;

    private List<UserModel> userModelList;

    private PrivateUserAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePrivateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);
        setupTitle();

        binding.shimmerEffect.setVisibility(View.VISIBLE);
        binding.shimmerEffect.startShimmerAnimation();
        binding.usersRecyclerView.setVisibility(View.GONE);

        preferenceManager = new PreferenceManager(this);
        createChatPrivatePresenter = new CreateChatPrivatePresenter(this, preferenceManager);

        userModelList = new ArrayList<>();
        adapter = new PrivateUserAdapter(userModelList,this);

        binding.usersRecyclerView.setAdapter(adapter);

        createChatPrivatePresenter.getListFriend(null);

        setListener();
    }

    public void setupTitle(){
        binding.textView.setText(getIntent().getStringExtra("title"));
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    createChatPrivatePresenter.searchUser(binding.searchView.getQuery().toString());
                    binding.shimmerEffect.startShimmerAnimation();
                    binding.shimmerEffect.setVisibility(View.VISIBLE);
                    binding.usersRecyclerView.setVisibility(View.GONE);
                    binding.textErrorMessage.setVisibility(View.GONE);
                }else{
                    adapter.reset(userModelList);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(!TextUtils.isEmpty(newText.trim())){
                    List<UserModel> userModels = Helpers.checkStringContain(newText, userModelList);
                    adapter.reset(userModels);
                }else{
                    adapter.reset(userModelList);

                }
                return false;
            }
        });
    }

    @Override
    public void onUserClicked(UserModel userModel) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, userModel);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSearchUserError() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.textErrorMessage.setText("Tìm kiếm gặp lỗi");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_error);
        binding.textErrorMessage.setTextColor(colorEror);
    }

    @Override
    public void onSearchUserSuccess() {
        List<UserModel> list = createChatPrivatePresenter.getUserModelList();
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        if (list.size() != 0){
            adapter.reset(list);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Không tìm thấy dữ liệu!");
        }
    }

    @Override
    public void onGetListFriendError() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.textErrorMessage.setText("Tải danh sách bạn bè gặp lỗi");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_error);
        binding.textErrorMessage.setTextColor(colorEror);
    }

    @Override
    public void onGetListFriendSuccess() {
        List<UserModel> list = createChatPrivatePresenter.getUserModelList();
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        if (list.size() != 0){
            adapter.reset(list);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Danh sách bạn bè trống!");
        }
    }
}