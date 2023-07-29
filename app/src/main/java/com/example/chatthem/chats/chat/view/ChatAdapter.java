package com.example.chatthem.chats.chat.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatthem.chats.model.Message;
import com.example.chatthem.databinding.ItemContainerReceivedMessageBinding;
import com.example.chatthem.databinding.ItemContainerSentMessageBinding;
import com.example.chatthem.utilities.Helpers;

import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    private  final List<Message> chatMessages;
    private final String senderId;
    private final String type;


    public ChatAdapter(List<Message> chatMessages, String senderId, String type) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position), position);

        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getUser().getId().equals(senderId)){
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public void addData() {
        notifyItemRangeInserted(chatMessages.size(),chatMessages.size());

    }

    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(Message chatMessage, int position) {
            binding.textMessage.setText(chatMessage.getContent());
            binding.textTime.setText(Helpers.formatTime(chatMessage.getUpdatedAt(),true));
            binding.getRoot().setOnClickListener(v->{
                if(binding.textTime.getVisibility() == View.VISIBLE){
                    binding.textTime.setVisibility(View.GONE);
                } else {
                    binding.textTime.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMessageBinding binding;

        public ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }


        void setData(Message chatMessage) {
            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(chatMessage.getUser().getAvatar()));
            binding.textMessage.setText(chatMessage.getContent());
            binding.textTime.setText(Helpers.formatTime(chatMessage.getUpdatedAt(), true));
            binding.getRoot().setOnClickListener(v->{
                if(binding.textTime.getVisibility() == View.VISIBLE){
                    binding.textTime.setVisibility(View.GONE);
                } else {
                    binding.textTime.setVisibility(View.VISIBLE);
                }
            });


            if (Objects.equals(type, "GROUP_CHAT")){
                binding.textName.setText(chatMessage.getUser().getUsername());
                binding.textName.setVisibility(View.VISIBLE);
            }
        }
    }
}
