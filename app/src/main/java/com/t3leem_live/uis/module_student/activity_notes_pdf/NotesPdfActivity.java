package com.t3leem_live.uis.module_student.activity_notes_pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.NotesPdfAdapter;
import com.t3leem_live.databinding.ActivityNotesPdfBinding;
import com.t3leem_live.databinding.DialogPriceAlertBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.models.VideoLessonsDataModel;
import com.t3leem_live.models.VideoLessonsModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.services.ServiceDownload;
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

public class NotesPdfActivity extends AppCompatActivity {
    private ActivityNotesPdfBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private StageClassModel stageClassModel;
    private List<VideoLessonsModel> videoLessonsModelList;
    private NotesPdfAdapter adapter;
    private String lang;
    private SkeletonScreen skeletonScreen;
    private int selectedPos = -1;
    private VideoLessonsModel selectedVideoLesson;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_REQ = 1;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes_pdf);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        videoLessonsModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        if (lang.equals("ar")) {
            binding.setSubject(stageClassModel.getTitle());
        } else {
            binding.setSubject(stageClassModel.getTitle_en());

        }
        adapter = new NotesPdfAdapter(videoLessonsModelList, this);
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


    private void getPdf() {

        Api.getService(Tags.base_url)
                .getVideos(userModel.getData().getId(), stageClassModel.getStage_id(), stageClassModel.getClass_id(), stageClassModel.getDepartment_id(), String.valueOf(stageClassModel.getId()), "pdf")
                .enqueue(new Callback<VideoLessonsDataModel>() {
                    @Override
                    public void onResponse(Call<VideoLessonsDataModel> call, Response<VideoLessonsDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                videoLessonsModelList.clear();
                                videoLessonsModelList.addAll(response.body().getData());
                                if (videoLessonsModelList.size() > 0) {
                                    binding.tvNoPdf.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                } else {
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


    public void download(VideoLessonsModel model, int adapterPosition) {
        this.selectedPos = adapterPosition;
        this.selectedVideoLesson = model;
        if (model.getVideo_or_pdf__payment_fk() == null) {
            createDialogAlert(model);

        } else {
            checkWritePermission();
        }
    }

    private void startService(VideoLessonsModel model) {
        Intent intent = new Intent(this, ServiceDownload.class);
        intent.putExtra("file_url", Tags.IMAGE_URL + model.getFile_doc());
        intent.putExtra("file_name", model.getTitle());
        startService(intent);

    }

    private void createDialogAlert(VideoLessonsModel model) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogPriceAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_price_alert, null, false);
        String pr = getString(R.string.download_file) + " " + model.getPrice() + " " + getString(R.string.egp);

        binding.tvMsg.setText(pr);
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        binding.btnAccept.setOnClickListener(v ->
                {
                    if (userModel.getData().getStudent_money() >= model.getPrice()) {
                        discount(model);
                    } else {
                        Toast.makeText(this, R.string.balance_not_ava, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void discount(VideoLessonsModel model) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .payment(userModel.getData().getId(), "summary", model.getId(), model.getPrice())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            double balance = userModel.getData().getStudent_money() - model.getPrice();
                            userModel.getData().setStudent_money(balance);
                            preferences.create_update_userdata(NotesPdfActivity.this, userModel);
                            selectedVideoLesson.setVideo_or_pdf__payment_fk(new VideoLessonsModel.StudentPayment());
                            videoLessonsModelList.set(selectedPos, selectedVideoLesson);
                            adapter.notifyItemChanged(selectedPos);
                            checkWritePermission();
                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(NotesPdfActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                Toast.makeText(NotesPdfActivity.this, R.string.already_paid, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(NotesPdfActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

    public void checkWritePermission() {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            startService(selectedVideoLesson);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{write_permission}, write_REQ);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == write_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startService(selectedVideoLesson);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }


}