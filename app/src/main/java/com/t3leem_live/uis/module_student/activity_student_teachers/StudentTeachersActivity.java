package com.t3leem_live.uis.module_student.activity_student_teachers;

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
import com.t3leem_live.uis.module_student.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_video.TeacherVideoActivity;
import com.t3leem_live.adapters.StudentTeachersAdapter;
import com.t3leem_live.databinding.ActivityStudentTeachersBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.TeachersDataModel;
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

public class StudentTeachersActivity extends AppCompatActivity {
    private ActivityStudentTeachersBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StudentTeachersAdapter adapter;
    private List<TeacherModel> teacherModelList;
    private SkeletonScreen skeletonScreen;
    private int current_page = 1;
    private boolean isLoading = false;
    private StageClassModel stageClassModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_teachers);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");
    }
    private void initView() {
        teacherModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentTeachersAdapter(teacherModelList,this);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        binding.llBack.setOnClickListener(view -> {finish();});

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        });
        getTeachers();

    }

    private void getTeachers()
    {

        Api.getService(Tags.base_url).getStudentTeachers("on", 20, 1, userModel.getData().getStage_fk().getId())
                .enqueue(new Callback<TeachersDataModel>() {
                    @Override
                    public void onResponse(Call<TeachersDataModel> call, Response<TeachersDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                teacherModelList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoTeacher.setVisibility(View.GONE);
                                    teacherModelList.addAll(response.body().getData());
                                    current_page = response.body().getCurrent_page();
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
                    public void onFailure(Call<TeachersDataModel> call, Throwable t) {
                        skeletonScreen.hide();
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(StudentTeachersActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(StudentTeachersActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }
    private void loadMore(int page)
    {
        adapter.notifyItemInserted(teacherModelList.size() - 1);
        isLoading = true;

        Api.getService(Tags.base_url).getStudentTeachers("on", 20, page,userModel.getData().getStage_fk().getId())
                .enqueue(new Callback<TeachersDataModel>() {
            @Override
            public void onResponse(Call<TeachersDataModel> call, Response<TeachersDataModel> response) {
                isLoading = false;
                if (teacherModelList.get(teacherModelList.size() - 1) == null) {
                    teacherModelList.remove(teacherModelList.size() - 1);
                    adapter.notifyItemRemoved(teacherModelList.size() - 1);
                }
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData().size() > 0) {
                        current_page = response.body().getCurrent_page();
                        int old_pos = teacherModelList.size() - 1;
                        teacherModelList.addAll(response.body().getData());
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
            public void onFailure(Call<TeachersDataModel> call, Throwable t) {
                isLoading = false;
                if (teacherModelList.get(teacherModelList.size() - 1) == null) {
                    teacherModelList.remove(teacherModelList.size() - 1);
                    adapter.notifyItemRemoved(teacherModelList.size() - 1);
                }
                try {
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage() + "__");

                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(StudentTeachersActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            Toast.makeText(StudentTeachersActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {

                }
            }
        });

    }

    public void setItemData(TeacherModel model, String type) {
        Intent intent;
        if (type.equals("video")){
            intent = new Intent(this, TeacherVideoActivity.class);

        }else {
            intent = new Intent(this, StudentTeachersGroupActivity.class);

        }
        intent.putExtra("data",model);
        intent.putExtra("data2",stageClassModel);
        startActivity(intent);
    }
}