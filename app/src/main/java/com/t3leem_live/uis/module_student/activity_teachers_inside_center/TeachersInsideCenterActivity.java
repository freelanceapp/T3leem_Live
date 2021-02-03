package com.t3leem_live.uis.module_student.activity_teachers_inside_center;

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
import com.t3leem_live.adapters.TeacherInsideCenterAdapter;
import com.t3leem_live.databinding.ActivityTeachersInsideCenterBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.StudentCenterModel;
import com.t3leem_live.models.StudentSelectedGroup;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.TeachersInsideCenterModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_other_students.OtherStudentsActivity;
import com.t3leem_live.uis.module_student.activity_student_center.StudentCenterActivity;
import com.t3leem_live.uis.module_student.activity_student_center_groups.StudentCenterGroupsActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_video.TeacherVideoActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeachersInsideCenterActivity extends AppCompatActivity {

    private ActivityTeachersInsideCenterBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private TeacherInsideCenterAdapter adapter;
    private List<TeachersInsideCenterModel> teachersInsideCenterModelList;
    private SkeletonScreen skeletonScreen;
    private CenterGroupModel model;
    private List<StudentSelectedGroup> selectedGroupList;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teachers_inside_center);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (CenterGroupModel) intent.getSerializableExtra("data");
    }


    private void initView() {
        selectedGroupList = new ArrayList<>();
        teachersInsideCenterModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.setTitle(model.getTitle());
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeacherInsideCenterAdapter(teachersInsideCenterModelList, this);
        binding.recView.setAdapter(adapter);

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

        binding.btnBook.setOnClickListener(view -> {
            book();
        });

        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(this::getTeacher);
        getTeacher();

    }

    private void getTeacher() {
        selectedGroupList.clear();
        binding.btnBook.setVisibility(View.GONE);

        Api.getService(Tags.base_url).
                getTeachersInsideCenterGroup(model.getId(), userModel.getData().getId())
                .enqueue(new Callback<List<TeachersInsideCenterModel>>() {
                    @Override
                    public void onResponse(Call<List<TeachersInsideCenterModel>> call, Response<List<TeachersInsideCenterModel>> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                teachersInsideCenterModelList.clear();

                                if (response.body().size() > 0) {
                                    binding.tvNoTeacher.setVisibility(View.GONE);
                                    teachersInsideCenterModelList.addAll(response.body());
                                } else {
                                    binding.tvNoTeacher.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<List<TeachersInsideCenterModel>> call, Throwable t) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);

                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setItemData(StudentCenterModel model, String group_share) {
        Intent intent;
        if (group_share.equals("group")) {
            intent = new Intent(this, StudentCenterGroupsActivity.class);
            intent.putExtra("data", model);
        } else {
            intent = new Intent(this, OtherStudentsActivity.class);
            intent.putExtra("data", model.getId());
        }
        startActivity(intent);
    }

    public void setSelectedGroup(StudentSelectedGroup selectedGroup) {

        int pos = isGroupSelected(selectedGroup);
        if (pos == -1) {
            if (selectedGroup.getGroup_id() != 0) {
                selectedGroupList.add(selectedGroup);

            }
        } else {

            if (selectedGroup.getGroup_id() == 0) {

                selectedGroupList.remove(pos);
            }
        }

        if (selectedGroupList.size() == teachersInsideCenterModelList.size()) {
            binding.btnBook.setVisibility(View.VISIBLE);
        } else {
            binding.btnBook.setVisibility(View.GONE);
        }
    }

    private int isGroupSelected(StudentSelectedGroup selectedGroup) {
        int pos = -1;
        for (int index = 0; index < selectedGroupList.size(); index++) {
            StudentSelectedGroup group = selectedGroupList.get(index);
            if (group.getId() == selectedGroup.getId()) {
                pos = index;

                return pos;
            }
        }

        return pos;
    }


    private void book() {
        List<Integer> group_ids = new ArrayList<>();
        for (StudentSelectedGroup group : selectedGroupList) {
            group_ids.add(group.getGroup_id());
        }
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .studentBookGroup(userModel.getData().getId(), model.getId(), group_ids)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            try {
                                Log.e("error_code", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(TeachersInsideCenterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 409) {
                                Toast.makeText(TeachersInsideCenterActivity.this, R.string.already_booked, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void setTeacherItem(TeacherModel model) {
        Intent intent = new Intent(this, TeacherVideoActivity.class);
        intent.putExtra("data", model);
        startActivity(intent);
    }


    public void share(TeacherModel model) {
        Intent intent = new Intent(this, OtherStudentsActivity.class);
        intent.putExtra("data", model.getId());
        intent.putExtra("data2", "teacher");
        startActivity(intent);
    }

    public void follow_un_follow(TeacherModel model,boolean type,int pos){
        Api.getService(Tags.base_url)
                .studentFollow_UnFollow(userModel.getData().getId(), model.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {

                        } else {

                            TeachersInsideCenterModel teachersInsideCenterModel = teachersInsideCenterModelList.get(pos);
                            if (type){
                                model.setFollow_fk(null);

                            }else{
                                model.setFollow_fk(new TeacherModel.FollowFk());
                            }
                            teachersInsideCenterModel.setTeacher_fk(model);

                            adapter.notifyItemChanged(pos);
                            try {
                                Log.e("error_code", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(TeachersInsideCenterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 409) {
                                Toast.makeText(TeachersInsideCenterActivity.this, R.string.already_booked, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            TeachersInsideCenterModel teachersInsideCenterModel = teachersInsideCenterModelList.get(pos);
                            if (type){
                                model.setFollow_fk(null);

                            }else{
                                model.setFollow_fk(new TeacherModel.FollowFk());
                            }
                            teachersInsideCenterModel.setTeacher_fk(model);

                            adapter.notifyItemChanged(pos);

                            if (t.getMessage() != null) {

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeachersInsideCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

}