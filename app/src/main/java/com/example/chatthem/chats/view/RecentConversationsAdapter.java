package com.example.chatthem.chats.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatthem.authentication.model.User;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.presenter.ChatsContract;
import com.example.chatthem.databinding.ItemContainerRecentConversionBinding;
import com.example.chatthem.utilities.Helpers;

import java.util.List;
import java.util.Objects;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversiongViewHolder>{

    private List<Chat> chatMessages;
    private final ChatsContract.ViewInterface conversionListener;

    public RecentConversationsAdapter(List<Chat> chatMessages, ChatsContract.ViewInterface conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public RecentConversationsAdapter.ConversiongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversiongViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationsAdapter.ConversiongViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void reset(List<Chat> conversations) {
        chatMessages = conversations;
        notifyDataSetChanged();
    }

    class ConversiongViewHolder extends RecyclerView.ViewHolder {

        ItemContainerRecentConversionBinding binding;

        public ConversiongViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(Chat chatMessage) {
            if (Objects.equals(chatMessage.getOnline(), "1")){
                binding.imageStatus.setColorFilter(Color.rgb(0,255,0));
            }else {
                binding.imageStatus.setColorFilter(Color.rgb(255,165,0));
            }

            binding.imageProfile.setImageBitmap(getConversionImage(chatMessage.getAvatar()));
            binding.textName.setText(chatMessage.getName());
            binding.textRecentMessage.setText(chatMessage.getLastMessage().getContent());
            binding.textTime.setText(Helpers.formatTime(chatMessage.getLastMessage().getUpdatedAt(),false));

            binding.getRoot().setOnClickListener(view -> {
                conversionListener.onConversionClicked(chatMessage);
            });
        }
    }

    private Bitmap getConversionImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }
}
