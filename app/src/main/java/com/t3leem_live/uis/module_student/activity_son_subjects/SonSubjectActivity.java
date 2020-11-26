package com.t3leem_live.uis.module_student.activity_son_subjects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.StageAdapter;
import com.t3leem_live.adapters.SubjectAdapter;
import com.t3leem_live.databinding.ActivitySonSubjectBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_notification.NotificationActivity;
import com.t3leem_live.uis.module_student.activity_student_exam.StudentExamActivity;
import com.t3leem_live.uis.module_student.activity_student_home.StudentHomeActivity;
import com.t3leem_live.uis.module_student.activity_subject_tutorial.SubjectTutorialActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SonSubjectActivity extends AppCompatActivity {
    private ActivitySonSubjectBinding binding;
    private SubjectAdapter adapter;
    private List<StageClassModel> stageClassModelList;
    private SkeletonScreen skeletonScreen;
    private String lang;
    private TeacherModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_son_subject);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        userModel = (TeacherModel) intent.getSerializableExtra("data");
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang","ar");

        stageClassModelList = new ArrayList<>();
        adapter = new SubjectAdapter(stageClassModelList,this);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recView.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        getSubjects();

    }

    private void getSubjects()
    {
        int stage_id =0;
        int class_id =0;
        String department_id=null;
        String data="";
        if (userModel.getStage_fk()!=null){
            stage_id = userModel.getStage_fk().getId();
            if (lang.equals("ar")){
                data += userModel.getStage_fk().getTitle();

            }else {
                data += userModel.getStage_fk().getTitle_en();

            }
        }
        if (userModel.getClass_fk()!=null){
            class_id = userModel.getClass_fk().getId();
            data +=",";
            if (lang.equals("ar")){
                data += userModel.getClass_fk().getTitle();

            }else {
                data += userModel.getClass_fk().getTitle_en();

            }
        }

        if (userModel.getDepartment_fk()!=null){
            department_id = String.valueOf(userModel.getDepartment_fk().getId());

            data +=",";
            if (lang.equals("ar")){
                data += userModel.getDepartment_fk().getTitle();

            }else {
                data += userModel.getDepartment_fk().getTitle_en();

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
                                Toast.makeText(SonSubjectActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SonSubjectActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(SonSubjectActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SonSubjectActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
        intent.putExtra("data",classModel);
        startActivity(intent);
    }
}