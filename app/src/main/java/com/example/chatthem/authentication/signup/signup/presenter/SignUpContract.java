package com.example.chatthem.authentication.signup.signup.presenter;

public class SignUpContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {
        void onPhoneExistSignUpFail();

        void onSignUpError();

        void onSignUpSuccess();

        void onSavePriKeySignUpError();

        void onGenKeyPairSignUpError();
        // Interface này dành cho SignUpActivity
    }
}
