package com.t3leem_live.uis.module_center_course.activity_home_center.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.t3leem_live.adapters.CenterGroupsAdapter;
import com.t3leem_live.databinding.DialogAlertBinding;
import com.t3leem_live.databinding.DialogCreateCenterGroupBinding;
import com.t3leem_live.databinding.FragmentHomeCenterCourseBinding;
import com.t3leem_live.models.CenterGroupDataModel;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.CenterGroupDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_center_course.activity_center_group_details.CenterGroupDetailsActivity;
import com.t3leem_live.uis.module_center_course.activity_home_center.CenterHomeActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_create_chat_group.TeacherCreateGroupChatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home_Center extends Fragment {
    private FragmentHomeCenterCourseBinding binding;
    private CenterHomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String lang;
    private CenterGroupsAdapter centerGroupsAdapter;
    private List<CenterGroupModel> centerGroupModelList;

    private SkeletonScreen skeletonScreen;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (CenterHomeActivity) context;
    }

    public static Fragment_Home_Center newInstance() {
        return new Fragment_Home_Center();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_center_course, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        centerGroupModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        centerGroupsAdapter = new CenterGroupsAdapter(centerGroupModelList, activity,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));

        binding.recView.setAdapter(centerGroupsAdapter);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(centerGroupsAdapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true).show();

        getCenterGroups();
    }

    private void getCenterGroups()
    {

        skeletonScreen.show();
        Api.getService(Tags.base_url)
                .getCenterGroups("Bearer "+userModel.getData().getToken(),userModel.getData().getId(),"off")
                .enqueue(new Callback<CenterGroupDataModel>() {
                    @Override
                    public void onResponse(Call<CenterGroupDataModel> call, Response<CenterGroupDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            centerGroupModelList.clear();
                            centerGroupModelList.addAll(response.body().getData());
                            centerGroupsAdapter.notifyDataSetChanged();
                        } else {
                            skeletonScreen.hide();

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
                    public void onFailure(Call<CenterGroupDataModel> call, Throwable t) {
                        try {
                            skeletonScreen.hide();

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
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialogAlertGroup(activity);
            }
        });

    }
    public  void CreateDialogAlertGroup(Context context) {
       AlertDialog  dialog = new AlertDialog.Builder(context)
                .create();

        DialogCreateCenterGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_create_center_group, null, false);

     binding.btnAccept.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String name=binding.edtname.getText().toString();
             if(!name.isEmpty()){
                 dialog.dismiss();

                 createGroup(name);
             }
         }
     });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void createGroup(String title)
    {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .centerCreateGroups("Bearer "+userModel.getData().getToken(),userModel.getData().getId()+"",title)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            getCenterGroups();
                            Toast.makeText(activity, R.string.suc, Toast.LENGTH_SHORT).show();

                        } else {
                            dialog.dismiss();
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

    public void showDetials(int layoutPosition) {
        Intent intent=new Intent(activity, CenterGroupDetailsActivity.class);
        intent.putExtra("data",centerGroupModelList.get(layoutPosition));
        startActivity(intent);
    }
}
