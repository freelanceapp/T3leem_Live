package com.t3leem_live.uis.module_student.activity_summary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.StudentCenterGroupsAdapter;
import com.t3leem_live.adapters.SummaryAdapter;
import com.t3leem_live.databinding.ActivityStudentCenterGroupsBinding;
import com.t3leem_live.databinding.ActivitySummaryBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StudentCenterModel;
import com.t3leem_live.models.SummaryDataModel;
import com.t3leem_live.models.SummaryModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_student_center_groups.StudentCenterGroupsActivity;
import com.t3leem_live.uis.module_student.activity_subject_tutorial.SubjectTutorialActivity;
import com.t3leem_live.uis.module_student.activity_teachers_inside_center.TeachersInsideCenterActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {
    private ActivitySummaryBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StageClassModel stageClassModel;
    private List<SummaryModel> summaryModelList;
    private SummaryAdapter adapter;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_summary);
        binding.executePendingBindings();
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        summaryModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SummaryAdapter(summaryModelList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(adapter);
        binding.recView.setNestedScrollingEnabled(true);

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
        binding.swipeRefresh.setOnRefreshListener(this::getSummary);

    }

    private void getSummary() {

        Api.getService(Tags.base_url)
                .getSummary(Integer.parseInt(stageClassModel.getStage_id()), Integer.parseInt(stageClassModel.getClass_id()), stageClassModel.getDepartment_id(), stageClassModel.getId())
                .enqueue(new Callback<SummaryDataModel>() {
                    @Override
                    public void onResponse(Call<SummaryDataModel> call, Response<SummaryDataModel> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                summaryModelList.clear();
                                summaryModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();


                                if (summaryModelList.size() > 0) {
                                    binding.tvNoSummary.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoSummary.setVisibility(View.VISIBLE);

                                }
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
                                Toast.makeText(SummaryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SummaryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SummaryDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();
                            binding.swipeRefresh.setRefreshing(false);


                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SummaryActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SummaryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


}