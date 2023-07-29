package com.example.chatthem.chats.private_chat_info.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.ActivityProfileDetailBinding;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.profile.model.EditProfileResponse;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileDetailActivity extends AppCompatActivity {

    ActivityProfileDetailBinding binding;
    private PreferenceManager preferenceManager;
    private UserModel userModel;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        preferenceManager = new PreferenceManager(this);
        APIServices.apiServices.getProfile("Bearer "+ preferenceManager.getString(Constants.KEY_TOKEN), getIntent().getStringExtra("id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getApplicationContext(), "Xảy ra lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        binding.textName.setText(userModel.getUsername());
                        preferenceManager.putString(Constants.KEY_NAME,userModel.getUsername());

                        binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
                        if (userModel.getCover_image() != null){
                            preferenceManager.putString(Constants.KEY_COVERIMAGE, userModel.getCover_image());
                            binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getCover_image()));
                        }
                        if (userModel.getBirthday() != null){
                            preferenceManager.putString(Constants.KEY_BIRTHDAY, userModel.getBirthday());
                            binding.textBirthday.setText(userModel.getBirthday());
                            binding.textBirthday.setVisibility(View.VISIBLE);
                        }else {
                            binding.textBirthday.setVisibility(View.GONE);
                        }
                        if (userModel.getGender() != null){
                            preferenceManager.putString(Constants.KEY_GENDER, userModel.getGender());
                            binding.textGender.setText(userModel.getGender());
                            binding.textGender.setVisibility(View.VISIBLE);
                        }else {
                            binding.textGender.setVisibility(View.GONE);
                        }
                        if (userModel.getCity() != null){
                            preferenceManager.putString(Constants.KEY_ADDRESS_CITY, userModel.getCity());
                            preferenceManager.putString(Constants.KEY_ADDRESS_COUNTRY, userModel.getCountry());
                            preferenceManager.putString(Constants.KEY_ADDRESS_DETAIL, userModel.getAddress());
                            String city = userModel.getCity()+" (" + userModel.getCountry()+")";
                            binding.textCity.setText(city);
                            binding.textAddressDetail.setText(userModel.getAddress());
                        }else {
                            binding.textCity.setText("Không rõ");
                            binding.textAddressDetail.setText("Không rõ");
                        }
                        if (userModel.getEmail() != null){
                            preferenceManager.putString(Constants.KEY_EMAIL, userModel.getEmail());

                            binding.textPhone.setText(userModel.getEmail());
                            binding.textPhone.setVisibility(View.VISIBLE);
                        }else {
                            binding.textPhone.setText("Không có");
                        }

                        binding.itemEmail.textEmail.setText(userModel.getPhonenumber());
                        preferenceManager.putString(Constants.KEY_PHONE,userModel.getPhonenumber());
                    }
                });

    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{onBackPressed();});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}