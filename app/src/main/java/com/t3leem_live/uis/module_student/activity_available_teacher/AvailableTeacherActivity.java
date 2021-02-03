package com.t3leem_live.uis.module_student.activity_available_teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.StudentTeachersAdapter2;
import com.t3leem_live.databinding.ActivityAvailableTeacherBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.TeachersDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
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

public class AvailableTeacherActivity extends AppCompatActivity {
    private ActivityAvailableTeacherBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private List<TeacherModel> teacherModelList;
    private StudentTeachersAdapter2 adapter;
    private SkeletonScreen skeletonScreen;
    private Call<TeachersDataModel> call, searchCall;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_available_teacher);
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
        adapter = new StudentTeachersAdapter2(teacherModelList, this);
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

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    Common.CloseKeyBoard(AvailableTeacherActivity.this, binding.edtSearch);
                    getAvailableTeacher();
                } else {
                    search(s.toString());
                }
            }
        });

        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.edtSearch.getText().toString();
                if (!query.isEmpty()) {
                    Common.CloseKeyBoard(AvailableTeacherActivity.this, binding.edtSearch);
                    search(query);
                }
            }
            return false;
        });

        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(this::getAvailableTeacher);

        getAvailableTeacher();

    }

    private void getAvailableTeacher() {

        if (searchCall != null) {
            searchCall.cancel();
        }
        call = Api.getService(Tags.base_url)
                .getAvailableTeacher(userModel.getData().getId());
        call.enqueue(new Callback<TeachersDataModel>() {
            @Override
            public void onResponse(Call<TeachersDataModel> call, Response<TeachersDataModel> response) {
                skeletonScreen.hide();
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getData() != null) {
                        teacherModelList.clear();
                        teacherModelList.addAll(response.body().getData());
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
                        Toast.makeText(AvailableTeacherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AvailableTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeachersDataModel> call, Throwable t) {
                try {
                    skeletonScreen.hide();
                    binding.swipeRefresh.setRefreshing(false);


                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage() + "__");

                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(AvailableTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            Toast.makeText(AvailableTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage() + "__");
                }
            }
        });
    }


    private void search(String query) {

        if (call != null) {
            call.cancel();
        }

        searchCall = Api.getService(Tags.base_url)
                .getSearchTeacher(userModel.getData().getId(), query);
        searchCall.enqueue(new Callback<TeachersDataModel>() {
            @Override
            public void onResponse(Call<TeachersDataModel> call, Response<TeachersDataModel> response) {
                skeletonScreen.hide();
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getData() != null) {
                        teacherModelList.clear();
                        teacherModelList.addAll(response.body().getData());
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
                        Toast.makeText(AvailableTeacherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AvailableTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeachersDataModel> call, Throwable t) {
                try {
                    skeletonScreen.hide();
                    binding.swipeRefresh.setRefreshing(false);


                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage() + "__");

                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(AvailableTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            Toast.makeText(AvailableTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

    public void follow_un_follow(TeacherModel model, boolean type, int pos) {
        Api.getService(Tags.base_url)
                .studentFollow_UnFollow(userModel.getData().getId(), model.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {

                        } else {

                            if (type) {
                                model.setFollow_fk(null);

                            } else {
                                model.setFollow_fk(new TeacherModel.FollowFk());
                            }
                            teacherModelList.set(pos, model);
                            adapter.notifyItemChanged(pos);
                            try {
                                Log.e("error_code", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(AvailableTeacherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 409) {
                                Toast.makeText(AvailableTeacherActivity.this, R.string.already_booked, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AvailableTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            if (type) {
                                model.setFollow_fk(null);

                            } else {
                                model.setFollow_fk(new TeacherModel.FollowFk());
                            }
                            teacherModelList.set(pos, model);
                            adapter.notifyItemChanged(pos);

                            if (t.getMessage() != null) {

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(AvailableTeacherActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AvailableTeacherActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void myReservation(TeacherModel model) {
        Intent intent = new Intent(this, StudentTeachersGroupActivity.class);


        StageClassModel stageClassModel = new StageClassModel();
        stageClassModel.setId(Integer.parseInt(userModel.getData().getStage_id()));
        intent.putExtra("data", model);
        intent.putExtra("data2",stageClassModel);
        startActivity(intent);
    }
}