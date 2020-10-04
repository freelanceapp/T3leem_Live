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
import com.t3leem_live.adapters.StageAdapter;
import com.t3leem_live.databinding.FragmentHomeStudentBinding;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home_Student extends Fragment {
    private FragmentHomeStudentBinding binding;
    private StageAdapter adapter;
    private List<StageClassModel> stageClassModelList;
    private StudentHomeActivity activity;
    private SkeletonScreen skeletonScreen;

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

        getStage();
    }

    private void getStage()
    {
        Api.getService(Tags.base_url)
                .getStage()
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            stageClassModelList.clear();
                            stageClassModelList.addAll(response.body().getData());
                            Log.e("size",stageClassModelList.size()+"_");
                            adapter.notifyDataSetChanged();
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
        Intent intent = new Intent(activity, ClassesActivity.class);
        intent.putExtra("data",classModel);
        startActivity(intent);
    }
}
