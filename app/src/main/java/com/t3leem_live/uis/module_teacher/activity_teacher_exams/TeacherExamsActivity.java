package com.t3leem_live.uis.module_teacher.activity_teacher_exams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_view.ViewActivity;
import com.t3leem_live.adapters.TeacherExamsAdapter;
import com.t3leem_live.databinding.ActivityTeacherExamsBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.TeacherExamDataModel;
import com.t3leem_live.models.TeacherExamModel;
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

public class TeacherExamsActivity extends AppCompatActivity {
    private ActivityTeacherExamsBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private List<TeacherExamModel> teacherExamModelList;
    private TeacherExamsAdapter adapter;
    private String lang;
    private SkeletonScreen skeletonScreen;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_teacher_exams);
        initView();

    }



    private void initView() {
        teacherExamModelList = new ArrayList<>();
        preferences  = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");

        adapter = new TeacherExamsAdapter(teacherExamModelList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        binding.fab.setOnClickListener(view -> {
            String url = Tags.base_url+"create-teacher-exams?"+"teacher_id="+userModel.getData().getId()+"&view_type=webView";
            Intent intent = new Intent(this, ViewActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        });
        getExams();


    }

    private void getExams()
    {

        Api.getService(Tags.base_url)
                .getTeacherExams("Bearer "+userModel.getData().getToken(),userModel.getData().getId())
                .enqueue(new Callback<TeacherExamDataModel>() {
                    @Override
                    public void onResponse(Call<TeacherExamDataModel> call, Response<TeacherExamDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {

                            if (response.body()!=null&&response.body().getData()!=null){
                                teacherExamModelList.clear();
                                teacherExamModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                if (teacherExamModelList.size()>0){
                                    binding.tvNoExams.setVisibility(View.GONE);
                                }else {
                                    binding.tvNoExams.setVisibility(View.VISIBLE);

                                }
                            }

                        } else {
                            skeletonScreen.hide();

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherExamsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherExamsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherExamDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherExamsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherExamsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void setItemData(TeacherExamModel model) {
        String url = Tags.base_url+"student-answer-exam/"+model.getId()+"?teacher_id="+userModel.getData().getId()+"&view_type=webView";
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);

    }
}