package com.example.chatthem.contacts.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatthem.chats.create_new_private_chat.view.CreatePrivateChatActivity;
import com.example.chatthem.contacts.manage_request_friend.view.ManageReqFrieActivity;
import com.example.chatthem.databinding.FragmentContactsBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    private FragmentContactsBinding binding;
    private PreferenceManager preferenceManager;
    private String STR ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater,container,false);
        View rootView = binding.getRoot();
        Helpers.setupUI(rootView, requireActivity());
        preferenceManager = new PreferenceManager(requireContext());

        binding.username.setText(preferenceManager.getString(Constants.KEY_NAME));

        // Chuyển đổi dữ liệu thành định dạng JSON
        STR = preferenceManager.getString(Constants.KEY_PHONE);

        try {
            // Tính toán kích thước ảnh QR dựa trên kích thước màn hình
            int qrCodeSize = getQRCodeSize();
            //Tạo qr
            Bitmap bitmap = encodeAsBitmap(STR, qrCodeSize);
            // Hiển thị lên imv
            binding.qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Fail when gen QR", Toast.LENGTH_SHORT).show();
        }
        setListener();
        return rootView;
    }

    private void setListener(){
        binding.cardviewListFriend.setOnClickListener(v->{
            Intent it = new Intent(requireContext(), CreatePrivateChatActivity.class);
            it.putExtra("title", "Danh sách bạn bè");
            startActivity(it);
        });
        binding.btnScan.setOnClickListener(v->{
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan a QR Code");
            options.setCameraId(0);  // Use a specific camera of the device
            options.setBeepEnabled(true);
            options.setOrientationLocked(false);
            barcodeLauncher.launch(options);
        });
        binding.cardviewRequest.setOnClickListener(v->{
            Intent it = new Intent(requireContext(), ManageReqFrieActivity.class);
            startActivity(it);
        });
    }

    private int getQRCodeSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int smallerDimension = Math.min(screenWidth, screenHeight);
        return (int) (smallerDimension * 0.8); // Sử dụng tỷ lệ 80% của kích thước màn hình
    }
    Bitmap encodeAsBitmap(String str, int qrCodeSize) throws WriterException {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.encodeBitmap(str, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
        return bitmap;
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(requireContext(), "Quét QR Code bị hủy", Toast.LENGTH_LONG).show();
                } else {
                    // if the intentResult is not null we'll set
                    // the content and format of scan message
                    try {
                        String phone = result.getContents();
                        binding.searchView.setQueryHint(phone);

                        // Xử lý dữ liệu
                        if (phone.equals(preferenceManager.getString(Constants.KEY_PHONE))){
                            Toast.makeText(requireContext(), "Bạn đang quét QR của chính bạn!", Toast.LENGTH_SHORT).show();
                        }else {
//                            preferenceManager.putString(Constants.KEY_PRIVATE_KEY, privateKey);
//
//                            //save in new device
//                            PrivateKey privateKeyObj = ECCc.stringToPrivateKey(preferenceManager.getString(Constants.KEY_PRIVATE_KEY));
//                            PublicKey publicKeyObj = ECCc.stringToPublicKey(preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
//                            ECCc.savePrivateKey2(getApplicationContext(),
//                                    preferenceManager.getString(Constants.KEY_EMAIL),
//                                    preferenceManager.getString(Constants.KEY_PASSWORD),
//                                    privateKeyObj, publicKeyObj
//                            );
//
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                            finish();

                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
}