package com.example.chatthem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MotionEvent;

import com.example.chatthem.chats.view.ChatsFragment;
import com.example.chatthem.contacts.view.ContactsFragment;
import com.example.chatthem.databinding.ActivityMainBinding;
import com.example.chatthem.profile.view.ProfileFragment;
import com.example.chatthem.utilities.Helpers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ChatsFragment chatsFragment = new ChatsFragment();
    private ContactsFragment contactsFragment = new ContactsFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view_tag, chatsFragment, "ChatsFragment").commit();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.fragment_container_view_tag, chatsFragment, "ChatsFragment");
//        ft.show(chatsFragment);
//        ft.commit();

        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();

        ft0.add(R.id.fragment_container_view_tag, contactsFragment, "ContactsFragment");
        ft0.add(R.id.fragment_container_view_tag, profileFragment, "ProfileFragment");

        ft0.hide(contactsFragment);
        ft0.hide(profileFragment);
        ft0.commit();


//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, chatsFragment, "ChatsFragment").commit();

        binding.bottomNav.setOnItemSelectedListener(item -> {
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            if (item.getItemId() == R.id.chats) {

                ft1.hide(contactsFragment);
                ft1.hide(profileFragment);
                ft1.show(chatsFragment);
                ft1.commit();
                return true;
            }else if (item.getItemId() == R.id.contact) {
                ft1.hide(chatsFragment);
                ft1.hide(profileFragment);
                ft1.show(contactsFragment);
                ft1.commit();
                return true;
            }else if (item.getItemId() == R.id.profile) {
                ft1.hide(chatsFragment);
                ft1.hide(contactsFragment);
                ft1.show(profileFragment);
                ft1.commit();
                return true;
            }
            return false;
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("ChatsFragment");
        if (fragment instanceof ChatsFragment) {
            ChatsFragment chatsFragment1 = (ChatsFragment) fragment;
            // Sử dụng đối tượng YourFragment ở đây
            chatsFragment1.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);

    }


}