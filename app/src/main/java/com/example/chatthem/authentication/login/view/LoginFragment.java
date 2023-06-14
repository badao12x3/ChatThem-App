package com.example.chatthem.authentication.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.chatthem.MainActivity;
import com.example.chatthem.R;
import com.example.chatthem.authentication.login.presenter.LogInContract;
import com.example.chatthem.authentication.login.presenter.LogInPresenter;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.databinding.FragmentLoginBinding;
import com.example.chatthem.databinding.FragmentSignupBinding;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements LogInContract.ViewInterface {

    private FragmentLoginBinding binding;

    private float v= 0;
    private Boolean isHidePassEdt = true;

    private LogInPresenter logInPresenter;
    private PreferenceManager preferenceManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout using DataBindingUtil
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        // Get the root view from the binding
        View rootView = binding.getRoot();

        Helpers.setupUI(binding.LoginFragment, requireActivity());
        preferenceManager = new PreferenceManager(requireContext());

        logInPresenter = new LogInPresenter(this, preferenceManager);
        showAnimationFromStart();
        checkEnableLogin();
        setListener();
        return rootView;
    }

    private void setListener(){

        binding.login.setOnClickListener(view -> {
            User user = new User(binding.edtPhone.getText().toString().trim(),
                    binding.edtPass.getText().toString().trim());
            if (isValidLoginDetails(user)){
                binding.login.setVisibility(View.GONE);
                binding.loading.setVisibility(View.VISIBLE);
                logInPresenter.login(user.getPhone(),user.getPassword());
            }
        });
        binding.imgShowPass.setOnClickListener(view ->{
            if(isHidePassEdt){
                binding.edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imgShowPass.setImageResource(R.drawable.red);
                isHidePassEdt = false;
            }else {
                binding.edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imgShowPass.setImageResource(R.drawable.ic_remove_red_eye);
                isHidePassEdt = true;
            }
        });
    }

    private boolean isValidLoginDetails(User user) {
        int colorEror = ContextCompat.getColor(requireActivity(), R.color.md_theme_light_error);
        int colorDefault = ContextCompat.getColor(requireActivity(), R.color.black);

        if(!user.isValidPhone()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);

            binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPhone.setTextColor(colorEror);
            binding.status.setText("Số điện thoại không đúng định dạng");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isValidNumOfPhone()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);

            binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPhone.setTextColor(colorEror);
            binding.status.setText("Số điện thoại cần 10 kí tự số");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isValidPass()){
            binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPhone.setTextColor(colorDefault);

            binding.password.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPass.setTextColor(colorEror);
            binding.status.setText("Password cần tối thiểu 6 ký tự");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else{
            return true;
        }

    }

    private void checkEnableLogin(){
        binding.edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.login.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.login.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void showAnimationFromStart(){
        binding.password.setTranslationX(800);
        binding.password.setAlpha(v);
        binding.password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        binding.phone.setTranslationX(800);
        binding.phone.setAlpha(v);
        binding.phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        binding.forgotPass.setTranslationX(800);
        binding.forgotPass.setAlpha(v);
        binding.forgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        binding.login.setTranslationY(300);
        binding.login.setAlpha(v);
        binding.login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }

    @Override
    public void onLoginError() {
        binding.login.setVisibility(View.VISIBLE);
        binding.loading.setVisibility(View.GONE);
        binding.status.setText("Đăng nhập thất bại!");
        binding.status.setVisibility(View.VISIBLE);
        Toast.makeText(requireActivity(), "Login Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess() {
        binding.login.setVisibility(View.VISIBLE);
        binding.loading.setVisibility(View.GONE);
        binding.status.setVisibility(View.INVISIBLE);
        Toast.makeText(requireActivity(), "Login Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFail() {
        binding.login.setVisibility(View.VISIBLE);
        binding.loading.setVisibility(View.GONE);
        binding.status.setText("Số điện thoại hoặc mật khẩu không đúng");
        binding.status.setVisibility(View.VISIBLE);

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
