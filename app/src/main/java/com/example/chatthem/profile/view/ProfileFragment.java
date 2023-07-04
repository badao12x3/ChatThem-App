package com.example.chatthem.profile.view;

import static com.example.chatthem.utilities.Helpers.encodeCoverImage;
import static com.example.chatthem.utilities.Helpers.encodeImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.authentication.LoginActivity;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.databinding.FragmentProfileBinding;
import com.example.chatthem.profile.presenter.ProfileContract;
import com.example.chatthem.profile.presenter.ProfilePresenter;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.Helpers;
import com.example.chatthem.utilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileFragment extends Fragment implements ProfileContract.ViewInterface {

    private FragmentProfileBinding binding;

    private PreferenceManager preferenceManager;

    private ProfilePresenter profilePresenter;
    private DatePickerDialog datePickerDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View rootView = binding.getRoot();
        Helpers.setupUI(binding.getRoot(),requireActivity());
        init();
        setListener();
        showUserInfo();
        return rootView;
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireContext());
        profilePresenter = new ProfilePresenter(this,preferenceManager);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                binding.editBirthday.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(requireContext(),style,dateSetListener,y,m,d );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        binding.editBirthday.setText(getTodayDate());

        String[] gender = {"Nam", "Nữ", "Bí mật"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_gender_item, gender);
        binding.autoCompleteText.setAdapter(adapter);

    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        m = m + 1;
        return d + "/" + m + "/" + y;
    }

    private void setListener(){
        binding.include.logout.setOnClickListener(v -> {
            onLogOut();
        });
        binding.itemInfo.btnAdd.setOnClickListener(v -> onAddPhoneNumberPressed());
        binding.itemInfo.btnOK.setOnClickListener(v -> {
            if (isValidPhone(binding.itemInfo.editTextPhoneNumber.getText().toString().trim())){
                onOKPhoneNumberPressed();
            }
        });
        binding.itemInfo.btnHide.setOnClickListener(v -> onCancelPhoneNumberPressed());
        binding.include.icEdit.setOnClickListener(v -> onEditNamePressed());

        binding.itemInfo.btnCancel.setOnClickListener(v -> onCancelPhoneEditPressed());
        binding.itemInfo.icEdit.setOnClickListener(v -> onEditPhonePressed());
        binding.include.btnChangePass.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
        });

        binding.include.genQrCode.setOnClickListener(x -> {
            onAddDevicePressed();
        });

        binding.updateProfileImg.setOnClickListener(view ->
                ImagePicker.with(this)
                    .crop()//Crop image(Optional), Check Customization for more option
//                    .compress(4096)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(2160, 2160)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(intent -> {
                        startForCoverImageResult.launch(intent);
                        return null;
                    })
        );
        binding.include.updateProfileImg.setOnClickListener(view -> onUpdateProfileImgPressed());
        binding.include.imageProfile.setOnClickListener(view -> {

        });

        binding.itemAdd.btnAdd.setOnClickListener(view -> onAddAdressPressed());
        binding.itemAdd.btnOK.setOnClickListener(view -> {
            if (isValidAdress()) {
                onOKAdressPressed();
            }
        });
        binding.itemAdd.icEdit.setOnClickListener(view -> onEditAdressPressed());
        binding.itemAdd.btnCancel.setOnClickListener(view -> onCancelAdressPressed());
        binding.itemAdd.icHide.setOnClickListener(view -> onHideAdressPressed());
        binding.icEditBirthday.setOnClickListener(view -> onEditBirthdayPressed());
        binding.btnOKBirthday.setOnClickListener(view -> onOKBirthdayPressed());
        binding.editBirthday.setOnClickListener(view -> {
            datePickerDialog.show();
        });
        binding.btnOKGender.setOnClickListener(view -> profilePresenter.editGender(binding.autoCompleteText.getText().toString()));
        binding.icEditGender.setOnClickListener(view -> onEditGenderPressed());

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Xử lý logic khi làm mới dữ liệu, ví dụ tải dữ liệu mới
                profilePresenter.getProfile();
            }
        });
    }

    private void onOKBirthdayPressed() {
        profilePresenter.editBirthday(binding.editBirthday.getText().toString().trim());
    }
    private void onEditGenderPressed(){
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        if (binding.containerGenderBtn.getVisibility() == View.VISIBLE){
            binding.containerGenderBtn.setVisibility(View.GONE);
            binding.spinnerGender.setVisibility(View.GONE);
            if (preferenceManager.getString(Constants.KEY_BIRTHDAY) != null){
                binding.textGender.setVisibility(View.VISIBLE);
            }
        }else {
            binding.containerGenderBtn.setVisibility(View.VISIBLE);
            binding.spinnerGender.setVisibility(View.VISIBLE);
            binding.textGender.setVisibility(View.GONE);
        }
    }

    private void onEditBirthdayPressed() {
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        if (binding.containerBirthdayBtn.getVisibility() == View.VISIBLE){
            binding.containerBirthdayBtn.setVisibility(View.GONE);
            binding.editBirthday.setVisibility(View.GONE);
            if (preferenceManager.getString(Constants.KEY_BIRTHDAY) != null){
                binding.textBirthday.setVisibility(View.VISIBLE);
            }
        }else {
            binding.containerBirthdayBtn.setVisibility(View.VISIBLE);
            binding.editBirthday.setVisibility(View.VISIBLE);
            binding.textBirthday.setVisibility(View.GONE);
        }
    }



    private void onLogOut(){
        showToast("Đang đăng xuất...");
        preferenceManager.clear();
        startActivity(new Intent(requireContext(), LoginActivity.class));
        requireActivity().finish();
    }
    private void onAddAdressPressed() {
        binding.itemAdd.btnAdd.setVisibility(View.GONE);
        binding.itemAdd.btnCancel.setVisibility(View.GONE);
        binding.itemAdd.icHide.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemAdd.containerEditAddres.setVisibility(View.VISIBLE);
        binding.itemAdd.containerBtn.setVisibility(View.VISIBLE);
    }
    private void onEditAdressPressed() {
        if(binding.itemAdd.btnCancel.getVisibility() == View.GONE){
            binding.itemAdd.btnCancel.setVisibility(View.VISIBLE);
        }
        binding.itemAdd.icEdit.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemAdd.containerAddress.setVisibility(View.GONE);
        binding.itemAdd.editTextCountry.setText(preferenceManager.getString(Constants.KEY_ADDRESS_COUNTRY));
        binding.itemAdd.editTextCity.setText(preferenceManager.getString(Constants.KEY_ADDRESS_CITY));
        binding.itemAdd.editTextDetail.setText(preferenceManager.getString(Constants.KEY_ADDRESS_DETAIL));
        binding.itemAdd.containerEditAddres.setVisibility(View.VISIBLE);
        binding.itemAdd.containerBtn.setVisibility(View.VISIBLE);
    }
    private void onCancelAdressPressed() {

        binding.itemAdd.icEdit.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());

        binding.itemAdd.containerEditAddres.setVisibility(View.GONE);
        binding.itemAdd.containerBtn.setVisibility(View.GONE);
        binding.itemAdd.containerAddress.setVisibility(View.VISIBLE);
    }
    private void onHideAdressPressed() {

        binding.itemAdd.icHide.setVisibility(View.GONE);
        binding.itemAdd.btnAdd.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemAdd.containerEditAddres.setVisibility(View.GONE);
        binding.itemAdd.containerBtn.setVisibility(View.GONE);
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
                    try {
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        String encodedImage = encodeImage(bitmap);
                        profilePresenter.editAvatar(encodedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
    private final ActivityResultLauncher<Intent> startForCoverImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    Uri resultUri = data.getData();
                    try {
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        String encodedImage = encodeCoverImage(bitmap);
                        profilePresenter.editCoverImage(encodedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

    private void onOKAdressPressed(){

        HashMap<String, Object> user = new HashMap<>();
        String city = binding.itemAdd.editTextCity.getText().toString();
        String country = binding.itemAdd.editTextCountry.getText().toString();
        String address = binding.itemAdd.editTextDetail.getText().toString();

        profilePresenter.editAddress(address,city,country);
    }
    private void onEditPhonePressed(){
        binding.itemInfo.btnCancel.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemInfo.textPhone.setVisibility(View.GONE);
        binding.itemInfo.icEdit.setVisibility(View.GONE);
        binding.itemInfo.editTextPhoneNumber.setVisibility(View.VISIBLE);
        binding.itemInfo.containerBtn.setVisibility(View.VISIBLE);


    }
    private Boolean isValidAdress(){
        if (binding.itemAdd.editTextCity.getText().toString().trim().isEmpty()){
            showToast("Hãy nhập tên thành phố");
            return false;
        }else if(binding.itemAdd.editTextCountry.getText().toString().trim().isEmpty()){
            showToast("Hãy nhập tên quốc gia");
            return false;
        }else if(binding.itemAdd.editTextDetail.getText().toString().trim().isEmpty()){
            showToast("Hãy nhập địa chỉ cụ thể");
            return false;
        }
        return true;
    }
    private void onOKPhoneNumberPressed(){

        String textEmail = binding.itemInfo.editTextPhoneNumber.getText().toString().trim();
        profilePresenter.editEmail(textEmail);

    }
    private void onEditNamePressed() {
        // Sử dụng LayoutInflater để tạo ra view từ tệp tin layout tùy chỉnh
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        // Lấy tham chiếu đến các phần tử trong layout tùy chỉnh
        TextView textViewTitle = dialogView.findViewById(R.id.textViewTitle);
        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        Button buttonOK = dialogView.findViewById(R.id.btn_OK);
        Button buttonEnd = dialogView.findViewById(R.id.btn_Cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm ra bên ngoài
        alertDialog.setCanceledOnTouchOutside(false);

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm nút Back
        alertDialog.setCancelable(false);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = editTextName.getText().toString().trim();
                        if (!newName.isEmpty() && !Patterns.DOMAIN_NAME.matcher(editTextName.getText().toString()).matches()) {
                            // Thay đổi tên người dùng thành newName ở đây
                            profilePresenter.editUsername(newName);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(requireContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
    private void onCancelPhoneNumberPressed(){
        binding.itemInfo.btnHide.setVisibility(View.GONE);
        binding.itemInfo.btnAdd.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemInfo.editTextPhoneNumber.setVisibility(View.GONE);
        binding.itemInfo.containerBtn.setVisibility(View.GONE);
    }
    private void onCancelPhoneEditPressed(){
        binding.itemInfo.editTextPhoneNumber.setVisibility(View.GONE);
        binding.itemInfo.btnCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemInfo.textPhone.setVisibility(View.VISIBLE);
        binding.itemInfo.icEdit.setVisibility(View.VISIBLE);

        binding.itemInfo.containerBtn.setVisibility(View.GONE);

    }
    private Boolean isValidPhone(String textPhone){
        if (textPhone.isEmpty()){
            showToast("Hãy nhập email!");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(textPhone).matches()){
            showToast("Định dạng email không đúng");
            return false;
        }
        return true;
    }
    private void showToast (String message){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void onAddPhoneNumberPressed(){

//        TransitionManager.beginDelayedTransition(binding.itemInfo.cardview, new AutoTransition());
        binding.itemInfo.btnAdd.setVisibility(View.GONE);
        binding.itemInfo.btnHide.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemInfo.editTextPhoneNumber.setVisibility(View.VISIBLE);
        binding.itemInfo.containerBtn.setVisibility(View.VISIBLE);
//        binding.itemInfo.container.startAnimation(slideUp);

    }
    private void onAddDevicePressed() {
        // Sử dụng LayoutInflater để tạo ra view từ tệp tin layout tùy chỉnh
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_warning, null);

        // Lấy tham chiếu đến các phần tử trong layout tùy chỉnh
        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        Button buttonOK = dialogView.findViewById(R.id.btn_OK);
        Button buttonEnd = dialogView.findViewById(R.id.btn_Cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm ra bên ngoài
        alertDialog.setCanceledOnTouchOutside(false);

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm nút Back
        alertDialog.setCancelable(false);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pass = editTextName.getText().toString().trim();
                        if (!pass.isEmpty() && pass.equals(preferenceManager.getString(Constants.KEY_PASSWORD))) {
                            startActivity(new Intent(requireContext(), QR_code.class));
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(requireActivity(), "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
    private void showUserInfo(){
        if(preferenceManager.getString(Constants.KEY_AVATAR) != null){
            byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_AVATAR), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.include.imageProfile.setImageBitmap(bitmap);
        }

        if(preferenceManager.getString(Constants.KEY_PHONE) != null){
            binding.itemEmail.textEmail.setText(preferenceManager.getString(Constants.KEY_PHONE));
        }
        if(preferenceManager.getString(Constants.KEY_NAME) != null){
            binding.include.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        }
        if(preferenceManager.getString(Constants.KEY_EMAIL) != null){

            binding.itemInfo.btnAdd.setVisibility(View.GONE);
            binding.itemInfo.btnHide.setVisibility(View.GONE);

            binding.itemInfo.textPhone.setText(preferenceManager.getString(Constants.KEY_EMAIL));
            binding.itemInfo.textPhone.setVisibility(View.VISIBLE);
        }else {
            binding.itemInfo.icEdit.setVisibility(View.GONE);
            binding.itemInfo.btnHide.setVisibility(View.GONE);
            binding.itemInfo.textPhone.setVisibility(View.GONE);
        }
        if(preferenceManager.getString(Constants.KEY_ADDRESS_CITY) != null ){
            binding.itemAdd.btnAdd.setVisibility(View.GONE);
            binding.itemAdd.icHide.setVisibility(View.GONE);

            String city =  preferenceManager.getString(Constants.KEY_ADDRESS_CITY) + " (" + preferenceManager.getString(Constants.KEY_ADDRESS_COUNTRY) + ")";
            binding.itemAdd.textCity.setText(city);

            binding.itemAdd.textAddressDetail.setVisibility(View.VISIBLE);
            binding.itemAdd.textAddressDetail.setText(preferenceManager.getString(Constants.KEY_ADDRESS_DETAIL));

        }else {
            binding.itemAdd.icEdit.setVisibility(View.GONE);
            binding.itemAdd.icHide.setVisibility(View.GONE);
            binding.itemAdd.containerAddress.setVisibility(View.GONE);
        }
        if (preferenceManager.getString(Constants.KEY_BIRTHDAY) != null){
            binding.textBirthday.setText(preferenceManager.getString(Constants.KEY_BIRTHDAY));
            binding.textBirthday.setVisibility(View.VISIBLE);
        }else {
            binding.textBirthday.setVisibility(View.GONE);
        }
        if (preferenceManager.getString(Constants.KEY_GENDER) != null){
            binding.textGender.setText(preferenceManager.getString(Constants.KEY_GENDER));
            binding.textGender.setVisibility(View.VISIBLE);
        }else {
            binding.textGender.setVisibility(View.GONE);
        }
        if (preferenceManager.getString(Constants.KEY_COVERIMAGE) != null){
            binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_COVERIMAGE)));
        }
    }
    @Override
    public void onEditProfileSuccess(String type) {
        showToast("Cập nhật thành công");
        switch (type){
            case "avatar":
                String avatar = profilePresenter.getUserModel().getAvatar();
                preferenceManager.putString(Constants.KEY_AVATAR,avatar);
                binding.include.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(avatar));
                break;
            case "username":
                String newName = profilePresenter.getUserModel().getUsername();
                preferenceManager.putString(Constants.KEY_NAME, newName);
                binding.include.textName.setText(newName);
                break;
            case "cover_image":
                String cover_img = profilePresenter.getUserModel().getCover_image();
                preferenceManager.putString(Constants.KEY_COVERIMAGE,cover_img);
                binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(cover_img));
                break;
            case "email":
                String newEmail = profilePresenter.getUserModel().getEmail();
                preferenceManager.putString(Constants.KEY_EMAIL, newEmail);
                binding.itemInfo.btnHide.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.itemInfo.editTextPhoneNumber.setVisibility(View.GONE);
                binding.itemInfo.containerBtn.setVisibility(View.GONE);
                binding.itemInfo.btnAdd.setVisibility(View.GONE);
                binding.itemInfo.textPhone.setText(newEmail);
                binding.itemInfo.textPhone.setVisibility(View.VISIBLE);
                binding.itemInfo.icEdit.setVisibility(View.VISIBLE);
                break;
            case "address":
                String city = profilePresenter.getUserModel().getCity();
                String country = profilePresenter.getUserModel().getCountry();
                String address = profilePresenter.getUserModel().getAddress();

                preferenceManager.putString(Constants.KEY_ADDRESS_CITY, city);
                preferenceManager.putString(Constants.KEY_ADDRESS_COUNTRY, country);
                preferenceManager.putString(Constants.KEY_ADDRESS_DETAIL, address);
                binding.itemAdd.icHide.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.itemAdd.containerEditAddres.setVisibility(View.GONE);
                binding.itemAdd.containerBtn.setVisibility(View.GONE);
                binding.itemAdd.btnAdd.setVisibility(View.GONE);
                String cityCountry = city + " (" + country + ")";
                binding.itemAdd.textCity.setText(cityCountry);
                binding.itemAdd.textAddressDetail.setText(address);
                binding.itemAdd.containerAddress.setVisibility(View.VISIBLE);
                binding.itemAdd.icEdit.setVisibility(View.VISIBLE);
                binding.itemAdd.editTextCity.getText().clear();
                binding.itemAdd.editTextCountry.getText().clear();
                binding.itemAdd.editTextDetail.getText().clear();
                break;
            case "gender":
                String gender = profilePresenter.getUserModel().getGender();
                preferenceManager.putString(Constants.KEY_GENDER,gender);
                binding.textGender.setText(gender);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.containerGenderBtn.setVisibility(View.GONE);
                binding.spinnerGender.setVisibility(View.GONE);
                binding.textGender.setVisibility(View.VISIBLE);
                break;
            case "birthday":
                String birthday = profilePresenter.getUserModel().getBirthday();
                preferenceManager.putString(Constants.KEY_BIRTHDAY,birthday);
                binding.textBirthday.setText(birthday);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.containerBirthdayBtn.setVisibility(View.GONE);
                binding.editBirthday.setVisibility(View.GONE);
                binding.textBirthday.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onEditProfileError() {
        showToast("Cập nhật hồ sơ thất bại, hãy thử lại!");
    }

    @Override
    public void onGetProfileSuccess() {
        UserModel userModel = profilePresenter.getUserModel();

        binding.include.textName.setText(userModel.getUsername());
        preferenceManager.putString(Constants.KEY_NAME,userModel.getUsername());

        binding.include.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
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
            binding.itemAdd.textCity.setText(city);
            binding.itemAdd.textAddressDetail.setText(userModel.getAddress());
        }
        if (userModel.getEmail() != null){
            preferenceManager.putString(Constants.KEY_EMAIL, userModel.getEmail());

            binding.itemInfo.textPhone.setText(userModel.getEmail());
            binding.itemInfo.textPhone.setVisibility(View.VISIBLE);
            binding.itemInfo.btnAdd.setVisibility(View.GONE);
            binding.itemInfo.btnHide.setVisibility(View.GONE);
            binding.itemInfo.icEdit.setVisibility(View.VISIBLE);
        }

        binding.itemEmail.textEmail.setText(userModel.getPhonenumber());
        preferenceManager.putString(Constants.KEY_PHONE,userModel.getPhonenumber());
        binding.swipeLayout.setRefreshing(false);

    }

    @Override
    public void onGetProfileError() {
        binding.swipeLayout.setRefreshing(false);
        showToast("Lấy dữ liệu cá nhân thất bại");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (profilePresenter.getDisposable() != null){
            for (int i = 0; i < profilePresenter.getDisposable().size(); i++ ){
                profilePresenter.getDisposable().get(i).dispose();
            }
        }
    }
}