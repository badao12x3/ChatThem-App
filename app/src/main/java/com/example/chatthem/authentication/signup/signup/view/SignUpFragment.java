package com.example.chatthem.authentication.signup.signup.view;

import static com.example.chatthem.utilities.Helpers.encodeImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.chatthem.MainActivity;
import com.example.chatthem.R;
import com.example.chatthem.authentication.model.User;
import com.example.chatthem.authentication.signup.signup.presenter.SignUpContract;
import com.example.chatthem.authentication.signup.signup.presenter.SignUpPresenter;
import com.example.chatthem.databinding.FragmentSignupBinding;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class SignUpFragment extends Fragment implements SignUpContract.ViewInterface {
    private FragmentSignupBinding binding;
    private String encodedImage;
    private Boolean isHidePassEdt = true;
    private Boolean isHideRePassEdt = true;
    private PreferenceManager preferenceManager;
    private SignUpPresenter signUpPresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout using DataBindingUtil
        binding = FragmentSignupBinding.inflate(inflater, container, false);

        // Get the root view from the binding
        View rootView = binding.getRoot();
        preferenceManager = new PreferenceManager(requireContext());

        signUpPresenter = new SignUpPresenter(requireContext(),this, preferenceManager);
        Helpers.setupUI(binding.signupFragment, requireActivity());
        checkEnableSignup();
        setListener();
        onEditTextStatusChange();
        return rootView;
    }

    private void checkEnableSignup(){
        binding.edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
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
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.reEdtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setListener(){

        binding.signup.setOnClickListener(view -> {
            loading(true);
            User user = new User(binding.edtPhone.getText().toString().trim(),
                    binding.edtPass.getText().toString().trim(),
                    binding.reEdtPass.getText().toString().trim(),
                    encodedImage,
                    binding.edtUsername.getText().toString().trim());
            String publicKey = signUpPresenter.getPublicKeyStr();
            if (isValidSignUpDetails(user) && !Objects.equals(publicKey, "")){
                signUpPresenter.signup(user.getAvatar(), user.getUsername(), user.getPhone(), user.getPassword(),publicKey);
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            onUpdateProfileImgPressed();
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
        binding.reImgShowPass.setOnClickListener(view ->{
            if(isHideRePassEdt){
                binding.reEdtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.reImgShowPass.setImageResource(R.drawable.red);
                isHideRePassEdt = false;
            }else {
                binding.reEdtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.reImgShowPass.setImageResource(R.drawable.ic_remove_red_eye);
                isHideRePassEdt = true;
            }
        });
    }
    private Boolean isValidSignUpDetails(User user){
        int colorEror = ContextCompat.getColor(requireActivity(), R.color.md_theme_light_error);
        int colorDefault = ContextCompat.getColor(requireActivity(), R.color.black);

        if(!user.isValidPhone()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);
            binding.rePass.setBackgroundResource(R.drawable.edit_text_bg);
            binding.reEdtPass.setHintTextColor(colorDefault);

            binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPhone.setTextColor(colorEror);
            binding.status.setText("Số điện thoại không đúng định dạng");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isValidNumOfPhone()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);
            binding.rePass.setBackgroundResource(R.drawable.edit_text_bg);
            binding.reEdtPass.setHintTextColor(colorDefault);

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
        }else if(!user.isConfirmPass()){
            binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPhone.setTextColor(colorDefault);

            binding.password.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPass.setTextColor(colorEror);
            binding.rePass.setBackgroundResource(R.drawable.background_input_wrong);
            binding.reEdtPass.setTextColor(colorEror);
            binding.status.setText("Hãy kiểm tra lại sao cho password trùng nhau");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else{
            return true;
        }
    }
    private void onEditTextStatusChange(){
        int colorFocus = ContextCompat.getColor(requireActivity(), R.color.md_theme_light_primary);
        int colorDefault = ContextCompat.getColor(requireActivity(), R.color.secondary_text);
        binding.edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.username.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtUsername.setHintTextColor(colorFocus);
                }
                else {
                    binding.username.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtUsername.setHintTextColor(colorDefault);
//                    if (binding.inputCurrentPass.getText().toString().isEmpty()) {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_wrong);
//                    } else {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_good);
//                    }
                }
            }
        });
        binding.edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.phone.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtPhone.setHintTextColor(colorFocus);
                }
                else {
                    binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtPhone.setHintTextColor(colorDefault);
                }
            }
        });
        binding.edtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.password.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtPass.setHintTextColor(colorFocus);
                }
                else {
                    binding.password.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtPass.setHintTextColor(colorDefault);
                }
            }
        });
        binding.reEdtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.rePass.setBackgroundResource(R.drawable.background_input_good);
                    binding.reEdtPass.setHintTextColor(colorFocus);
                }
                else {
                    binding.rePass.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.reEdtPass.setHintTextColor(colorDefault);
                }
            }
        });
    }
    private void onUpdateProfileImgPressed() {
        ImagePicker.with(this)
                .cropSquare()//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent(intent -> {
                    startForProfileImageResult.launch(intent);
                    return null;
                });
    }

    private final ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    Uri resultUri = data.getData();
                    binding.imageProfile.setImageURI(resultUri);
                    binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty());
                    try {
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        encodedImage = encodeImage(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                        Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });


    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void loading(Boolean isload){
        if (isload){
            binding.status.setVisibility(View.INVISIBLE);
        }
        binding.loading.setVisibility(isload? View.VISIBLE : View.GONE);
        binding.signup.setVisibility(isload? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPhoneExistSignUpFail() {
        loading(false);
        binding.status.setText("Số điện thoại đã tồn tại, vui lòng chọn số khác");
        binding.status.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignUpError() {
        loading(false);
        binding.status.setText("Đăng ký thất bại");
        binding.status.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignUpSuccess() {
        loading(false);
        binding.status.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onSavePriKeySignUpError() {
        loading(false);
        binding.status.setText("Lưu khóa bí mật thất bại");
        binding.status.setVisibility(View.VISIBLE);

    }

    @Override
    public void onGenKeyPairSignUpError() {
        loading(false);
        binding.status.setText("Tạo cặp khóa thất bại");
        binding.status.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (signUpPresenter.getDisposable() != null) {
            signUpPresenter.getDisposable().dispose();
        }

    }
}
