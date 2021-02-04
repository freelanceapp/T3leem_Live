package com.t3leem_live.uis.module_student.activity_student_center_groups;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
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
import com.t3leem_live.adapters.OtherStudentsAdapter;
import com.t3leem_live.adapters.StudentCenterGroupsAdapter;
import com.t3leem_live.databinding.ActivityStudentCenterGroupsBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.StudentCenterModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.models.UsersDataModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_other_students.OtherStudentsActivity;
import com.t3leem_live.uis.module_student.activity_teachers_inside_center.TeachersInsideCenterActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentCenterGroupsActivity extends AppCompatActivity {

    private ActivityStudentCenterGroupsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StudentCenterGroupsAdapter adapter;
    private List<CenterGroupModel> centerGroupModelList;
    private SkeletonScreen skeletonScreen;
    private StudentCenterModel model;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_center_groups);
        binding.executePendingBindings();
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (StudentCenterModel) intent.getSerializableExtra("data");
        Log.e("data",model.getName()+"___"+model.getId());


    }

    private void initView() {
        centerGroupModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.setTitle(model.getName());
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentCenterGroupsAdapter(centerGroupModelList, this);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        new Handler(Looper.myLooper()).postDelayed(() -> {
            binding.scrollView.scrollTo(0, 0);
        }, 200);

        binding.llBack.setOnClickListener(view -> {
            finish();
        });


        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(this::getGroups);
        getGroups();


    }

    private void getGroups() {
        Api.getService(Tags.base_url).getStudentCenterGroups(model.getId())
                .enqueue(new Callback<List<CenterGroupModel>>() {
                    @Override
                    public void onResponse(Call<List<CenterGroupModel>> call, Response<List<CenterGroupModel>> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                centerGroupModelList.clear();

                                if (response.body().size() > 0) {
                                    binding.tvNoGroups.setVisibility(View.GONE);
                                    centerGroupModelList.addAll(response.body());
                                } else {
                                    binding.tvNoGroups.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<List<CenterGroupModel>> call, Throwable t) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);

                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(StudentCenterGroupsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(StudentCenterGroupsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setItemData(CenterGroupModel model) {
        Intent intent = new Intent(this, TeachersInsideCenterActivity.class);
        intent.putExtra("data",model);
        startActivity(intent);
    }
}