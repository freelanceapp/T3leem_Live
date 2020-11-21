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


import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_classes.ClassesActivity;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
import com.t3leem_live.activities_fragments.activity_student_sign_up.StudentSignUpActivity;
import com.t3leem_live.activities_fragments.activity_subject_tutorial.SubjectTutorialActivity;
import com.t3leem_live.adapters.StageAdapter;
import com.t3leem_live.databinding.FragmentHomeStudentBinding;
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

public class Fragment_Home_Student extends Fragment {
    private FragmentHomeStudentBinding binding;
    private StageAdapter adapter;
    private List<StageClassModel> stageClassModelList;
    private StudentHomeActivity activity;
    private SkeletonScreen skeletonScreen;
    private UserModel userModel;
    private Preferences preferences;
    private String lang;

    public static Fragment_Home_Student newInstance(){
        return new Fragment_Home_Student();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_student,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        activity = (StudentHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");

        stageClassModelList = new ArrayList<>();
        adapter = new StageAdapter(stageClassModelList,activity,this);
        binding.recView.setLayoutManager(new GridLayoutManager(activity,2));
        binding.recView.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        if (userModel!=null){
            getSubjects();

        }
    }

    private void getSubjects()
    {
        int stage_id =0;
        int class_id =0;
        String department_id=null;
        String data="";
        if (userModel.getData().getStage_fk()!=null){
            stage_id = userModel.getData().getStage_fk().getId();
            if (lang.equals("ar")){
                data += userModel.getData().getStage_fk().getTitle();

            }else {
                data += userModel.getData().getStage_fk().getTitle_en();

            }
        }
        if (userModel.getData().getClass_fk()!=null){
            class_id = userModel.getData().getClass_fk().getId();
            data +=",";
            if (lang.equals("ar")){
                data += userModel.getData().getClass_fk().getTitle();

            }else {
                data += userModel.getData().getClass_fk().getTitle_en();

            }
        }

        if (userModel.getData().getDepartment_fk()!=null){
            department_id = String.valueOf(userModel.getData().getDepartment_fk().getId());

            data +=",";
            if (lang.equals("ar")){
                data += userModel.getData().getDepartment_fk().getTitle();

            }else {
                data += userModel.getData().getDepartment_fk().getTitle_en();

            }
        }

        binding.tvStageClassDepartment.setText(data);

        Api.getService(Tags.base_url)
                .getSubject(stage_id,class_id,department_id)
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            stageClassModelList.clear();
                            stageClassModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                            if (stageClassModelList.size()>0){
                                binding.tvNoSubject.setVisibility(View.GONE);
                            }else {
                                binding.tvNoSubject.setVisibility(View.VISIBLE);

                            }
                        } else {
                            skeletonScreen.hide();

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StageDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    public void setItemData(StageClassModel classModel) {
        Intent intent = new Intent(activity, SubjectTutorialActivity.class);
        intent.putExtra("data",classModel);
        startActivity(intent);
    }
}
