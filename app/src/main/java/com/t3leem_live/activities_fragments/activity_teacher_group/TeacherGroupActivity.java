package com.t3leem_live.activities_fragments.activity_teacher_group;

import androidx.annotation.Nullable;
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
import com.t3leem_live.activities_fragments.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.activities_fragments.activity_teacher_add_group.TeacherAddGroupActivity;
import com.t3leem_live.activities_fragments.activity_teacher_create_stream.TeacherCreateStreamActivity;
import com.t3leem_live.adapters.StudentTeacherGroupsAdapter;
import com.t3leem_live.adapters.TeacherGroupsAdapter;
import com.t3leem_live.databinding.ActivityStudentTeachersGroupBinding;
import com.t3leem_live.databinding.ActivityTeacherGroupBinding;
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

public class TeacherGroupActivity extends AppCompatActivity {
    private ActivityTeacherGroupBinding binding;
    private String lang = "ar";
    private List<TeacherGroupModel> teacherGroupModelList;
    private TeacherGroupsAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_group);
        initView();

    }


    private void initView() {
        preference = Preferences.getInstance();
        userModel = preference.getUserData(this);
        teacherGroupModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeacherGroupsAdapter(teacherGroupModelList, this);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherAddGroupActivity.class);
            startActivityForResult(intent,100);
        });
        getGroups();

    }

    private void getGroups() {

        Api.getService(Tags.base_url).getTeachersGroups(userModel.getData().getId())
                .enqueue(new Callback<List<TeacherGroupModel>>() {
                    @Override
                    public void onResponse(Call<List<TeacherGroupModel>> call, Response<List<TeacherGroupModel>> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                teacherGroupModelList.clear();
                                if (response.body().size() > 0) {
                                    binding.tvNoGroups.setVisibility(View.GONE);
                                    teacherGroupModelList.addAll(response.body());
                                    adapter.notifyDataSetChanged();

                                } else {
                                    binding.tvNoGroups.setVisibility(View.VISIBLE);

                                }



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
                    public void onFailure(Call<List<TeacherGroupModel>> call, Throwable t) {
                        skeletonScreen.hide();
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void deleteGroup(TeacherGroupModel model, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteGroups(model.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            teacherGroupModelList.remove(adapterPosition);
                            adapter.notifyItemRemoved(adapterPosition);
                            if (teacherGroupModelList.size() > 0) {
                                binding.tvNoGroups.setVisibility(View.GONE);
                            } else {
                                binding.tvNoGroups.setVisibility(View.VISIBLE);

                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherGroupActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TeacherGroupActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherGroupActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    public void startLiveStream(TeacherGroupModel model2, int adapterPosition) {
        Intent intent = new Intent(this, TeacherCreateStreamActivity.class);
        intent.putExtra("data",model2);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==100&&resultCode==RESULT_OK){
            getGroups();
        }
    }
}