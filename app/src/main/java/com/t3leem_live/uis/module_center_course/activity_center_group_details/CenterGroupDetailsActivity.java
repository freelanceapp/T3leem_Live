package com.t3leem_live.uis.module_center_course.activity_center_group_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.CenterGroupsTeacherAdapter;
import com.t3leem_live.adapters.LibraryBooksAdapter;
import com.t3leem_live.databinding.ActivityGroupDetailsBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.CenterGroupTeacherDataModel;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.CenterGroupTeacherDataModel;
import com.t3leem_live.models.CenterGroupTeacherModel;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_general.activity_view.ViewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CenterGroupDetailsActivity extends AppCompatActivity {

    private ActivityGroupDetailsBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private CenterGroupModel centerGroupModel;
    private List<CenterGroupTeacherModel> centerGroupTeacherModelList;
    private CenterGroupsTeacherAdapter adapter;
    private String lang;
    private SkeletonScreen skeletonScreen;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_group_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        centerGroupModel = (CenterGroupModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        centerGroupTeacherModelList = new ArrayList<>();
        preferences  = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
      binding.setModel(centerGroupModel);
      binding.setLang(lang);
        adapter = new CenterGroupsTeacherAdapter(centerGroupTeacherModelList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();
        getTeachersGroups();

    }

    private void
    getTeachersGroups()
    {

        skeletonScreen.show();
        Api.getService(Tags.base_url)
                .getCenterGroupTeacher(
                        "Bearer "+userModel.getData().getToken(),userModel.getData().getId(),centerGroupModel.getId(),"off")
                .enqueue(new Callback<CenterGroupTeacherDataModel>() {
                    @Override
                    public void onResponse(Call<CenterGroupTeacherDataModel> call, Response<CenterGroupTeacherDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            centerGroupTeacherModelList.clear();
                            centerGroupTeacherModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            skeletonScreen.hide();

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(CenterGroupDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CenterGroupDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CenterGroupTeacherDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(CenterGroupDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CenterGroupDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


    }



}