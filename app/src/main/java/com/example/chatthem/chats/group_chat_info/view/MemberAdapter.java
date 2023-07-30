package com.example.chatthem.chats.group_chat_info.view;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.ItemMemberBinding;
import com.example.chatthem.utilities.Helpers;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{

    List<UserModel> userModelList;
    boolean isAdmin ;

    public MemberAdapter(List<UserModel> userModelList, boolean isAdmin) {
        this.userModelList = userModelList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMemberBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(userModelList.get(position));
        if (position == userModelList.size() - 1)
            holder.binding.title.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemMemberBinding binding;

        public ViewHolder(ItemMemberBinding itemMemberBinding) {
            super(itemMemberBinding.getRoot());
            binding = itemMemberBinding;
        }

        void setData(UserModel userModel){
            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getUsername());
            if (!isAdmin) {
                binding.btnDelete.setVisibility(View.GONE);
                return;
            }
            binding.btnDelete.setOnClickListener(v -> {

            });
        }

    }
}
