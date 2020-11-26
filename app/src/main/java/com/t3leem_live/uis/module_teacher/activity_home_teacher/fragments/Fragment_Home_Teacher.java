package com.t3leem_live.uis.module_teacher.activity_home_teacher.fragments;

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

import com.t3leem_live.R;
import com.t3leem_live.uis.module_teacher.activity_home_teacher.TeacherHomeActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_exams.TeacherExamsActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_group.TeacherGroupActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_students.TeacherStudentsActivity;
import com.t3leem_live.adapters.SliderAdapter;
import com.t3leem_live.databinding.FragmentHomeTeacherBinding;
import com.t3leem_live.models.SliderDataModel;
import com.t3leem_live.models.SliderModel;
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

public class Fragment_Home_Teacher extends Fragment {
    private FragmentHomeTeacherBinding binding;
    private TeacherHomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String lang;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private Task task;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (TeacherHomeActivity) context;
    }

    public static Fragment_Home_Teacher newInstance(){
        return new Fragment_Home_Teacher();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_teacher,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        sliderModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.cardStudent.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherStudentsActivity.class);
            startActivity(intent);
        });
        binding.cardTest.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherExamsActivity.class);
            startActivity(intent);
        });

        binding.cardGroup.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TeacherGroupActivity.class);
            startActivity(intent);
        });


        getSlider();





    }

    private void getSlider() {
        Api.getService(Tags.base_url)
                .getSlider()
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                       try {
                           binding.progBar.setVisibility(View.GONE);

                       }catch (Exception e){}

                        if (response.isSuccessful() && response.body() != null) {
                            sliderModelList.clear();
                            sliderModelList.addAll(response.body().getData());
                            if (sliderModelList.size()>0){
                                binding.pager.setVisibility(View.VISIBLE);
                                binding.llNoSliderData.setVisibility(View.GONE);

                                updateSliderUi();

                            }else {
                                binding.llNoSliderData.setVisibility(View.VISIBLE);
                                binding.pager.setVisibility(View.GONE);

                            }

                        } else {
                            binding.llNoSliderData.setVisibility(View.VISIBLE);
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
                            binding.llNoSliderData.setVisibility(View.VISIBLE);

                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }


    private void updateSliderUi()
    {
        try {
            if (sliderModelList.size() > 0) {
                sliderAdapter = new SliderAdapter(sliderModelList, activity);
                binding.pager.setAdapter(sliderAdapter);
                binding.tab.setupWithViewPager(binding.pager);
                if (sliderModelList.size() > 1) {
                    timer = new Timer();
                    task = new Task();
                    timer.scheduleAtFixedRate(task, 3000, 3000);
                }
            }else {
                binding.llNoSliderData.setVisibility(View.VISIBLE);
                binding.pager.setVisibility(View.GONE);
            }
        }catch (Exception e){}

    }

    public void stopTimer() {
        if (timer!=null){
            timer.purge();
            timer.cancel();
        }
        if (task!=null){
            task.cancel();
        }
    }


    private  class Task extends TimerTask {

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.pager.getCurrentItem()<sliderModelList.size()-1){
                        binding.pager.setCurrentItem(binding.pager.getCurrentItem()+1);
                    }else {
                        binding.pager.setCurrentItem(0,false);
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
}
