package com.example.chatthem.contacts.manage_request_friend.presenter;

public class ReceiveReqContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {
        void getReceiveReqSuccess();
        void getReceiveReqFail();
    }
}
