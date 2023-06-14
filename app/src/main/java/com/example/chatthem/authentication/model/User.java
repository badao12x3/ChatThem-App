package com.example.chatthem.authentication.model;

import android.util.Patterns;

import java.util.Objects;

public class User {
    private String phonenumber, password, re_password, avatar, publicKey, username, id, coverImage;

    public User(String phone, String password, String re_password, String avatar, String username) {
        this.phonenumber = phone;
        this.password = password;
        this.re_password = re_password;
        this.avatar = avatar;
        this.username = username;
    }

    public User(String phone, String password) {
        this.phonenumber = phone;
        this.password = password;
    }


    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getPhone() {
        return phonenumber;
    }

    public void setPhone(String phone) {
        this.phonenumber = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isValidNumOfPhone(){
        return phonenumber.length() == 10;
    }
    public Boolean isValidPhone(){
//        if (phone.charAt(0) == '0'){
//            Log.d("hp", "no");
//        }
//        Log.d("hp", String.valueOf(phone.charAt(0)));
//        String regex = "^0[0-9]{9}$";
//        return Pattern.matches(regex, phone);
        return Patterns.PHONE.matcher(phonenumber).matches() && phonenumber.startsWith("0");
    }
    public Boolean isValidPass(){
        return password.length() > 5;
    }
    public Boolean isConfirmPass(){
        return Objects.equals(password, re_password);
    }
}
