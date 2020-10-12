package com.t3leem_live.activities_fragments.activity_notes_pdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_subject_tutorial.SubjectTutorialActivity;
import com.t3leem_live.activities_fragments.activity_videos.VideosActivity;
import com.t3leem_live.adapters.NotesPdfAdapter;
import com.t3leem_live.adapters.SummaryAdapter;
import com.t3leem_live.databinding.ActivityNotesPdfBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.SummaryDataModel;
import com.t3leem_live.models.SummaryModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.models.VideoLessonsDataModel;
import com.t3leem_live.models.VideoLessonsModel;
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

public class NotesPdfActivity extends AppCompatActivity {
    private ActivityNotesPdfBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private StageClassModel stageClassModel;
    private List<VideoLessonsModel> videoLessonsModelList;
    private NotesPdfAdapter adapter;
    private String lang;
    private SkeletonScreen skeletonScreen;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notes_pdf);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        videoLessonsModelList = new ArrayList<>();
        preferences  = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        if (lang.equals("ar")){
            binding.setSubject(stageClassModel.getTitle());
        }else {
            binding.setSubject(stageClassModel.getTitle_en());

        }
        adapter = new NotesPdfAdapter(videoLessonsModelList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        getPdf();

    }


    private void getPdf()
    {

        Api.getService(Tags.base_url)
                .getVideos(stageClassModel.getStage_id(),stageClassModel.getClass_id(),stageClassModel.getDepartment_id(),String.valueOf(stageClassModel.getId()),"pdf")
                .enqueue(new Callback<VideoLessonsDataModel>() {
                    @Override
                    public void onResponse(Call<VideoLessonsDataModel> call, Response<VideoLessonsDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {

                            if (response.body()!=null&&response.body().getData()!=null){
                                videoLessonsModelList.clear();
                                videoLessonsModelList.addAll(response.body().getData());
                                if (videoLessonsModelList.size()>0){
                                    binding.tvNoPdf.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }else {
                                    binding.tvNoPdf.setVisibility(View.VISIBLE);

                                }
                            }

                        } else {
                            skeletonScreen.hide();

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(NotesPdfActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NotesPdfActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoLessonsDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(NotesPdfActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NotesPdfActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }




}