package com.t3leem_live.uis.module_student.activity_student_home.fragments;

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
import com.t3leem_live.adapters.StudentSliderAdapter;
import com.t3leem_live.models.SliderDataModel;
import com.t3leem_live.models.SliderModel;
import com.t3leem_live.uis.module_student.activity_student_home.StudentHomeActivity;
import com.t3leem_live.uis.module_student.activity_subject_tutorial.SubjectTutorialActivity;
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
import java.util.Timer;
import java.util.TimerTask;

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
    private StudentSliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private Task task;

    public static Fragment_Home_Student newInstance() {
        return new Fragment_Home_Student();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_student, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        sliderModelList = new ArrayList<>();
        activity = (StudentHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        stageClassModelList = new ArrayList<>();
        adapter = new StageAdapter(stageClassModelList, activity, this);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recView.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(this::getData);
        getData();

    }

    private void getData() {
        if (userModel != null) {
            getSubjects();
            getSlider();
        }
    }

    private void getSubjects() {
        int stage_id = 0;
        int class_id = 0;
        String department_id = null;

        if (userModel.getData().getStage_fk() != null) {
            stage_id = userModel.getData().getStage_fk().getId();

        }
        if (userModel.getData().getClass_fk() != null) {
            class_id = userModel.getData().getClass_fk().getId();

        }

        if (userModel.getData().getDepartment_fk() != null) {
            department_id = String.valueOf(userModel.getData().getDepartment_fk().getId());

        }


        Api.getService(Tags.base_url)
                .getSubject(stage_id, class_id, department_id)
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        binding.swipeRefresh.setRefreshing(false);
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            stageClassModelList.clear();
                            stageClassModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                            if (stageClassModelList.size() > 0) {
                                binding.tvNoSubject.setVisibility(View.GONE);
                            } else {
                                binding.tvNoSubject.setVisibility(View.VISIBLE);

                            }
                        } else {
                            skeletonScreen.hide();
                            binding.swipeRefresh.setRefreshing(false);

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
                            binding.swipeRefresh.setRefreshing(false);

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


    private void getSlider() {
        Api.getService(Tags.base_url)
                .getStudentSlider()
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                        } catch (Exception e) {
                        }

                        if (response.isSuccessful() && response.body() != null) {
                            sliderModelList.clear();
                            sliderModelList.addAll(response.body().getData());
                            if (sliderModelList.size() > 0) {
                                binding.pager.setVisibility(View.VISIBLE);
                                updateSliderUi();

                            } else {
                                binding.pager.setVisibility(View.GONE);

                            }

                        } else {
                            binding.pager.setVisibility(View.GONE);
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SliderDataModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                            binding.progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }


    private void updateSliderUi() {
        try {
            if (sliderModelList.size() > 0) {
                sliderAdapter = new StudentSliderAdapter(sliderModelList, activity);
                binding.pager.setAdapter(sliderAdapter);

                binding.pager.setClipToPadding(false);
                binding.pager.setPadding(156, 8, 156, 8);
                binding.pager.setPageMargin(24);

                if (sliderModelList.size() > 1) {
                    timer = new Timer();
                    task = new Task();
                    timer.scheduleAtFixedRate(task, 3000, 3000);
                }
            } else {
                binding.pager.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

    }



    public void stopTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }


    private class Task extends TimerTask {

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.pager.getCurrentItem() < sliderModelList.size() - 1) {
                        binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                    } else {
                        binding.pager.setCurrentItem(0, false);
                    }
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
    }


    public void setItemData(StageClassModel classModel) {
        Intent intent = new Intent(activity, SubjectTutorialActivity.class);
        intent.putExtra("data", classModel);
        startActivity(intent);
    }
}
