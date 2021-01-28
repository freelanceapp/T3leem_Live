package com.t3leem_live.uis.module_center_course.activity_center_group_details;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.CenterGroupsTeacherAdapter;
import com.t3leem_live.databinding.ActivityGroupDetailsBinding;
import com.t3leem_live.databinding.DialogCreateCenterGroupBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.CenterGroupTeacherDataModel;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.CenterGroupTeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_center_course.activity_teacher_add_group.CenterGroupAddTeacherActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
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
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        centerGroupModel = (CenterGroupModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        centerGroupTeacherModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setModel(centerGroupModel);
        binding.setLang(lang);
        adapter = new CenterGroupsTeacherAdapter(centerGroupTeacherModelList, this);
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
        binding.fledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialogAlertGroup(CenterGroupDetailsActivity.this);
            }
        });
        binding.fldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroup();
            }
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.fladdteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CenterGroupDetailsActivity.this, CenterGroupAddTeacherActivity.class);
                intent.putExtra("data", centerGroupModel);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void getTeachersGroups() {
        binding.tvNoBooks.setVisibility(View.GONE);
        binding.llteacher.setVisibility(View.VISIBLE);
        skeletonScreen.show();
        Api.getService(Tags.base_url)
                .getCenterGroupTeacher(
                        "Bearer " + userModel.getData().getToken(), userModel.getData().getId(), centerGroupModel.getId(), "off")
                .enqueue(new Callback<CenterGroupTeacherDataModel>() {
                    @Override
                    public void onResponse(Call<CenterGroupTeacherDataModel> call, Response<CenterGroupTeacherDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            centerGroupTeacherModelList.clear();
                            centerGroupTeacherModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                            if (centerGroupTeacherModelList.size() == 0) {
                                binding.tvNoBooks.setVisibility(View.VISIBLE);
                                binding.llteacher.setVisibility(View.GONE);
                            }
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


    public void deleteTeacher(int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteTeacher("Bearer " + userModel.getData().getToken(), centerGroupTeacherModelList.get(adapterPosition).getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            centerGroupTeacherModelList.remove(adapterPosition);
                            adapter.notifyItemRemoved(adapterPosition);
                            if (centerGroupTeacherModelList.size() > 0) {
                                binding.tvNoBooks.setVisibility(View.GONE);
                            } else {
                                binding.tvNoBooks.setVisibility(View.VISIBLE);
                                binding.llteacher.setVisibility(View.GONE);

                            }

                        } else {
                            dialog.dismiss();
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
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

    public void EditDialogAlertGroup(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogCreateCenterGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_create_center_group, null, false);
binding.btnAccept.setText(getResources().getString(R.string.edit));
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edtname.getText().toString();
                if (!name.isEmpty()) {
                    dialog.dismiss();

                    editGroup(name);
                }
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void editGroup(String title) {
        ProgressDialog dialog = Common.createProgressDialog(CenterGroupDetailsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .centerEditGroups("Bearer " + userModel.getData().getToken(), centerGroupModel.getId() + "", title)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(CenterGroupDetailsActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
                            centerGroupModel.setTitle(title);
                            binding.setModel(centerGroupModel);
                            // finish();
                        } else {
                            dialog.dismiss();
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
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

    public void deleteGroup() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteGroup("Bearer " + userModel.getData().getToken(), centerGroupModel.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(CenterGroupDetailsActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            dialog.dismiss();
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            getTeachersGroups();
        }
    }
}