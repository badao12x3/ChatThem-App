package com.example.chatthem.chats.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.chatthem.R;
import com.example.chatthem.chats.chat.view.ChatActivity;
import com.example.chatthem.chats.create_new_group_chat.view.CreateGroupChatActivity;
import com.example.chatthem.chats.create_new_private_chat.view.CreatePrivateChatActivity;
import com.example.chatthem.chats.model.Chat;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.chats.presenter.ChatsContract;
import com.example.chatthem.databinding.FragmentChatsBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment implements ChatsContract.ViewInterface {

    private boolean isExpanded = false;

    private FragmentChatsBinding binding;

    private PreferenceManager preferenceManager;
    private Animation fromBottomFabAnim;
    private Animation toBottomFabAnim;
    private Animation rotateClockWiseFabAnim;
    private Animation rotateAntiClockWiseFabAnim;
    private Animation fromBottomBgAnim;
    private Animation toBottomBgAnim;
    private List<Chat> conversations;
    private RecentConversationsAdapter conversationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater,container,false);
        View rootView = binding.getRoot();
        init();
        setListener();

        return rootView;
    }

    private void init(){
        preferenceManager = new PreferenceManager(requireContext());
        if (fromBottomFabAnim == null){
            fromBottomFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_fab);
        }
        if (toBottomFabAnim == null){
            toBottomFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_fab);
        }
        if (fromBottomBgAnim == null){
            fromBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim);
        }
        if (toBottomBgAnim == null){
            toBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim);
        }
        if (rotateAntiClockWiseFabAnim == null){
            rotateAntiClockWiseFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clock_wise);
        }
        if (rotateClockWiseFabAnim == null){
            rotateClockWiseFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clock_wise);
        }

    }

    private void setListener(){
        binding.mainFabBtn.setOnClickListener(v->{
            if (isExpanded) {
                shrinkFab();
            } else {
                expandFab();
            }
        });
        binding.galleryFabBtn.setOnClickListener(v->{
            Intent it = new Intent(requireContext(), CreateGroupChatActivity.class);
            startActivity(it);
        });

        binding.shareFabBtn.setOnClickListener(v->{
            Intent it = new Intent(requireContext(), CreatePrivateChatActivity.class);
            startActivity(it);
        });

    }

    private void expandFab() {
        binding.transparentBg.startAnimation(fromBottomBgAnim);

        binding.mainFabBtn.startAnimation(rotateClockWiseFabAnim);
        binding.galleryFabBtn.startAnimation(fromBottomFabAnim);
        binding.shareFabBtn.startAnimation(fromBottomFabAnim);
        binding.galleryTv.startAnimation(fromBottomFabAnim);
        binding.shareTv.startAnimation(fromBottomFabAnim);

        isExpanded = !isExpanded;
    }

    private void shrinkFab() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.mainFabBtn.startAnimation(rotateAntiClockWiseFabAnim);
        binding.galleryFabBtn.startAnimation(toBottomFabAnim);
        binding.shareFabBtn.startAnimation(toBottomFabAnim);
        binding.galleryTv.startAnimation(toBottomFabAnim);
        binding.shareTv.startAnimation(toBottomFabAnim);
        isExpanded = !isExpanded;
    }

    @Override
    public void onConversionClicked(Chat chat) {
        Intent it = new Intent(requireContext(), ChatActivity.class);
        it.putExtra(Constants.KEY_COLLECTION_CHAT, chat);
        startActivity(it);
    }

    @Override
    public void onAddNewChatClick() {
        Intent it = new Intent(requireContext(), CreatePrivateChatActivity.class);
        startActivity(it);
    }

    @Override
    public void onRecentUserChatClick(UserModel userModel) {
        Intent it = new Intent(requireContext(), ChatActivity.class);
        it.putExtra(Constants.KEY_USER, userModel);
        startActivity(it);
    }
}