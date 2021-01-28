package com.t3leem_live.uis.module_center_course.activity_teacher_add_group;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.t3leem_live.R;
import com.t3leem_live.adapters.StageSpinnerAdapter;
import com.t3leem_live.adapters.TeacherSpinnerAdapter;
import com.t3leem_live.databinding.ActivityAddTeacherGroupBinding;
import com.t3leem_live.databinding.ActivityTeacherAddGroupBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.AddTeacherModel;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.TeacherAddGroupModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.TeachersDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_center_course.activity_center_sign_up.CenterCourseSignUpActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers.StudentTeachersActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CenterGroupAddTeacherActivity extends AppCompatActivity {
    private ActivityAddTeacherGroupBinding binding;
    private AddTeacherModel model;
    private String lang = "ar";
    private List<StageClassModel> stageModelList;
    private StageSpinnerAdapter adapter;
    private TeacherSpinnerAdapter teacheradapter;
    private List<TeacherModel> teacherModelList;

    private Preferences preferences;
    private UserModel userModel;
    private boolean isDataAdded = false;
    private TeacherModel teacherModel;
    private CenterGroupModel centerGroupModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_teacher_group);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        centerGroupModel = (CenterGroupModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        stageModelList = new ArrayList<>();
        teacherModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        model = new AddTeacherModel();
        binding.setLang(lang);
        model.setStage("");
        binding.setModel(model);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        StageClassModel stageClassModel = new StageClassModel();
        stageClassModel.setId(0);
        stageClassModel.setTitle("إختر المرحلة");
        stageClassModel.setTitle_en("Choose Stage");
        stageModelList.add(stageClassModel);
        teacherModel = new TeacherModel();
        teacherModel.setId(0);
        teacherModel.setName(getResources().getString(R.string.select_teacher));
        teacherModelList.add(teacherModel);


        binding.llBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.editteacherprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    model.setTeacher_price(binding.editteacherprice.getText().toString());
                } catch (Exception e) {
                    model.setTeacher_price("0");

                }
                try {

                    if (model.getDiscount_value().equals("")) {
                        model.setDiscount_value("0");
                    }
                    if (model.getTeacher_price().equals("")) {
                        model.setTeacher_price("0");
                    }
                    if (model.getAdmin_value().equals("")) {
                        model.setAdmin_value("0");
                    }
                    if (Double.parseDouble(model.getTeacher_price()) - Double.parseDouble(model.getDiscount_value()) > 0) {
                        model.setTeacher_price_after_discount((Double.parseDouble(model.getTeacher_price()) - Double.parseDouble(model.getDiscount_value())) + "");
                    } else {
                        model.setTeacher_price_after_discount((Double.parseDouble(model.getTeacher_price()) - Double.parseDouble(model.getDiscount_value())) + "");

                        binding.editafterdiscount.setError(getResources().getString(R.string.must_big));
                    }
                    if (Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value()) > 0) {
                        model.setFinal_price((Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value())) + "");

                    } else {
                        model.setFinal_price((Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value())) + "");

                        binding.editFinal.setError(getResources().getString(R.string.must_big));

                    }
                } catch (Exception e) {

                }
                binding.setModel(model);
            }
        });
        binding.editdiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.editafterdiscount.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.editafterdiscount.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editafterdiscount.setError(null);
                try {
                    model.setDiscount_value(binding.editdiscount.getText().toString());
                } catch (Exception e) {
                    model.setDiscount_value("0");


                }
                try {
                    if (model.getDiscount_value().equals("")) {
                        model.setDiscount_value("0");
                    }
                    if (model.getTeacher_price().equals("")) {
                        model.setTeacher_price("0");
                    }
                    if (model.getAdmin_value().equals("")) {
                        model.setAdmin_value("0");
                    }


                    if (Double.parseDouble(model.getTeacher_price()) - Double.parseDouble(model.getDiscount_value()) > 0) {
                        model.setTeacher_price_after_discount((Double.parseDouble(model.getTeacher_price()) - Double.parseDouble(model.getDiscount_value())) + "");
                    } else {
                        model.setTeacher_price_after_discount((Double.parseDouble(model.getTeacher_price()) - Double.parseDouble(model.getDiscount_value())) + "");

                        binding.editafterdiscount.setError(getResources().getString(R.string.must_big));
                    }

                    if (Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value()) > 0) {
                        model.setFinal_price((Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value())) + "");

                    } else {
                        model.setFinal_price((Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value())) + "");

                        binding.editFinal.setError(getResources().getString(R.string.must_big));

                    }

                } catch (Exception e) {

                }
                binding.setModel(model);
            }
        });
        binding.edtadmin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    model.setAdmin_value(binding.edtadmin.getText().toString());
                } catch (Exception e) {
                    model.setAdmin_value("0");

                }
                try {

                    if (model.getDiscount_value().equals("")) {
                        model.setDiscount_value("0");
                    }
                    if (model.getTeacher_price().equals("")) {
                        model.setTeacher_price("0");
                    }
                    if (model.getAdmin_value().equals("")) {
                        model.setAdmin_value("0");
                    }
                    if (Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value()) > 0) {
                        model.setFinal_price((Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value())) + "");

                    } else {
                        model.setFinal_price((Double.parseDouble(model.getTeacher_price_after_discount()) - Double.parseDouble(model.getAdmin_value())) + "");



                    }
                } catch (Exception e) {

                }
                binding.setModel(model);
            }
        });
        adapter = new StageSpinnerAdapter(stageModelList, this);
        binding.spinnerStage.setAdapter(adapter);
        teacheradapter = new TeacherSpinnerAdapter(teacherModelList, this);
        binding.spinnerteacher.setAdapter(teacheradapter);
        binding.spinnerStage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    model.setStage("");

                } else {
                    StageClassModel stageClassModel1 = stageModelList.get(i);
                    model.setStage(stageClassModel1.getId() + "");
                    getTeachers(stageClassModel1.getId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerteacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    model.setName("");

                } else {
                    TeacherModel teacherModel1 = teacherModelList.get(i);
                    model.setName(teacherModel1.getId() + "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isDataValid(CenterGroupAddTeacherActivity.this)) {
                    addTeacher();
                }
            }
        });


        getStage();
    }

    private void getStage() {

        Api.getService(Tags.base_url)
                .getStage()
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        if (response.isSuccessful()) {
                            stageModelList.addAll(response.body().getData());
                            runOnUiThread(() -> adapter.notifyDataSetChanged());

                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(CenterGroupAddTeacherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StageDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void getTeachers(int stage_id) {

        Api.getService(Tags.base_url).getStudentTeachers("off", 20, 1, stage_id)
                .enqueue(new Callback<TeachersDataModel>() {
                    @Override
                    public void onResponse(Call<TeachersDataModel> call, Response<TeachersDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                teacherModelList.clear();
                                teacherModelList.add(teacherModel);
                                teacherModelList.addAll(response.body().getData());
                                runOnUiThread(() -> teacheradapter.notifyDataSetChanged());


                            }
                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<TeachersDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void addTeacher() {


        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .teacherAddToGroup("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), centerGroupModel.getId(), model.getStage(), model.getName(), model.getTeacher_price(), model.getDiscount_value(), model.getTeacher_price_after_discount(), model.getAdmin_value(), model.getFinal_price())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(CenterGroupAddTeacherActivity.this, R.string.suc, Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(CenterGroupAddTeacherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CenterGroupAddTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
        if (isDataAdded) {
            setResult(RESULT_OK);
            finish();
        } else {
            super.onBackPressed();

        }
    }
}