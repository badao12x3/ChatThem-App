package com.example.chatthem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chatthem.chats.view.ChatsFragment;
import com.example.chatthem.contacts.view.ContactsFragment;
import com.example.chatthem.databinding.ActivityMainBinding;
import com.example.chatthem.profile.view.ProfileFragment;

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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, chatsFragment).commit();

        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.chats) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, chatsFragment).commit();
                return true;
            }else if (item.getItemId() == R.id.contact) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, contactsFragment).commit();
                return true;
            }else if (item.getItemId() == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, profileFragment).commit();
                return true;
            }
            return false;
        });


    }
}