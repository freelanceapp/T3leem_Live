package com.t3leem_live.uis.module_teacher.activity_teacher_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.uis.module_student.activity_student_profile.StudentProfileActivity;
import com.t3leem_live.adapters.StudentsAdapter;
import com.t3leem_live.databinding.ActivityTeacherStudentsBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.TeacherStudentsDataModel;
import com.t3leem_live.models.TeacherStudentsModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherStudentsActivity extends AppCompatActivity {
    private ActivityTeacherStudentsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StudentsAdapter adapter;
    private List<TeacherStudentsModel> teacherModelList;
    private SkeletonScreen skeletonScreen;
    private int current_page = 1;
    private boolean isLoading = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_students);
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
        adapter = new StudentsAdapter(teacherModelList, this);
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

       /* binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.recView.getLayoutManager();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items_count = binding.recView.getAdapter().getItemCount();
                    if (total_items_count>=20&&last_item_pos == (total_items_count - 4) && !isLoading) {
                        int page = current_page + 1;
                        loadMore(page);
                    }
                }
            }
        });*/
        getStudents();

    }

    private void getStudents() {
        Api.getService(Tags.base_url).getStudents("off", 20, 1, userModel.getData().getId())
                .enqueue(new Callback<List<TeacherStudentsModel>>() {
                    @Override
                    public void onResponse(Call<List<TeacherStudentsModel>> call, Response<List<TeacherStudentsModel>> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                teacherModelList.clear();

                                if (response.body().size() > 0) {
                                    binding.tvNoTeacher.setVisibility(View.GONE);
                                    teacherModelList.addAll(response.body());
                                } else {
                                    binding.tvNoTeacher.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<List<TeacherStudentsModel>> call, Throwable t) {
                        skeletonScreen.hide();
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherStudentsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherStudentsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void loadMore(int page) {
        adapter.notifyItemInserted(teacherModelList.size() - 1);
        isLoading = true;

        Api.getService(Tags.base_url).getStudents("on", 20, page, userModel.getData().getId())
                .enqueue(new Callback<List<TeacherStudentsModel>>() {
                    @Override
                    public void onResponse(Call<List<TeacherStudentsModel>> call, Response<List<TeacherStudentsModel>> response) {
                        isLoading = false;
                        if (teacherModelList.get(teacherModelList.size() - 1) == null) {
                            teacherModelList.remove(teacherModelList.size() - 1);
                            adapter.notifyItemRemoved(teacherModelList.size() - 1);
                        }
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().size() > 0) {
                                int old_pos = teacherModelList.size() - 1;
                                teacherModelList.addAll(response.body());
                                int new_pos = teacherModelList.size();
                                adapter.notifyItemRangeInserted(old_pos, new_pos);

                            }
                        } else {
                            isLoading = false;
                            if (teacherModelList.get(teacherModelList.size() - 1) == null) {
                                teacherModelList.remove(teacherModelList.size() - 1);
                                adapter.notifyItemRemoved(teacherModelList.size() - 1);
                            }
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<List<TeacherStudentsModel>> call, Throwable t) {
                        isLoading = false;
                        if (teacherModelList.get(teacherModelList.size() - 1) == null) {
                            teacherModelList.remove(teacherModelList.size() - 1);
                            adapter.notifyItemRemoved(teacherModelList.size() - 1);
                        }
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherStudentsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherStudentsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });

    }


    public void navigateToStudentProfileActivity(TeacherStudentsModel model) {
        Intent intent = new Intent(this, StudentProfileActivity.class);
        intent.putExtra("data", model.getStudent_fk());
        startActivity(intent);
    }
}