package com.example.chatthem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.chatthem.chats.view.ChatsFragment;
import com.example.chatthem.contacts.view.ContactsFragment;
import com.example.chatthem.databinding.ActivityMainBinding;
import com.example.chatthem.firebase.MessagingService;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.networking.SocketManager;
import com.example.chatthem.profile.model.EditProfileResponse;
import com.example.chatthem.profile.view.ProfileFragment;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ChatsFragment chatsFragment = new ChatsFragment();
    private ContactsFragment contactsFragment = new ContactsFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private PreferenceManager preferenceManager;
    private SocketManager socketManager ;
    private Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);

        socketManager = SocketManager.getInstance();
        socket = socketManager.getSocket();
        preferenceManager = new PreferenceManager(getApplicationContext());

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

        JSONObject data = new JSONObject();
        try {
            data.put("userId", preferenceManager.getString(Constants.KEY_USED_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            socket.emit("online", data);
        } else {
            // Xử lý trường hợp Socket.IO object là null
        }
        getToken();

    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToke);
    }

    private void updateToke(String token) {
        if (preferenceManager.getString(Constants.KEY_FCM_TOKEN).equals(token)) return;
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);

        APIServices.apiServices.updateFCMToken("Bearer " + preferenceManager.getString(Constants.KEY_TOKEN), token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull EditProfileResponse editProfileResponse) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(getApplicationContext(), "FCM token thay đổi! Cập nhật FCM token lên server thất bại", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "FCM token thay đổi! Cập nhật FCM token lên server thành công", Toast.LENGTH_SHORT).show();
                    }
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