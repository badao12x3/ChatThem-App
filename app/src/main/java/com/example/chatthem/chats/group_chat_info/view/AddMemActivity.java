package com.example.chatthem.chats.group_chat_info.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SearchView;

import com.example.chatthem.R;
import com.example.chatthem.chats.chat.view.ChatActivity;
import com.example.chatthem.chats.create_new_group_chat.presenter.CreateGroupChatPresenter;
import com.example.chatthem.chats.create_new_group_chat.view.GroupUserAdapter;
import com.example.chatthem.chats.group_chat_info.presenter.AddMemContract;
import com.example.chatthem.chats.group_chat_info.presenter.AddMemPresenter;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.ActivityAddMemBinding;
import com.example.chatthem.databinding.ActivityCreateGroupChatBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;

public class AddMemActivity extends AppCompatActivity implements AddMemContract.ViewInterface {


    private ActivityAddMemBinding binding;
    private PreferenceManager preferenceManager;
    private AddMemPresenter addMemPresenter;
    private List<UserModel> chosenList;
    private List<UserModel> userModelList;
    private AddMemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);


        binding.shimmerEffect.setVisibility(View.VISIBLE);
        binding.shimmerEffect.startShimmerAnimation();
        binding.usersRecyclerView.setVisibility(View.GONE);


        preferenceManager = new PreferenceManager(this);
        addMemPresenter = new AddMemPresenter(this, preferenceManager);

        addMemPresenter.getListFriend(null);

        chosenList = new ArrayList<>();
        userModelList = new ArrayList<>();
//        for (int i = 0; i < 20; i++){
//            UserModel u = new UserModel(i+"","name" + i);
//            userModelList.add(u);
//        }
        adapter = new AddMemAdapter(userModelList,this);

        binding.usersRecyclerView.setAdapter(adapter);


        setListener();

    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.btnAdd.setOnClickListener(v->{
            chosenList = adapter.getChosenList();

            List<String> list = new ArrayList<>();
            for (UserModel u : chosenList){
                list.add(u.getId());
            }

        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    addMemPresenter.searchUser(binding.searchView.getQuery().toString());
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

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Xử lý logic khi làm mới dữ liệu, ví dụ tải dữ liệu mới
                binding.shimmerEffect.startShimmerAnimation();
                binding.shimmerEffect.setVisibility(View.VISIBLE);
                addMemPresenter.getListFriend(null);
            }
        });
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
        List<UserModel> list = addMemPresenter.getUserModelList();
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        if (list.size() != 0){
            List<UserModel> chosen_list = adapter.getChosenList();
            int cnt = chosen_list.size();
            for (UserModel u: list) {
                for (UserModel c: chosen_list) {
                    if (Objects.equals(u.getPhonenumber(), c.getPhonenumber())){
                        u.setChecked(true);
                        cnt --;
                        break;
                    }
                }
                if (cnt <= 0){
                    break;
                }
            }

            adapter.reset(list);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Không tìm thấy dữ liệu!");
        }

    }

    @Override
    public void onGetListFriendSuccess() {

        userModelList = addMemPresenter.getUserModelList();
        userModelList.removeAll(userModelList);
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        if (userModelList.size() != 0){
            adapter.reset(userModelList);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Danh sách bạn bè trống!");
        }
    }

    @Override
    public void onGetListFriendError() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.swipeLayout.setRefreshing(false);
        binding.textErrorMessage.setText("Tải danh sách bạn bè gặp lỗi");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_error);
        binding.textErrorMessage.setTextColor(colorEror);
    }

    @Override
    public void onUserClicked() {
        binding.btnAdd.setEnabled(adapter.getChosenList().size() > 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addMemPresenter.getDisposables() != null) {
            for (Disposable d: addMemPresenter.getDisposables()){
                d.dispose();
            }
        }

    }
}