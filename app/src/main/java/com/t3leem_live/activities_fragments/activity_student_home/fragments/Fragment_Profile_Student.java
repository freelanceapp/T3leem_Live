package com.t3leem_live.activities_fragments.activity_student_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_classes.ClassesActivity;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
import com.t3leem_live.adapters.LibraryAdapter;
import com.t3leem_live.databinding.FragmentLibraryStudentBinding;
import com.t3leem_live.databinding.FragmentProfileStudentBinding;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile_Student extends Fragment {
    private FragmentProfileStudentBinding binding;
    private StudentHomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;

    public static Fragment_Profile_Student newInstance(){
        return new Fragment_Profile_Student();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_student,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (StudentHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        updateUiUserData();

        binding.logout.setOnClickListener(view -> activity.logout());
    }

    private void updateUiUserData() {
        String data="";
        if (userModel.getData().getStage_fk()!=null){
            if (lang.equals("ar")){
                data += userModel.getData().getStage_fk().getTitle();

            }else {
                data += userModel.getData().getStage_fk().getTitle_en();

            }
        }
        if (userModel.getData().getClass_fk()!=null){
            data +=",";
            if (lang.equals("ar")){
                data += userModel.getData().getClass_fk().getTitle();

            }else {
                data += userModel.getData().getClass_fk().getTitle_en();

            }
        }
        if (userModel.getData().getDepartment_fk()!=null){

            data +=",";
            if (lang.equals("ar")){
                data += userModel.getData().getDepartment_fk().getTitle();

            }else {
                data += userModel.getData().getDepartment_fk().getTitle_en();

            }
        }

        binding.tvStageClassDepartment.setText(data);
    }


}
