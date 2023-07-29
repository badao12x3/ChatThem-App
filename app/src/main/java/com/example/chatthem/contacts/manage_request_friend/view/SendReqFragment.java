package com.example.chatthem.contacts.manage_request_friend.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.contacts.manage_request_friend.presenter.ReceiveReqPresenter;
import com.example.chatthem.contacts.manage_request_friend.presenter.SendReqContract;
import com.example.chatthem.contacts.manage_request_friend.presenter.SendReqPresenter;
import com.example.chatthem.databinding.FragmentSendReqBinding;
import com.example.chatthem.databinding.FragmentSignupBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendReqFragment#} factory method to
 * create an instance of this fragment.
 */
public class SendReqFragment extends Fragment implements SendReqContract.ViewInterface {

    private FragmentSendReqBinding binding;
    private PreferenceManager preferenceManager;
    private SendReqPresenter presenter;
    private SendReqAdapter adapter;
    private List<UserModel> userModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSendReqBinding.inflate(inflater, container, false);

        // Get the root view from the binding
        View rootView = binding.getRoot();
        preferenceManager = new PreferenceManager(requireContext());
        presenter = new SendReqPresenter(this);

        presenter.getSendReq(preferenceManager.getString(Constants.KEY_TOKEN));
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void getSendReqSuccess() {
        userModelList = new ArrayList<>();
        userModelList = presenter.getUserModelList();
        adapter = new SendReqAdapter(userModelList, presenter);

        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerview.setAdapter(adapter);
    }

    @Override
    public void getSendReqFail() {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Lấy dữ liệu fail", Toast.LENGTH_SHORT).show();
    }
}