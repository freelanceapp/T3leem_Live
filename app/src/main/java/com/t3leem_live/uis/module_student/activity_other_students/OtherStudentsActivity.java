package com.t3leem_live.uis.module_student.activity_other_students;

import androidx.appcompat.app.AppCompatActivity;
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
import com.t3leem_live.adapters.OtherStudentsAdapter;
import com.t3leem_live.databinding.ActivityOtherStudentsBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StudentCenterModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.models.UsersDataModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_student_center.StudentCenterActivity;
import com.t3leem_live.uis.module_student.activity_student_sign_up.StudentSignUpActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherStudentsActivity extends AppCompatActivity {
    private ActivityOtherStudentsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private OtherStudentsAdapter adapter;
    private List<UserModel.User> studentModelList;
    private SkeletonScreen skeletonScreen;
    private List<Integer> selectedUsersId;
    private int share_id;
    private String share_type="";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_students);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        share_id =  intent.getIntExtra("data",0);
        share_type = intent.getStringExtra("data2");
    }


    private void initView() {
        selectedUsersId = new ArrayList<>();
        studentModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OtherStudentsAdapter(studentModelList, this);
        binding.recView.setAdapter(adapter);

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

        getStudent();

        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(this::getStudent);
        binding.imageShare.setOnClickListener(view -> share());

    }

    private void getStudent() {
        selectedUsersId.clear();
        binding.setCount(0);
        Api.getService(Tags.base_url).getStudentInMyStages(userModel.getData().getId())
                .enqueue(new Callback<UsersDataModel>() {
                    @Override
                    public void onResponse(Call<UsersDataModel> call, Response<UsersDataModel> response) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                studentModelList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoCenters.setVisibility(View.GONE);
                                    studentModelList.addAll(response.body().getData());
                                } else {
                                    binding.tvNoCenters.setVisibility(View.VISIBLE);

                                }

                                adapter.notifyDataSetChanged();


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
                    public void onFailure(Call<UsersDataModel> call, Throwable t) {
                        skeletonScreen.hide();
                        binding.swipeRefresh.setRefreshing(false);

                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(OtherStudentsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(OtherStudentsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setItemData(UserModel.User model) {

        if (model.isSelected()) {
            selectedUsersId.add(model.getId());
        } else {
            int pos = getItemPos(model.getId());
            if (pos != -1) {
                selectedUsersId.remove(pos);
            }
        }

        binding.setCount(selectedUsersId.size());
    }


    private int getItemPos(int user_id) {
        int pos = -1;
        for (int index = 0; index < selectedUsersId.size(); index++) {
            if (selectedUsersId.get(index) == user_id) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private void share(){
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .share(userModel.getData().getId(),share_type,share_id,selectedUsersId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            Toast.makeText(OtherStudentsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            finish();
                        }else
                        {
                            try {
                                Log.e("error_code",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code()==500)
                            {
                                Toast.makeText(OtherStudentsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(OtherStudentsActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(OtherStudentsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OtherStudentsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e)
                        {
                            Log.e("Error",e.getMessage()+"__");
                        }
                    }
                });
    }
}