package com.example.chatthem.contacts.manage_request_friend.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatthem.authentication.login.view.LoginFragment;
import com.example.chatthem.authentication.signup.signup.view.SignUpFragment;

public class PagerAdapter extends FragmentStateAdapter {
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ReceiveReqFragment();

            case 1:
                return new SendReqFragment();

            default:
                return new ReceiveReqFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
