package com.t3leem_live.uis.module_teacher.activity_teacher_create_students_chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.t3leem_live.uis.module_teacher.activity_teacher_create_chat_group.TeacherCreateGroupChatActivity;
import com.t3leem_live.adapters.StudentsCreateChatAdapter;
import com.t3leem_live.databinding.ActivityTeacherCreateStudentsChatBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.TeacherStudentsDataModel;
import com.t3leem_live.models.TeacherStudentsModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherCreateStudentsChatActivity extends AppCompatActivity {
    private ActivityTeacherCreateStudentsChatBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StudentsCreateChatAdapter adapter;
    private List<TeacherStudentsModel> teacherModelList;
    private SkeletonScreen skeletonScreen;
    private int current_page = 1;
    private boolean isLoading = false;
    private List<Integer> selectedStudentModelList;
    private List<TeacherStudentsModel> selectedStudents;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_create_students_chat);
        initView();
    }


    private void initView() {
        selectedStudentModelList = new ArrayList<>();
        teacherModelList = new ArrayList<>();
        selectedStudents = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.setCounter(selectedStudentModelList.size());
        binding.flCounter.setBackgroundResource(0);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentsCreateChatAdapter(teacherModelList,this);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        binding.llBack.setOnClickListener(view -> {finish();});

        /*binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        binding.flCounter.setOnClickListener(view -> {
            if(selectedStudentModelList.size()>0){


                Intent intent = new Intent(this, TeacherCreateGroupChatActivity.class);
                intent.putExtra("list", (Serializable) selectedStudentModelList);
                startActivityForResult(intent,100);

                /*if (selectedStudentModelList.size()==1){
                    createStudentGroup(selectedStudents.get(0).getStudent_fk().getName(),selectedStudents.get(0).getStudent_fk().getName());
                }else {

                }*/

            }
        });
        getStudents();

    }

    private void getStudents()
    {
        Api.getService(Tags.base_url).getStudents("off", 20, 1, userModel.getData().getId())
                .enqueue(new Callback<List<TeacherStudentsModel>>() {
                    @Override
                    public void onResponse(Call<List<TeacherStudentsModel>> call, Response<List<TeacherStudentsModel>> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                teacherModelList.clear();

                                if (response.body().size() > 0) {
                                    binding.tvNoStudents.setVisibility(View.GONE);
                                    teacherModelList.addAll(response.body());
                                } else {
                                    binding.tvNoStudents.setVisibility(View.VISIBLE);

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
                                    Toast.makeText(TeacherCreateStudentsChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherCreateStudentsChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

        Api.getService(Tags.base_url).getStudents("off", 20, page,userModel.getData().getId())
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
                                    Toast.makeText(TeacherCreateStudentsChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherCreateStudentsChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });

    }

    public void addStudentToList(TeacherStudentsModel model)
    {
        selectedStudents.add(model);
        selectedStudentModelList.add(model.getStudent_fk().getId());
        binding.setCounter(selectedStudentModelList.size());
        binding.flCounter.setBackgroundResource(R.drawable.rounded_color1);
        binding.tvCounter.setTextColor(ContextCompat.getColor(this,R.color.white));

    }

    public void removeStudent(TeacherStudentsModel model)
    {
        int pos = getSelectedItemPos(model.getStudent_fk().getId());
        if (pos!=-1){
            selectedStudentModelList.remove(pos);
            selectedStudents.remove(pos);
            binding.setCounter(selectedStudentModelList.size());

            if(selectedStudentModelList.size()==0){
                binding.flCounter.setBackgroundResource(0);
                binding.tvCounter.setTextColor(ContextCompat.getColor(this,R.color.black));
            }
        }
    }
    private int getSelectedItemPos(int id){
        int pos = -1;
        for (int index = 0;index<selectedStudentModelList.size();index++){
            if (selectedStudentModelList.get(index)==id){
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private void createStudentGroup(String title, String description)
    {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .teacherCreateStudentChat("Bearer "+userModel.getData().getToken(),title,description,"Teacher_To_Student",userModel.getData().getId(),selectedStudentModelList)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            finish();
                            Toast.makeText(TeacherCreateStudentsChatActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherCreateStudentsChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if(response.code()==404){
                                Common.CreateDialogAlert(TeacherCreateStudentsChatActivity.this,getString(R.string.no_student_to_create_group));
                            } else {
                                Toast.makeText(TeacherCreateStudentsChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TeacherCreateStudentsChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherCreateStudentsChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            finish();
        }
    }
}