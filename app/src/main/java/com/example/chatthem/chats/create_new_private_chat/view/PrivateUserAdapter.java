package com.example.chatthem.chats.create_new_private_chat.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatthem.chats.create_new_group_chat.view.GroupUserAdapter;
import com.example.chatthem.chats.create_new_private_chat.presenter.CreateChatPrivateContract;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.chats.presenter.ChatsContract;
import com.example.chatthem.databinding.ItemContainerRecentConversionBinding;
import com.example.chatthem.utilities.Helpers;

import java.util.List;
import java.util.Objects;

public class PrivateUserAdapter extends RecyclerView.Adapter<PrivateUserAdapter.ListUserViewHolder> {
    private List<UserModel> userModelList;
    private final CreateChatPrivateContract.ViewInterface viewInterface;


    public PrivateUserAdapter(List<UserModel> userModelList, CreateChatPrivateContract.ViewInterface viewInterface) {
        this.userModelList = userModelList;
        this.viewInterface = viewInterface;
    }

    public void reset(List<UserModel> userModels) {
        this.userModelList = userModels;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrivateUserAdapter.ListUserViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserViewHolder holder, int position) {
        holder.setData(userModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }



    class ListUserViewHolder extends RecyclerView.ViewHolder {

        ItemContainerRecentConversionBinding binding;

        public ListUserViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(UserModel userModel) {
            if (Objects.equals(userModel.getOnline(), "1")){
                binding.imageStatus.setColorFilter(Color.rgb(0,255,0));
            }else {
                binding.imageStatus.setColorFilter(Color.rgb(255,165,0));
            }

            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getUsername());
            binding.textRecentMessage.setText(userModel.getPhonenumber());
            binding.textTime.setVisibility(View.GONE);

            binding.getRoot().setOnClickListener(view -> {
                viewInterface.onUserClicked(userModel);
            });
        }
    }
}
