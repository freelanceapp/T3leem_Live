package com.t3leem_live.uis.module_general.activity_library_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_view.ViewActivity;
import com.t3leem_live.adapters.LibraryBooksAdapter;
import com.t3leem_live.databinding.ActivityLibraryDetailsBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
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

public class LibraryDetailsActivity extends AppCompatActivity {

    private ActivityLibraryDetailsBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private StageClassModel stageClassModel;
    private List<StageClassModel> stageClassModelList;
    private LibraryBooksAdapter adapter;
    private String lang;
    private SkeletonScreen skeletonScreen;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_library_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        stageClassModelList = new ArrayList<>();
        preferences  = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        if (lang.equals("ar")){
            binding.setName(stageClassModel.getTitle());
        }else {
            binding.setName(stageClassModel.getTitle_en());

        }
        adapter = new LibraryBooksAdapter(stageClassModelList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();


        getBooks();
    }

    private void getBooks() {
        Api.getService(Tags.base_url)
                .getBooks(stageClassModel.getId())
                .enqueue(new Callback<StageDataModel>() {
                    @Override
                    public void onResponse(Call<StageDataModel> call, Response<StageDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {

                            if (response.body()!=null&&response.body().getData()!=null){
                                stageClassModelList.clear();
                                stageClassModelList.addAll(response.body().getData());
                                if (stageClassModelList.size()>0){
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoBooks.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoBooks.setVisibility(View.VISIBLE);

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
                                Toast.makeText(LibraryDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LibraryDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StageDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(LibraryDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LibraryDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    public void navigateToActivityView(String url) {
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}