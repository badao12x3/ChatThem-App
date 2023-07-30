package com.example.chatthem.profile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.databinding.ActivityChangePasswordBinding;
import com.example.chatthem.profile.presenter.ChangePassContract;
import com.example.chatthem.profile.presenter.ChangePassPresenter;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;

public class ChangePasswordActivity extends AppCompatActivity implements ChangePassContract.ViewInterface {


    ActivityChangePasswordBinding binding;
    private PreferenceManager preferenceManager;
    private String currPass = null;
    private String newPass = null;
    private ChangePassPresenter changePassPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(),this);

        preferenceManager = new PreferenceManager(getApplicationContext());
        changePassPresenter = new ChangePassPresenter(this, preferenceManager, getApplicationContext());

        getCurrPass();
        setListener();
        onEditTextStatusChange();
    }
    private void onEditTextStatusChange(){
        int colorFocus = ContextCompat.getColor(getApplicationContext(), R.color.primary_text);
        int colorDefault = ContextCompat.getColor(getApplicationContext(), R.color.secondary_text);
        binding.inputCurrentPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_good);
                    binding.inputCurrentPass.setHintTextColor(colorFocus);
                }
                else {
                    binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input);
                    binding.inputCurrentPass.setHintTextColor(colorDefault);
//                    if (binding.inputCurrentPass.getText().toString().isEmpty()) {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_wrong);
//                    } else {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_good);
//                    }
                }
            }
        });
        binding.inputNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_good);
                    binding.inputNewPassword.setHintTextColor(colorFocus);
                }
                else {
                    binding.inputNewPassword.setBackgroundResource(R.drawable.background_input);
                    binding.inputNewPassword.setHintTextColor(colorDefault);
                }
            }
        });
        binding.inputConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_good);
                    binding.inputConfirmPassword.setHintTextColor(colorFocus);
                }
                else {
                    binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input);
                    binding.inputConfirmPassword.setHintTextColor(colorDefault);
                }
            }
        });
    }
    private void getCurrPass(){
        System.out.println("curren password");
        currPass = preferenceManager.getString(Constants.KEY_PASSWORD);
        System.out.println(currPass);
    }
    private Boolean isValidPassDetails(){
        int colorEror = ContextCompat.getColor(this, R.color.error);
        if(binding.inputCurrentPass.getText().toString().trim().isEmpty()){
            binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputCurrentPass.setHintTextColor(colorEror);
            showToast("Hãy nhập mật khẩu hiện tại");
            return false;
        }else if(binding.inputNewPassword.getText().toString().trim().isEmpty()){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            showToast("Hãy nhập mật khẩu mới");
            return false;
        }else if(binding.inputConfirmPassword.getText().toString().isEmpty()){
            binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputConfirmPassword.setHintTextColor(colorEror);
            showToast("Hãy nhập lại mật khẩu mới");
            return false;
        }else if(!binding.inputNewPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputConfirmPassword.setHintTextColor(colorEror);
            showToast("Xác nhận mật khẩu mới chưa không đồng nhất, hãy kiểm tra lại");
            return false;
        }else if (binding.inputCurrentPass.getText().toString().equals(binding.inputNewPassword.getText().toString())){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            showToast("Mật khẩu mới không được giống với mật khẩu cũ");
            return false;
        }else if (!binding.inputCurrentPass.getText().toString().equals(currPass)) {
            binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputCurrentPass.setHintTextColor(colorEror);
            showToast("Mật khẩu hiện tại không chính xác");
            return false;
        }else if(binding.inputNewPassword.getText().toString().length() < 6){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputConfirmPassword.setHintTextColor(colorEror);
            showToast("Mật khẩu cần có tối thiểu 6 kí tự");
            return false;
        }
        else{
            return true;
        }

    }
    private void loading(boolean isLoading) {
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void showToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void setListener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.buttonChangePass.setOnClickListener(view -> {
            if (isValidPassDetails()){
                binding.buttonChangePass.setVisibility(View.GONE);
                loading(true);
                newPass = binding.inputNewPassword.getText().toString().trim();
                currPass = binding.inputCurrentPass.getText().toString().trim();
                changePassPresenter.changePass(currPass,newPass);
            }
        });
    }

    @Override
    public void onChangePassSuccess() {
        binding.buttonChangePass.setVisibility(View.VISIBLE);
        showToast("Thay đổi mật khẩu thành công");
        onBackPressed();
    }

    @Override
    public void onChangePassError() {
        binding.buttonChangePass.setVisibility(View.VISIBLE);
        showToast("Thay đổi mật khẩu thất bại! Hãy thử lại.");
    }
}