package com.t3leem_live.uis.module_student.activity_my_reservations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.JoinGroupAdapter;
import com.t3leem_live.databinding.ActivityMyReservationBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.JoinCenterDataModel;
import com.t3leem_live.models.JoinCenterModel;
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

public class MyReservationActivity extends AppCompatActivity {
    private ActivityMyReservationBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private List<JoinCenterModel> groupModelList;
    private JoinGroupAdapter adapter;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_reservation);
        binding.executePendingBindings();
        initView();
    }


    private void initView() {
        groupModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JoinGroupAdapter(groupModelList, this);
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
        binding.swipeRefresh.setOnRefreshListener(this::getGroup);

        getGroup();
    }

    private void getGroup() {

        Api.getService(Tags.base_url)
                .getMyReservation(userModel.getData().getId(), "normal")
                .enqueue(new Callback<JoinCenterDataModel>() {
                    @Override
                    public void onResponse(Call<JoinCenterDataModel> call, Response<JoinCenterDataModel> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                groupModelList.clear();
                                groupModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();


                                if (groupModelList.size() > 0) {
                                    binding.tvNoGroups.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoGroups.setVisibility(View.VISIBLE);

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
                                Toast.makeText(MyReservationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyReservationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JoinCenterDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();
                            binding.swipeRefresh.setRefreshing(false);


                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(MyReservationActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MyReservationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
}