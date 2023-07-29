package com.example.chatthem.cryptophy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chatthem.MainActivity;
import com.example.chatthem.databinding.ActivityTransferDataBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

import java.security.PrivateKey;
import java.security.PublicKey;

public class TransferDataActivity extends AppCompatActivity {


    ActivityTransferDataBinding binding ;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransferDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        binding.scanBtn.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan a QR Code");
            options.setCameraId(0);  // Use a specific camera of the device
            options.setBeepEnabled(true);
            options.setOrientationLocked(false);
            barcodeLauncher.launch(options);

        });
    }
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(getApplicationContext(), "Quét QR Code bị hủy", Toast.LENGTH_LONG).show();
                } else {
                    // if the intentResult is not null we'll set
                    // the content and format of scan message
                    try {
                        // Phân tích dữ liệu JSON
                        JSONObject jsonData = new JSONObject(result.getContents());
                        String phone = jsonData.getString(Constants.KEY_PHONE);
                        String privateKey = jsonData.getString(Constants.KEY_PRIVATE_KEY);

                        // Xử lý dữ liệu
                        if (!phone.equals(preferenceManager.getString(Constants.KEY_PHONE))){
                            Toast.makeText(getApplicationContext(), "QR bạn đang quét không khớp với tài khoản bạn đang đăng nhập trên máy này!", Toast.LENGTH_SHORT).show();
                        }else {
                            preferenceManager.putString(Constants.KEY_PRIVATE_KEY, privateKey);
                            binding.textContent.setText("Lấy dữ liệu thành công");

                            //save in new device
                            PrivateKey privateKeyObj = ECCc.stringToPrivateKey(preferenceManager.getString(Constants.KEY_PRIVATE_KEY));
                            PublicKey publicKeyObj = ECCc.stringToPublicKey(preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
                            ECCc.savePrivateKey2(getApplicationContext(),
                                    preferenceManager.getString(Constants.KEY_EMAIL),
                                    preferenceManager.getString(Constants.KEY_PASSWORD),
                                    privateKeyObj, publicKeyObj
                            );

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

}