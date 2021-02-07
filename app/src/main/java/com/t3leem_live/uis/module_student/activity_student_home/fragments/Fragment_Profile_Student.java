package com.t3leem_live.uis.module_student.activity_student_home.fragments;

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
import com.t3leem_live.uis.module_general.activity_chat_rooms.ChatRoomsActivity;
import com.t3leem_live.uis.module_general.activity_view.ViewActivity;
import com.t3leem_live.uis.module_student.activity_available_teacher.AvailableTeacherActivity;
import com.t3leem_live.uis.module_student.activity_follow_teacher.FollowTeacherActivity;
import com.t3leem_live.uis.module_student.activity_my_reservations.MyReservationActivity;
import com.t3leem_live.uis.module_student.activity_student_center.StudentCenterActivity;
import com.t3leem_live.uis.module_student.activity_student_home.StudentHomeActivity;
import com.t3leem_live.uis.module_student.activity_student_sign_up.StudentSignUpActivity;
import com.t3leem_live.databinding.FragmentProfileStudentBinding;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.uis.module_student.activity_joint_center.JointCenterActivity;

import io.paperdb.Paper;

public class Fragment_Profile_Student extends Fragment {
    private FragmentProfileStudentBinding binding;
    private StudentHomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;

    public static Fragment_Profile_Student newInstance() {
        return new Fragment_Profile_Student();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_student, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (StudentHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        updateUiUserData();

        binding.logout.setOnClickListener(view -> activity.logout());


        binding.llJointCenter.setOnClickListener(view -> {
            Intent intent = new Intent(activity, JointCenterActivity.class);
            startActivity(intent);
        });

        binding.llUpdateProfile.setOnClickListener(view -> {
            Intent intent = new Intent(activity, StudentSignUpActivity.class);
            startActivityForResult(intent, 100);
        });

        binding.llChat.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ChatRoomsActivity.class);
            startActivity(intent);
        });

        binding.llCenter.setOnClickListener(view -> {
            Intent intent = new Intent(activity, StudentCenterActivity.class);
            startActivity(intent);
        });

        binding.llFollowTeacher.setOnClickListener(view -> {
            Intent intent = new Intent(activity, FollowTeacherActivity.class);
            startActivity(intent);
        });
        binding.llAvailableTeacher.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AvailableTeacherActivity.class);
            startActivity(intent);
        });
        binding.llMyReservations.setOnClickListener(view -> {
            Intent intent = new Intent(activity, MyReservationActivity.class);
            startActivity(intent);
        });
        binding.llQuestions.setOnClickListener(view -> {

            String department_id = "";
            if (userModel.getData().getDepartment_fk() != null) {
                department_id = String.valueOf(userModel.getData().getDepartment_fk().getId());
            }
            String url = "http://teach.creativeshare.sa/question-bank?student_id=" + userModel.getData().getId() + "&stage_id=" + userModel.getData().getStage_fk().getId() + "&class_id=" + userModel.getData().getClass_fk().getId() + "&department_id=" + department_id + "&view_type=webView";

            navigateToWebViewActivity(url);
        });

    }

    private void updateUiUserData() {
        String data = "";
        if (userModel.getData().getStage_fk() != null) {
            if (lang.equals("ar")) {
                data += userModel.getData().getStage_fk().getTitle();

            } else {
                data += userModel.getData().getStage_fk().getTitle_en();

            }
        }
        if (userModel.getData().getClass_fk() != null) {
            data += ",";
            if (lang.equals("ar")) {
                data += userModel.getData().getClass_fk().getTitle();

            } else {
                data += userModel.getData().getClass_fk().getTitle_en();

            }
        }
        if (userModel.getData().getDepartment_fk() != null) {

            data += ",";
            if (lang.equals("ar")) {
                data += userModel.getData().getDepartment_fk().getTitle();

            } else {
                data += userModel.getData().getDepartment_fk().getTitle_en();

            }
        }

        binding.tvStageClassDepartment.setText(data);
    }


    private void navigateToWebViewActivity(String url) {
        Intent intent = new Intent(activity, ViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            userModel = preferences.getUserData(activity);
            binding.setModel(userModel);
            updateUiUserData();
        }
    }

   public void updateUserData(){
       userModel = preferences.getUserData(activity);
       binding.setModel(userModel);
   }
}
