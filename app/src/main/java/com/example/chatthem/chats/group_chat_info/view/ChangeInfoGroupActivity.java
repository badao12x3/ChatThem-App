package com.example.chatthem.chats.group_chat_info.view;

import static com.example.chatthem.utilities.Helpers.encodeImage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.databinding.ActivityChangeInfoGroupBinding;
import com.example.chatthem.utilities.Helpers;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChangeInfoGroupActivity extends AppCompatActivity {

    ActivityChangeInfoGroupBinding binding;
    private String chatId;
    private String encodeImg = null;
    private String newName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeInfoGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListener();

    }
    private void init(){
        binding.textName.setText(getIntent().getStringExtra("name"));
        binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(getIntent().getStringExtra("avatar")));
        chatId = getIntent().getStringExtra("chatId");
    }
    private void setListener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.textName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = !TextUtils.isEmpty(s);
                binding.change.setEnabled(hasText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.updateProfileImg.setOnClickListener(v -> onUpdateProfileImgPressed());
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
                    binding.change.setEnabled(true);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        encodeImg = encodeImage(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                    Toast.makeText(getApplicationContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Hủy thêm ảnh", Toast.LENGTH_SHORT).show();
                }
            });
}