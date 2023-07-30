package com.example.chatthem.utilities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.chatthem.chats.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Helpers {

    public static void main(String[] args) {
//        formatTime("2023-06-07T11:18:52.843+00:00", true);
//        formatTime("2023-07-30T09:18:52.843+07:00", true);
    }
    public static String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }else{
            return null;
        }
    }
    public static String encodeCoverImage(Bitmap bitmap){
        int previewWidth = 600;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }else{
            return null;
        }
    }
    public static Bitmap getBitmapFromEncodedString(String encodedImage){
        if (encodedImage != null){
            byte[] bytes = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }else {
            return null;
        }

    }
    public static void setupUI(View view, Activity activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, activity);
            }
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            if (activity.getCurrentFocus() != null){

                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
                activity.getCurrentFocus().clearFocus();
            }

        }
    }

    public static String formatTime(String time, boolean inChat){
        // Định nghĩa định dạng của đầu vào (UTC)
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Định nghĩa định dạng của đầu ra (giờ địa phương)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
        dateFormat.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        hourFormat.setTimeZone(TimeZone.getDefault());

        Date now = new Date();
        String nowLocalDateTime = dateFormat.format(now);
        // Chuyển đổi sang thời gian địa phương
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            ZoneId localZone = ZoneId.systemDefault();
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            DateTimeFormatter hourTimeFormatter = DateTimeFormatter.ofPattern("HH:mm a");
////            String localDateTimeString = time.atZoneSameInstant(localZone).format(formatter);
//            String localDateTime = time.toLocalTime().format(dateTimeFormatter);
//
//            if (localDateTime.equals(nowLocalDateTime)){
//                return time.toLocalTime().format(hourTimeFormatter);
//            }else {
//                return localDateTime;
//            }
//        }else {

            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd-MM");

            try {
                Date date = inputFormat.parse(time);
                String localDateTime = dateFormat.format(date);
                if (inChat){
                    return outputFormat.format(date);
                }else {
                    if (localDateTime.equals(nowLocalDateTime)){
                        return hourFormat.format(date);
                    }else {
                        return localDateTime;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return time.toString();
            }
//        }

    }

    public static List<UserModel> checkStringContain(String searchStr, List<UserModel> userModelList){
        List<UserModel> result = new ArrayList<>();
        for (UserModel u : userModelList){
            if (u.getUsername().contains(searchStr)){
                result.add(u);
            }else if (u.getPhonenumber().contains(searchStr)){
                result.add(u);
            }
        }
        return result;

//        boolean containsAllChars = true;
//        for (char c : searchStr.toCharArray()) {
//            boolean charFound = false;
//            for (String str : stringList) {
//                if (str.contains(String.valueOf(c))) {
//                    charFound = true;
//                    break;
//                }
//            }
//            if (!charFound) {
//                containsAllChars = false;
//                break;
//            }
//        }
//
//        return containsAllChars;
    }
}
