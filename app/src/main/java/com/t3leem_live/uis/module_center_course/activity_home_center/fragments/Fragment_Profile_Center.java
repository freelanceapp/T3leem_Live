package com.t3leem_live.uis.module_center_course.activity_home_center.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.t3leem_live.databinding.FragmentProfileCenterBinding;
import com.t3leem_live.databinding.FragmentProfileTeacherBinding;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.uis.module_center_course.activity_center_sign_up.CenterCourseSignUpActivity;
import com.t3leem_live.uis.module_center_course.activity_home_center.CenterHomeActivity;
import com.t3leem_live.uis.module_general.activity_chat_rooms.ChatRoomsActivity;
import com.t3leem_live.uis.module_general.activity_contact_us.ContactUsActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_create_students_chat.TeacherCreateStudentsChatActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_groups_chat.TeacherGroupChatActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_sign_up.TeacherSignUpActivity;

import io.paperdb.Paper;

public class Fragment_Profile_Center extends Fragment {
    private FragmentProfileCenterBinding binding;
    private CenterHomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;

    public static Fragment_Profile_Center newInstance(){
        return new Fragment_Profile_Center();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (CenterHomeActivity) context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_center,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");

        binding.logout.setOnClickListener(view -> activity.logout());
        binding.llContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactUsActivity.class);
            startActivity(intent);
        });

        binding.llEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(activity, CenterCourseSignUpActivity.class);
            startActivityForResult(intent,100);
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
