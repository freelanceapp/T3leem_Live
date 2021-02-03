package com.t3leem_live.uis.module_student.activity_notification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.t3leem_live.adapters.NotificationAdapter;
import com.t3leem_live.databinding.ActivityNotificationBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.NotificationDataModel;
import com.t3leem_live.models.NotificationModel;
import com.t3leem_live.models.TeacherGroupModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_general.activity_view.ViewActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_create_stream.TeacherCreateStreamActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_group.TeacherGroupActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    private String lang = "ar";
    private List<NotificationModel> notificationModelList;
    private NotificationAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();

    }


    private void initView() {
        preference = Preferences.getInstance();
        userModel = preference.getUserData(this);
        notificationModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationModelList, this);
        binding.recView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();
        binding.llBack.setOnClickListener(view -> finish());
        binding.swipeRefresh.setColorSchemeResources(R.color.color1);

        binding.swipeRefresh.setOnRefreshListener(this::getNotification);

        getNotification();


    }

    private void getNotification() {

        Api.getService(Tags.base_url).getNotification(userModel.getData().getId())
                .enqueue(new Callback<NotificationDataModel>() {
                    @Override
                    public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                notificationModelList.clear();
                                if (response.body().getData().size() > 0) {
                                    binding.tvNotification.setVisibility(View.GONE);
                                    notificationModelList.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();

                                } else {
                                    binding.tvNotification.setVisibility(View.VISIBLE);

                                }


                            }
                        } else {
                            binding.swipeRefresh.setRefreshing(false);

                            skeletonScreen.hide();
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);

                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(NotificationActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void acceptRefuseGroup(NotificationModel notificationModel, int adapterPosition, String status) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .acceptRefuseRequest("Bearer " + userModel.getData().getToken(), status, notificationModel.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            notificationModelList.remove(adapterPosition);
                            adapter.notifyItemRemoved(adapterPosition);
                            if (notificationModelList.size() > 0) {
                                binding.tvNotification.setVisibility(View.GONE);
                            } else {
                                binding.tvNotification.setVisibility(View.VISIBLE);

                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(NotificationActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    public void setItemExam(NotificationModel model) {
        String url = Tags.base_url + "student-exams/" + model.getExam_id() + "?student_id=" + userModel.getData().getId() + "&view_type=webView";
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);

    }
}