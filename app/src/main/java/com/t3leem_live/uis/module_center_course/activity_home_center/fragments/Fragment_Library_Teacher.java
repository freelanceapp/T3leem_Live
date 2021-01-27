package com.t3leem_live.uis.module_center_course.activity_home_center.fragments;

import android.content.Context;
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
import com.t3leem_live.adapters.LibraryAdapter;
import com.t3leem_live.databinding.FragmentLibraryTeacherBinding;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_center_course.activity_home_center.CenterHomeActivity;
import com.t3leem_live.uis.module_general.activity_library_details.LibraryDetailsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Library_Teacher extends Fragment {
    private FragmentLibraryTeacherBinding binding;
    private LibraryAdapter adapter;
    private List<StageClassModel> stageClassModelList;
    private CenterHomeActivity activity;
    private SkeletonScreen skeletonScreen;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (CenterHomeActivity) context;
    }
    public static Fragment_Library_Teacher newInstance(){
        return new Fragment_Library_Teacher();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_library_teacher,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        stageClassModelList = new ArrayList<>();
        adapter = new LibraryAdapter(stageClassModelList,activity,this);
        binding.recView.setLayoutManager(new GridLayoutManager(activity,2));
        binding.recView.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        getLibraries();
    }

    private void getLibraries()
    {
        Api.getService(Tags.base_url)
                .getLibraries()
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            stageClassModelList.clear();
                            stageClassModelList.addAll(response.body().getData());
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
        Intent intent = new Intent(activity, LibraryDetailsActivity.class);
        intent.putExtra("data",classModel);
        startActivity(intent);
    }
}
