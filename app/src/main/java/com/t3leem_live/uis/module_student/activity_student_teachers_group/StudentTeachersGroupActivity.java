package com.t3leem_live.uis.module_student.activity_student_teachers_group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.StudentTeacherGroupsAdapter;
import com.t3leem_live.databinding.ActivityStudentTeachersGroupBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.TeacherGroupDataModel;
import com.t3leem_live.models.TeacherGroupModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTeachersGroupActivity extends AppCompatActivity {
    private ActivityStudentTeachersGroupBinding binding;
    private TeacherModel teacherModel;
    private StageClassModel stageClassModel;
    private String lang = "ar";
    private List<TeacherGroupModel> teacherGroupModelList;
    private StudentTeacherGroupsAdapter adapter;
    private Preferences preference;
    private UserModel userModel;
    private SkeletonScreen skeletonScreen;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_teachers_group);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        teacherModel = (TeacherModel) intent.getSerializableExtra("data");
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data2");


    }

    private void initView() {
        preference = Preferences.getInstance();
        userModel = preference.getUserData(this);
        teacherGroupModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setModel(teacherModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentTeacherGroupsAdapter(teacherGroupModelList, this);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        getGroups();

    }

    private void getGroups()
    {
        String department_id = "";
        if (userModel.getData().getDepartment_fk()!=null){
            department_id = String.valueOf(userModel.getData().getDepartment_fk().getId());
        }

        Log.e("data",userModel.getData().getStage_fk().getId()+"__"+userModel.getData().getClass_id()+"___"+department_id+"__"+stageClassModel.getId()+"__"+userModel.getData().getId());

        Api.getService(Tags.base_url).getStudentTeachersGroups(Integer.parseInt(userModel.getData().getStage_id()), Integer.parseInt(userModel.getData().getClass_id()), department_id,stageClassModel.getId(), userModel.getData().getId())
                .enqueue(new Callback<TeacherGroupDataModel>() {
                    @Override
                    public void onResponse(Call<TeacherGroupDataModel> call, Response<TeacherGroupDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                teacherGroupModelList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoGroups.setVisibility(View.GONE);
                                    teacherGroupModelList.addAll(response.body().getData());
                                } else {
                                    binding.tvNoGroups.setVisibility(View.VISIBLE);

                                }

                                adapter.notifyDataSetChanged();


                            }
                        } else {
                            skeletonScreen.hide();
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<TeacherGroupDataModel> call, Throwable t) {
                        skeletonScreen.hide();
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(StudentTeachersGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(StudentTeachersGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void setItemData(TeacherGroupModel model, int adapterPosition)
    {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .studentGroupReservation(userModel.getData().getId(),model.getId(),stageClassModel.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            int pos = getMyGroup();
                            if (pos!=-1){
                                TeacherGroupModel model2 = teacherGroupModelList.get(pos);
                                model2.setStudent_book(null);
                                teacherGroupModelList.set(pos,model2);
                                adapter.notifyItemChanged(pos);


                            }
                            model.setStudent_book(new TeacherGroupModel.StudentBook());
                            teacherGroupModelList.set(adapterPosition,model);
                            adapter.notifyItemChanged(adapterPosition);

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(StudentTeachersGroupActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StudentTeachersGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(StudentTeachersGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(StudentTeachersGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private int getMyGroup(){
        int pos = -1;
        for (int index =0;index<teacherGroupModelList.size();index++){
            TeacherGroupModel model = teacherGroupModelList.get(index);
            if (model.getStudent_book()!=null){
                pos = index;
                return pos;
            }
        }
        return pos;
    }
}