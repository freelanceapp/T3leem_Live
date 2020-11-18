package com.t3leem_live.activities_fragments.activity_teacher_groups_chat;

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
import com.t3leem_live.activities_fragments.activity_teacher_add_group.TeacherAddGroupActivity;
import com.t3leem_live.activities_fragments.activity_teacher_create_chat_group.TeacherCreateGroupChatActivity;
import com.t3leem_live.activities_fragments.activity_teacher_create_stream.TeacherCreateStreamActivity;
import com.t3leem_live.activities_fragments.activity_teacher_group.TeacherGroupActivity;
import com.t3leem_live.adapters.TeacherGroupsAdapter;
import com.t3leem_live.adapters.TeacherGroupsChatAdapter;
import com.t3leem_live.databinding.ActivityTeacherGroupBinding;
import com.t3leem_live.databinding.ActivityTeacherGroupChatBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.TeacherGroupModel;
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

public class TeacherGroupChatActivity extends AppCompatActivity {
    private ActivityTeacherGroupChatBinding binding;
    private String lang = "ar";
    private List<TeacherGroupModel> teacherGroupModelList;
    private TeacherGroupsChatAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_group_chat);
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
        adapter = new TeacherGroupsChatAdapter(teacherGroupModelList, this);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        getGroups();

    }

    private void getGroups()
    {


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
                                    Toast.makeText(TeacherGroupChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherGroupChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }
    public void createGroupChat(TeacherGroupModel model) {
        Intent intent = new Intent(this, TeacherCreateGroupChatActivity.class);
        intent.putExtra("data",model);
        startActivity(intent);
    }
}