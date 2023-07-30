package com.example.chatthem.authentication.login.presenter;

public class LogInContract {
    public interface PresenterInterface {
        // Interface này dành cho LoginPresenter
    }
    public interface ViewInterface {
        // Interface này dành cho LoginActivity
        void onLoginError();
        void onLoginSuccess();
        void onLoginFail();

        void onGetPrivateKeyError();
    }
}
