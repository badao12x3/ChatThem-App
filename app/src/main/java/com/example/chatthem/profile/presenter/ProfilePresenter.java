package com.example.chatthem.profile.presenter;

import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.networking.APIServices;
import com.example.chatthem.profile.model.ChangePassResponse;
import com.example.chatthem.profile.model.EditProfileResponse;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenter {

    private String token;
    private final ProfileContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private ArrayList<Disposable> disposable;
    private UserModel userModel;

    public ArrayList<Disposable> getDisposable() {
        return disposable;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public ProfilePresenter(ProfileContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        token = "Bearer " + preferenceManager.getString(Constants.KEY_TOKEN);
        disposable = new ArrayList<>();
    }

    public void editAvatar(String avatar) {
        APIServices.apiServices.editAvatar(token, avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onEditProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onEditProfileSuccess("avatar");
                    }
                });
    }

    public void editUsername(String username) {
        APIServices.apiServices.editUsername(token, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onEditProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onEditProfileSuccess("username");
                    }
                });
    }
    public void editCoverImage(String cover_image) {
        APIServices.apiServices.editCoverImg(token, cover_image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onEditProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onEditProfileSuccess("cover_image");
                    }
                });
    }

    public void editEmail(String email) {
        APIServices.apiServices.editEmail(token, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onEditProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onEditProfileSuccess("email");
                    }
                });
    }

    public void editAddress(String address,String city,String country) {
        APIServices.apiServices.editAddress(token, address,country, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onEditProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onEditProfileSuccess("address");
                    }
                });
    }

    public void editGender(String gender) {
        APIServices.apiServices.editGender(token, gender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onEditProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onEditProfileSuccess("gender");
                    }
                });
    }
    public void editBirthday(String birthday) {
        APIServices.apiServices.editBirthday(token, birthday)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onEditProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onEditProfileSuccess("birthday");
                    }
                });
    }

        public void getProfile() {
        APIServices.apiServices.getMyProfile(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull EditProfileResponse editProfileResponse) {
                        userModel = editProfileResponse.getData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        viewInterface.onGetProfileError();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.onGetProfileSuccess();
                    }
                });
    }


}
