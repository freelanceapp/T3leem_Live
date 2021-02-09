package com.t3leem_live.uis.module_student.activity_summary;

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
import com.t3leem_live.adapters.SummaryAdapter;
import com.t3leem_live.databinding.ActivitySummaryBinding;
import com.t3leem_live.databinding.DialogPriceAlertBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.SummaryDataModel;
import com.t3leem_live.models.SummaryModel;
import com.t3leem_live.models.UserModel;
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

public class SummaryActivity extends AppCompatActivity {
    private ActivitySummaryBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private StageClassModel stageClassModel;
    private List<SummaryModel> summaryModelList;
    private SummaryAdapter adapter;
    private SkeletonScreen skeletonScreen;
    private int selectedPos = -1;
    private SummaryModel selectedSummaryModel;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_REQ = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_summary);
        binding.executePendingBindings();
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        summaryModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SummaryAdapter(summaryModelList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(adapter);
        binding.recView.setNestedScrollingEnabled(true);

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
        binding.swipeRefresh.setOnRefreshListener(this::getSummary);
        getSummary();

    }

    private void getSummary() {
        Log.e("data", stageClassModel.getStage_id() + "__" + stageClassModel.getClass_id() + "__" + stageClassModel.getDepartment_id() + "__" + stageClassModel.getId() + "__" + userModel.getData().getId());
        Api.getService(Tags.base_url)
                .getSummary(Integer.parseInt(stageClassModel.getStage_id()), Integer.parseInt(stageClassModel.getClass_id()), stageClassModel.getDepartment_id(), stageClassModel.getId(), userModel.getData().getId())
                .enqueue(new Callback<SummaryDataModel>() {
                    @Override
                    public void onResponse(Call<SummaryDataModel> call, Response<SummaryDataModel> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                summaryModelList.clear();
                                summaryModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();


                                if (summaryModelList.size() > 0) {
                                    binding.tvNoSummary.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoSummary.setVisibility(View.VISIBLE);

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
                                Toast.makeText(SummaryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SummaryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SummaryDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();
                            binding.swipeRefresh.setRefreshing(false);


                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SummaryActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SummaryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    public void download(SummaryModel model, int adapterPosition) {
        this.selectedPos = adapterPosition;
        this.selectedSummaryModel = model;
        if (model.getSummary_payment_fk() == null) {
            createDialogAlert(model);

        } else {
            checkWritePermission();
        }
    }

    private void startService(SummaryModel model) {
        Intent intent = new Intent(this, ServiceDownload.class);
        intent.putExtra("file_url", Tags.IMAGE_URL + model.getFile_doc());
        intent.putExtra("file_name", model.getTitle());
        startService(intent);

    }

    private void createDialogAlert(SummaryModel model) {
        final AlertDialog dialog = new AlertDialog.Builder(SummaryActivity.this)
                .create();

        DialogPriceAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(SummaryActivity.this), R.layout.dialog_price_alert, null, false);
        String pr = getString(R.string.watch_video_for) + " " + model.getPrice() + " " + getString(R.string.egp);

        binding.tvMsg.setText(pr);
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        binding.btnAccept.setOnClickListener(v ->
                {
                    if (userModel.getData().getStudent_money() >= model.getPrice()) {
                        discount(model);
                    } else {
                        Toast.makeText(SummaryActivity.this, R.string.balance_not_ava, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void discount(SummaryModel model) {

        ProgressDialog dialog = Common.createProgressDialog(SummaryActivity.this, getString(R.string.wait));
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
                            preferences.create_update_userdata(SummaryActivity.this, userModel);
                            selectedSummaryModel.setSummary_payment_fk(new SummaryModel.SummaryFk());
                            summaryModelList.set(selectedPos, selectedSummaryModel);
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
                                Toast.makeText(SummaryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                Toast.makeText(SummaryActivity.this, R.string.already_paid, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SummaryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(SummaryActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SummaryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
            startService(selectedSummaryModel);

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

                startService(selectedSummaryModel);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }
}