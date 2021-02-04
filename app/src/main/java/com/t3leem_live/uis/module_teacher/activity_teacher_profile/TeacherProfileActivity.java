package com.t3leem_live.uis.module_teacher.activity_teacher_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivityAboutAppBinding;
import com.t3leem_live.databinding.ActivityTeacherProfileBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.JoinCenterDataModel;
import com.t3leem_live.models.SettingDataModel;
import com.t3leem_live.models.TeacherDataModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_general.activity_about_app.AboutAppActivity;
import com.t3leem_live.uis.module_student.activity_my_reservations.MyReservationActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_video.TeacherVideoActivity;

import java.io.IOException;
import java.io.Serializable;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherProfileActivity extends AppCompatActivity {
    private ActivityTeacherProfileBinding binding;
    private String lang = "ar";
    private int teacher_id;
    private TeacherModel teacherModel;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_profile);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        teacher_id = intent.getIntExtra("data", 0);
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        binding.llBack.setOnClickListener(view -> finish());
        binding.llVideo.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherVideoActivity.class);
            intent.putExtra("data", teacherModel);
            startActivity(intent);
        });

        getTeacherData();
    }

    private void getTeacherData() {
        Api.getService(Tags.base_url)
                .getTeacherProfile(userModel.getData().getId(), teacher_id)
                .enqueue(new Callback<TeacherDataModel>() {
                    @Override
                    public void onResponse(Call<TeacherDataModel> call, Response<TeacherDataModel> response) {

                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                binding.scrollView.setVisibility(View.VISIBLE);
                                teacherModel = response.body().getData();
                                binding.setModel(teacherModel);
                            }

                        } else {

                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherDataModel> call, Throwable t) {
                        try {


                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

}