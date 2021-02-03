package com.t3leem_live.uis.module_student.activity_student_center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.StudentCenterAdapter;
import com.t3leem_live.adapters.StudentTeachersAdapter;
import com.t3leem_live.databinding.ActivityStudentCenterBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StudentCenterModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.TeachersDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_other_students.OtherStudentsActivity;
import com.t3leem_live.uis.module_student.activity_student_center_groups.StudentCenterGroupsActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers.StudentTeachersActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_video.TeacherVideoActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentCenterActivity extends AppCompatActivity {
    private ActivityStudentCenterBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StudentCenterAdapter adapter;
    private List<StudentCenterModel> studentCenterModelList;
    private SkeletonScreen skeletonScreen;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_center);
        initView();
    }


    private void initView() {
        studentCenterModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentCenterAdapter(studentCenterModelList, this);
        binding.recView.setAdapter(adapter);
        new Handler(Looper.myLooper()).postDelayed(() -> {
            binding.scrollView.scrollTo(0, 0);
        }, 200);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        binding.llBack.setOnClickListener(view -> {
            finish();
        });


        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(this::getCenters);
        getCenters();

    }

    private void getCenters() {

        Api.getService(Tags.base_url).getStudentCenter(userModel.getData().getStage_fk().getId())
                .enqueue(new Callback<List<StudentCenterModel>>() {
                    @Override
                    public void onResponse(Call<List<StudentCenterModel>> call, Response<List<StudentCenterModel>> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                studentCenterModelList.clear();

                                if (response.body().size() > 0) {
                                    binding.tvNoCenters.setVisibility(View.GONE);
                                    studentCenterModelList.addAll(response.body());
                                } else {
                                    binding.tvNoCenters.setVisibility(View.VISIBLE);

                                }

                                adapter.notifyDataSetChanged();


                            }
                        } else {
                            binding.swipeRefresh.setRefreshing(false);

                            skeletonScreen.hide();
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<List<StudentCenterModel>> call, Throwable t) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);

                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(StudentCenterActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(StudentCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setItemData(StudentCenterModel model, String group_share) {
        Intent intent;
        if (group_share.equals("group")) {
            intent = new Intent(this, StudentCenterGroupsActivity.class);
            intent.putExtra("data", model);
        } else {
            intent = new Intent(this, OtherStudentsActivity.class);
            intent.putExtra("data", model.getId());
            intent.putExtra("data2", "center");
        }
        startActivity(intent);
    }


}