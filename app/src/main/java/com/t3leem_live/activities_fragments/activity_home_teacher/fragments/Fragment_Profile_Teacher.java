package com.t3leem_live.activities_fragments.activity_home_teacher.fragments;

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
import com.t3leem_live.activities_fragments.activity_contact_us.ContactUsActivity;
import com.t3leem_live.activities_fragments.activity_home_teacher.TeacherHomeActivity;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
import com.t3leem_live.activities_fragments.activity_student_sign_up.StudentSignUpActivity;
import com.t3leem_live.activities_fragments.activity_teacher_create_students_chat.TeacherCreateStudentsChatActivity;
import com.t3leem_live.activities_fragments.activity_teacher_groups_chat.TeacherGroupChatActivity;
import com.t3leem_live.activities_fragments.activity_teacher_sign_up.TeacherSignUpActivity;
import com.t3leem_live.databinding.FragmentProfileStudentBinding;
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

    public static Fragment_Profile_Teacher newInstance(){
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_teacher,container,false);
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
            Intent intent = new Intent(activity, TeacherSignUpActivity.class);
            startActivityForResult(intent,100);
        });


        binding.llCreateGroupChat.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherGroupChatActivity.class);
            startActivity(intent);
        });

        binding.llCreateStudentChat.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherCreateStudentsChatActivity.class);
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
