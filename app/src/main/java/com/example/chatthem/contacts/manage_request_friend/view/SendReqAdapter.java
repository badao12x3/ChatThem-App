package com.example.chatthem.contacts.manage_request_friend.view;

import static com.example.chatthem.utilities.Helpers.getBitmapFromEncodedString;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.contacts.manage_request_friend.presenter.SendReqPresenter;
import com.example.chatthem.databinding.ItemContainerRecentConversionBinding;
import com.example.chatthem.databinding.ItemSendReqFrieBinding;
import com.example.chatthem.utilities.Helpers;

import java.util.List;
import java.util.Objects;

public class SendReqAdapter extends RecyclerView.Adapter<SendReqAdapter.ViewHolder> {

    private List<UserModel> userModelList;
    private SendReqPresenter presenter;

    public SendReqAdapter(List<UserModel> userModelList, SendReqPresenter presenter) {
        this.userModelList = userModelList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSendReqFrieBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(userModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSendReqFrieBinding binding;

        public ViewHolder(ItemSendReqFrieBinding itemSendReqFrieBinding) {
            super(itemSendReqFrieBinding.getRoot());
            binding = itemSendReqFrieBinding;
        }

        void setData(UserModel userModel) {

            binding.imageProfile.setImageBitmap(getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getUsername());


            binding.remove.setOnClickListener(view -> {
            });
        }
    }
}
