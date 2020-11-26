package com.t3leem_live.uis.module_parent.activity_home_parent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_about_app.AboutAppActivity;
import com.t3leem_live.uis.module_general.activity_chat_rooms.ChatRoomsActivity;
import com.t3leem_live.uis.module_general.activity_contact_us.ContactUsActivity;
import com.t3leem_live.uis.module_parent.activity_home_parent.ParentHomeActivity;
import com.t3leem_live.uis.module_parent.activity_parent_sign_up.ParentSignUpActivity;
import com.t3leem_live.databinding.FragmentProfileParentBinding;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Profile_Parent extends Fragment {
    private FragmentProfileParentBinding binding;
    private ParentHomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;

    public static Fragment_Profile_Parent newInstance(){
        return new Fragment_Profile_Parent();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_parent,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (ParentHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);

        binding.logout.setOnClickListener(view -> activity.logout());
        binding.llContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactUsActivity.class);
            startActivity(intent);
        });

        binding.llAbout.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AboutAppActivity.class);
            startActivity(intent);
        });

        binding.llUpdateProfile.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ParentSignUpActivity.class);
            startActivityForResult(intent,100);
        });

        binding.llChat.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ChatRoomsActivity.class);
            startActivity(intent);
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==100&&resultCode== Activity.RESULT_OK){
            userModel = preferences.getUserData(activity);
            binding.setModel(userModel);
        }
    }
}
