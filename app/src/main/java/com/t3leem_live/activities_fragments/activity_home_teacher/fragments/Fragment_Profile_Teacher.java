package com.t3leem_live.activities_fragments.activity_home_teacher.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_home_teacher.TeacherHomeActivity;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_teacher,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (TeacherHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.logout.setOnClickListener(view -> activity.logout());
    }



}
