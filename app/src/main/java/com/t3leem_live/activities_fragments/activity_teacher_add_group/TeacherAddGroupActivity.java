package com.t3leem_live.activities_fragments.activity_teacher_add_group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_student_sign_up.StudentSignUpActivity;
import com.t3leem_live.activities_fragments.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.adapters.StageSpinnerAdapter;
import com.t3leem_live.databinding.ActivityTeacherAddGroupBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.TeacherAddGroupModel;
import com.t3leem_live.models.TeacherGroupModel;
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

public class TeacherAddGroupActivity extends AppCompatActivity {
    private ActivityTeacherAddGroupBinding binding;
    private TeacherAddGroupModel model;
    private String lang = "ar";
    private List<StageClassModel> subjectModelList;
    private List<StageClassModel> classModelList;
    private List<StageClassModel> departmentList;
    private StageSpinnerAdapter subjectAdapter,classAdapter,departmentAdapter;
    private Preferences preferences;
    private UserModel userModel;
    private boolean isDataAdded = false;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_add_group);
        initView();

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        model = new TeacherAddGroupModel();
        binding.setLang(lang);
        binding.setModel(model);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        subjectModelList = new ArrayList<>();
        StageClassModel stageClassModel = new StageClassModel();
        stageClassModel.setId(0);
        stageClassModel.setTitle("إختر المادة");
        stageClassModel.setTitle_en("Choose Subject");
        subjectModelList.add(stageClassModel);

        classModelList = new ArrayList<>();
        StageClassModel classModel = new StageClassModel();
        classModel.setId(0);
        classModel.setTitle("إختر الصف");
        classModel.setTitle_en("Choose Class");
        classModelList.add(classModel);

        departmentList = new ArrayList<>();

        StageClassModel departmentModel = new StageClassModel();
        departmentModel.setId(0);
        departmentModel.setTitle("إختر القسم");
        departmentModel.setTitle_en("Choose Department");
        departmentList.add(departmentModel);



        subjectAdapter = new StageSpinnerAdapter(subjectModelList,this);
        binding.spinnerSubject.setAdapter(subjectAdapter);
        binding.spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    binding.spinnerClass.setSelection(0);
                    model.setSubject_id(0);
                    model.setClass_id(0);
                    model.setDepartment_id("");
                    binding.llDepartment.setVisibility(View.GONE);

                }else {
                    StageClassModel stageClassModel1 = subjectModelList.get(i);
                    model.setSubject_id(stageClassModel1.getId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        classAdapter = new StageSpinnerAdapter(classModelList,this);
        binding.spinnerClass.setAdapter(classAdapter);
        binding.spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    model.setClass_id(0);
                    model.setDepartment_id("");
                    binding.llDepartment.setVisibility(View.GONE);
                    binding.spinnerDepartment.setSelection(0);

                }else {
                    StageClassModel stageClassModel1 = classModelList.get(i);
                    model.setClass_id(stageClassModel1.getId());
                    getDepartment(stageClassModel1.getId());
                    getSubjects(stageClassModel1.getId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        departmentAdapter = new StageSpinnerAdapter(departmentList,this);
        binding.spinnerDepartment.setAdapter(departmentAdapter);
        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    model.setDepartment_id("");

                }else {
                    StageClassModel stageClassModel1 = departmentList.get(i);
                    model.setDepartment_id(String.valueOf(stageClassModel1.getId()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        binding.llBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.btnSave.setOnClickListener(view -> {
            if (model.isDataValid(this)){
                addGroup();
            }
        });

        getClasses(userModel.getData().getStage_fk().getId());
    }

    private void getClasses(int stage_id)
    {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getClassByStageId(stage_id)
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            classModelList.clear();
                            StageClassModel classModel = new StageClassModel();
                            classModel.setId(0);
                            classModel.setTitle("إختر الصف");
                            classModel.setTitle_en("Choose Class");
                            classModelList.add(classModel);
                            classModelList.addAll(response.body().getData());
                            runOnUiThread(() -> {
                                classAdapter.notifyDataSetChanged();
                                binding.spinnerClass.setSelection(0);

                            });


                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherAddGroupActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StageDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }
    private void getDepartment(int classId)
    {
        Api.getService(Tags.base_url)
                .getDepartmentByClassId(classId)
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        if (response.isSuccessful()) {
                            departmentList.clear();
                            StageClassModel departmentModel = new StageClassModel();
                            departmentModel.setId(0);
                            departmentModel.setTitle("إختر القسم");
                            departmentModel.setTitle_en("Choose Department");
                            departmentList.add(departmentModel);

                            departmentList.addAll(response.body().getData());

                            if (response.body().getData().size()>0){
                                binding.llDepartment.setVisibility(View.VISIBLE);
                                model.setHasDepartment(true);
                            }else {
                                binding.llDepartment.setVisibility(View.GONE);
                                model.setHasDepartment(false);

                            }
                            binding.setModel(model);
                            runOnUiThread(() -> {
                                departmentAdapter.notifyDataSetChanged();
                                binding.spinnerDepartment.setSelection(0);

                            });


                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherAddGroupActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StageDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }
    private void getSubjects(int classId)
    {
        Log.e("class_id",classId+"_");
        Api.getService(Tags.base_url)
                .getSubjectByClassId(classId,userModel.getData().getStage_fk().getId())
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        if (response.isSuccessful()) {
                            subjectModelList.clear();
                            StageClassModel subjectModel = new StageClassModel();
                            subjectModel.setId(0);
                            subjectModel.setTitle("إختر المادة");
                            subjectModel.setTitle_en("Choose Subject");
                            subjectModelList.add(subjectModel);

                            subjectModelList.addAll(response.body().getData());

                            runOnUiThread(() -> {
                                subjectAdapter.notifyDataSetChanged();
                                binding.spinnerSubject.setSelection(0);

                            });


                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherAddGroupActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StageDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }
    private void addGroup()
    {


        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .teacherAddGroup(userModel.getData().getId(),userModel.getData().getStage_fk().getId(),model.getClass_id(),model.getDepartment_id(),model.getSubject_id(),model.getName(),model.getTime(),model.getMax_number())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            isDataAdded = true;
                            Toast.makeText(TeacherAddGroupActivity.this,R.string.suc,Toast.LENGTH_LONG).show();

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherAddGroupActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherAddGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (isDataAdded){
            setResult(RESULT_OK);
            finish();
        }else {
            super.onBackPressed();

        }
    }
}