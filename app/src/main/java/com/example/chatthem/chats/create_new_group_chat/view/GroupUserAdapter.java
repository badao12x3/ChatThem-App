package com.example.chatthem.chats.create_new_group_chat.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatthem.chats.create_new_group_chat.presenter.CreateGroupChatContract;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.ItemContainerRecentConversionBinding;
import com.example.chatthem.utilities.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.ListUserViewHolder> {
    private List<UserModel> userModelList;

    static List<UserModel> chosenList;
    private CreateGroupChatContract.ViewInterface viewInterface;

    public void reset(List<UserModel> userModelList){
        this.userModelList = userModelList;
        notifyDataSetChanged();
    }

    public GroupUserAdapter(List<UserModel> userModelList, CreateGroupChatContract.ViewInterface viewInterface) {
        this.userModelList = userModelList;
        this.viewInterface = viewInterface;
        chosenList = new ArrayList<>();
    }


    public List<UserModel> getChosenList() {
        return chosenList;
    }

    @NonNull
    @Override
    public ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListUserViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserViewHolder holder, int position) {
        holder.setData(userModelList.get(position), position);
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

        void setData(UserModel userModel, int pos) {
            if (Objects.equals(userModel.getOnline(), "1")){
                binding.imageStatus.setColorFilter(Color.rgb(0,255,0));
            }else {
                binding.imageStatus.setColorFilter(Color.rgb(255,165,0));
            }

            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getUsername());
            binding.textRecentMessage.setText(userModel.getPhonenumber());
            binding.textTime.setVisibility(View.GONE);
            binding.checkbox.setVisibility(View.VISIBLE);

            binding.checkbox.setOnCheckedChangeListener(null);
            binding.checkbox.setChecked(userModel.getChecked());
            binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    userModel.setChecked(isChecked);
                    if (isChecked){
                        if(!chosenList.contains(userModel))
                            chosenList.add(userModel);
                    }else {
                        chosenList.removeIf(u -> Objects.equals(u.getPhonenumber(), userModel.getPhonenumber()));

                    }



                }
            });



            binding.getRoot().setOnClickListener(view -> {

//                userModel.setChecked(!userModel.getChecked());
                binding.checkbox.setChecked(!userModel.getChecked());
                viewInterface.onUserClicked();

            });
        }
    }
}
