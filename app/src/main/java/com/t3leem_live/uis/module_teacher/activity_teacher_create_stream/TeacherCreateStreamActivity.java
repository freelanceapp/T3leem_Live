package com.t3leem_live.uis.module_teacher.activity_teacher_create_stream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivityTeacherCreateStreamBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.CreateRoomModel;
import com.t3leem_live.models.StreamDataModel;
import com.t3leem_live.models.TeacherGroupModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherCreateStreamActivity extends AppCompatActivity {

    private ActivityTeacherCreateStreamBinding binding;
    private CreateRoomModel model;
    private String lang = "ar";
    private Preferences preferences;
    private UserModel userModel;
    private TeacherGroupModel teacherGroupModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_create_stream);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        teacherGroupModel = (TeacherGroupModel) intent.getSerializableExtra("data");


    }

    private void initView()
    {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        model = new CreateRoomModel();
        binding.setLang(lang);
        binding.setModel(model);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);


        binding.llBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnCreateRoom.setOnClickListener(view -> {
            if (model.isDataValid(this)){
                createStream();
            }
        });


    }
    private void createStream()
    {
        String department_id ="";
        if (teacherGroupModel.getDepartment_fk()!=null){
            department_id = String.valueOf(teacherGroupModel.getId());
        }
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .teacherCreateStream(userModel.getData().getId(),teacherGroupModel.getStage_fk().getId(),teacherGroupModel.getClass_fk().getId(),department_id,Integer.parseInt(teacherGroupModel.getSubject_id()),teacherGroupModel.getId(),model.getTitle(),model.getSubject(),model.getPrice(),model.getDuration(),"android")
                .enqueue(new Callback<StreamDataModel>() {
                    @Override
                    public void onResponse(Call<StreamDataModel> call, Response<StreamDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body()!=null&&response.body().data!=null){
                                String url = response.body().data.getStart_url();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                finish();

                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherCreateStreamActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherCreateStreamActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StreamDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherCreateStreamActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherCreateStreamActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


}