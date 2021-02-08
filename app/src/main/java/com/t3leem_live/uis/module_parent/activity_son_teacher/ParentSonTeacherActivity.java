package com.t3leem_live.uis.module_parent.activity_son_teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.ParentTeachersAdapter;
import com.t3leem_live.adapters.StudentTeachersAdapter2;
import com.t3leem_live.databinding.ActivityAvailableTeacherBinding;
import com.t3leem_live.databinding.ActivityParentSonTeacherBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.RoomModel;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.TeachersDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_general.activity_chat.ChatActivity;
import com.t3leem_live.uis.module_student.activity_available_teacher.AvailableTeacherActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_create_chat_group.TeacherCreateGroupChatActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_video.TeacherVideoActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentSonTeacherActivity extends AppCompatActivity {

    private ActivityParentSonTeacherBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private List<TeacherModel> teacherModelList;
    private ParentTeachersAdapter adapter;
    private SkeletonScreen skeletonScreen;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parent_son_teacher);
        binding.executePendingBindings();
        initView();
    }


    private void initView() {
        teacherModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParentTeachersAdapter(teacherModelList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(adapter);
        binding.recView.setNestedScrollingEnabled(true);
        new Handler(Looper.myLooper()).postDelayed(() -> {
            binding.scrollView.scrollTo(0, 0);
        }, 200);
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
        binding.swipeRefresh.setOnRefreshListener(this::getSonTeacher);

        getSonTeacher();

    }

    private void getSonTeacher() {

        Api.getService(Tags.base_url)
                .getParentSonTeacher("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<List<TeacherModel>>() {
                    @Override
                    public void onResponse(Call<List<TeacherModel>> call, Response<List<TeacherModel>> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                teacherModelList.clear();
                                teacherModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();


                                if (teacherModelList.size() > 0) {
                                    binding.tvNoTeacher.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoTeacher.setVisibility(View.VISIBLE);

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
                                Toast.makeText(ParentSonTeacherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ParentSonTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TeacherModel>> call, Throwable t) {
                        try {
                            skeletonScreen.hide();
                            binding.swipeRefresh.setRefreshing(false);


                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ParentSonTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(ParentSonTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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


    public void createRoom(TeacherModel model) {
        List<Integer> selectedStudentList = new ArrayList<>();
        selectedStudentList.add(model.getId());
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .teacherCreateStudentChat("Bearer " + userModel.getData().getToken(), "", "", "Parent_To_Teacher", userModel.getData().getId(), selectedStudentList)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                            Toast.makeText(ParentSonTeacherActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(ParentSonTeacherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 404) {
                                Common.CreateDialogAlert(ParentSonTeacherActivity.this, getString(R.string.no_student_to_create_group));
                            } else {
                                Toast.makeText(ParentSonTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ParentSonTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ParentSonTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void startChat(TeacherModel model) {
        RoomModel.RoomFkModel roomFkModel = new RoomModel.RoomFkModel(model.getRoom_user_fk().getRoom_info_id(), model.getRoom_user_fk().getTo_user_fk().getName(), "", model.getRoom_user_fk().getRoom_status(), model.getRoom_user_fk().getRoom_fk().getRoom_type());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("data", roomFkModel);
        startActivity(intent);
    }
}