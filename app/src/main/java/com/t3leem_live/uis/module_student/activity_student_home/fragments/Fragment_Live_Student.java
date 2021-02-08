package com.t3leem_live.uis.module_student.activity_student_home.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.databinding.DialogPriceAlertBinding;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.VideoLessonsModel;
import com.t3leem_live.share.Common;
import com.t3leem_live.uis.module_student.activity_student_home.StudentHomeActivity;
import com.t3leem_live.adapters.StreamAdapter;
import com.t3leem_live.databinding.FragmentLiveStudentBinding;
import com.t3leem_live.models.StreamModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_videos.VideosActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Live_Student extends Fragment {
    private FragmentLiveStudentBinding binding;
    private StreamAdapter adapter;
    private List<StreamModel> streamModelList;
    private StudentHomeActivity activity;
    private SkeletonScreen skeletonScreen;
    private UserModel userModel;
    private Preferences preferences;
    private String lang;
    private StreamModel selectedStreamModel;
    private int selectedPos = -1;


    public static Fragment_Live_Student newInstance() {
        return new Fragment_Live_Student();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_student, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        activity = (StudentHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");

        streamModelList = new ArrayList<>();
        adapter = new StreamAdapter(streamModelList, activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        if (userModel != null) {
            getStream();

        }
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getStream);
    }

    private void getStream() {

        Api.getService(Tags.base_url)
                .getStreams(userModel.getData().getId())
                .enqueue(new Callback<List<StreamModel>>() {
                    @Override
                    public void onResponse(Call<List<StreamModel>> call, Response<List<StreamModel>> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            streamModelList.clear();
                            streamModelList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            if (streamModelList.size() > 0) {
                                binding.tvNoStream.setVisibility(View.GONE);
                            } else {
                                binding.tvNoStream.setVisibility(View.VISIBLE);

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
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StreamModel>> call, Throwable t) {
                        try {
                            skeletonScreen.hide();
                            binding.swipeRefresh.setRefreshing(false);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }


    private void createDialogAlert(StreamModel streamModel) {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogPriceAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_price_alert, null, false);
        String pr = getString(R.string.watch_video_for) + " " + streamModel.getTeacher_live_price() + " " + getString(R.string.egp);

        binding.tvMsg.setText(pr);
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        binding.btnAccept.setOnClickListener(v ->
                {
                    if (userModel.getData().getStudent_money() >= streamModel.getTeacher_live_price()) {
                        discount(streamModel);
                    } else {
                        Toast.makeText(activity, R.string.balance_not_ava, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void discount(StreamModel streamModel) {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .payment(userModel.getData().getId(), "live", streamModel.getId(), streamModel.getTeacher_live_price())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            double balance = userModel.getData().getStudent_money() - streamModel.getTeacher_live_price();
                            userModel.getData().setStudent_money(balance);
                            preferences.create_update_userdata(activity, userModel);
                            selectedStreamModel.setUser_stream_fk(new TeacherModel());
                            streamModelList.set(selectedPos, selectedStreamModel);
                            adapter.notifyItemChanged(selectedPos);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(streamModel.getJoin_url()));
                            startActivity(intent);

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                Toast.makeText(activity, R.string.already_paid, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void setItemData(StreamModel streamModel, int adapterPosition) {
        this.selectedPos = adapterPosition;
        this.selectedStreamModel = streamModel;
        if (streamModel.getUser_stream_fk() == null) {
            createDialogAlert(streamModel);

        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(streamModel.getJoin_url()));
            startActivity(intent);
        }
    }
}
