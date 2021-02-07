package com.t3leem_live.uis.module_parent.activity_subjects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.t3leem_live.adapters.ParentTeachersAdapter;
import com.t3leem_live.adapters.StageAdapter;
import com.t3leem_live.adapters.StageAdapter2;
import com.t3leem_live.databinding.ActivityParentSonTeacherBinding;
import com.t3leem_live.databinding.ActivitySubjectBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_parent.activity_son_teacher.ParentSonTeacherActivity;
import com.t3leem_live.uis.module_student.activity_student_exam.StudentExamActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_video.TeacherVideoActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectActivity extends AppCompatActivity {

    private ActivitySubjectBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StageAdapter2 adapter;
    private List<StageClassModel> stageClassModelList;
    private SkeletonScreen skeletonScreen;
    private UserModel.User sonModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject);
        binding.executePendingBindings();
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        sonModel = (UserModel.User) intent.getSerializableExtra("data");
    }


    private void initView() {
        stageClassModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StageAdapter2(stageClassModelList, this);
        LinearLayoutManager manager = new GridLayoutManager(this,2);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(adapter);
        binding.recView.setNestedScrollingEnabled(true);
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
        binding.swipeRefresh.setOnRefreshListener(this::getSubjects);
        getSubjects();

    }

    private void getSubjects() {
        int stage_id = 0;
        int class_id = 0;
        String department_id = null;

        if (sonModel.getStage_fk() != null) {
            stage_id = sonModel.getStage_fk().getId();

        }
        if (sonModel.getClass_fk() != null) {
            class_id = sonModel.getClass_fk().getId();

        }

        if (sonModel.getDepartment_fk() != null) {
            department_id = String.valueOf(sonModel.getDepartment_fk().getId());

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
                                Toast.makeText(SubjectActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SubjectActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(SubjectActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SubjectActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    public void setItemData(StageClassModel classModel) {
        Intent intent = new Intent(this, StudentExamActivity.class);
        intent.putExtra("data", classModel);
        startActivity(intent);
    }
}