package com.t3leem_live.uis.module_teacher.activity_teacher_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_chat_rooms.ChatRoomsActivity;
import com.t3leem_live.uis.module_general.activity_contact_us.ContactUsActivity;
import com.t3leem_live.uis.module_parent.activity_home_parent.ParentHomeActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_home.TeacherHomeActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_create_students_chat.TeacherCreateStudentsChatActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_groups_chat.TeacherGroupChatActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_sign_up.TeacherSignUpActivity;
import com.t3leem_live.databinding.FragmentProfileTeacherBinding;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Profile_Teacher extends Fragment {
    private FragmentProfileTeacherBinding binding;
    private TeacherHomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;

    public static Fragment_Profile_Teacher newInstance() {
        return new Fragment_Profile_Teacher();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (TeacherHomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_teacher, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");

        binding.logout.setOnClickListener(view -> activity.logout());


        binding.llEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherSignUpActivity.class);
            startActivityForResult(intent, 100);
        });

        binding.llMySon.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ParentHomeActivity.class);
            startActivity(intent);

        });


        binding.llCreateGroupChat.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherGroupChatActivity.class);
            startActivity(intent);
        });

        binding.llCreateStudentChat.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherCreateStudentsChatActivity.class);
            startActivity(intent);

        });

        binding.llChat.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ChatRoomsActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            updateUserData();
        }
    }

    public void updateUserData() {
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Log.e("data",userModel.getData().getIs_parent()+"__");
    }

}
