package com.example.chatthem.contacts.manage_request_friend.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatthem.R;
import com.example.chatthem.chats.model.UserModel;
import com.example.chatthem.contacts.manage_request_friend.presenter.ReceiveReqContract;
import com.example.chatthem.contacts.manage_request_friend.presenter.ReceiveReqPresenter;
import com.example.chatthem.databinding.FragmentReceiveReqBinding;
import com.example.chatthem.databinding.FragmentSendReqBinding;
import com.example.chatthem.utilities.Constants;
import com.example.chatthem.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiveReqFragment#} factory method to
 * create an instance of this fragment.
 */
public class ReceiveReqFragment extends Fragment implements ReceiveReqContract.ViewInterface {
    private FragmentReceiveReqBinding binding;
    private PreferenceManager preferenceManager;
    private ReceiveReqPresenter presenter;
    private ReceiveReqAdapter adapter;
    private List<UserModel> userModelList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceiveReqBinding.inflate(inflater, container, false);

        // Get the root view from the binding
        View rootView = binding.getRoot();
        preferenceManager = new PreferenceManager(requireContext());
        presenter = new ReceiveReqPresenter(this);

        presenter.getReceiveReq(preferenceManager.getString(Constants.KEY_TOKEN));


        return rootView;
    }

    @Override
    public void getReceiveReqSuccess() {
        userModelList = new ArrayList<>();
        userModelList = presenter.getUserModelList();
        adapter = new ReceiveReqAdapter(userModelList, presenter);

        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerview.setAdapter(adapter);
    }

    @Override
    public void getReceiveReqFail() {

        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Lấy dữ liệu fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter.getDisposable() != null)
            presenter.getDisposable().dispose();
    }
}