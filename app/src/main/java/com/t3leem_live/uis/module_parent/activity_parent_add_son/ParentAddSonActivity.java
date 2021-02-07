package com.t3leem_live.uis.module_parent.activity_parent_add_son;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.MySonsAdapter;
import com.t3leem_live.adapters.SonSearchAdapter;
import com.t3leem_live.databinding.ActivityParentAddSonBinding;
import com.t3leem_live.databinding.DialogAlertBinding;
import com.t3leem_live.databinding.FragmentHomeParentBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.MySonsDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_parent.activity_home_parent.ParentHomeActivity;
import com.t3leem_live.uis.module_parent.activity_home_parent.fragments.Fragment_Home_Parent;
import com.t3leem_live.uis.module_teacher.activity_teacher_create_chat_group.TeacherCreateGroupChatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentAddSonActivity extends AppCompatActivity {
    private ActivityParentAddSonBinding binding;
    private UserModel userModel;
    private SonSearchAdapter adapter;
    private List<UserModel.User> sonsList;
    private Preferences preferences;
    private String lang;
    private boolean refresh = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parent_add_son);
        initView();


    }

    private void initView() {
        sonsList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SonSearchAdapter(sonsList, this);
        binding.recView.setAdapter(adapter);

        binding.tvSearch.setOnClickListener(view -> {
            String query = binding.edtSearch.getText().toString();
            if (!query.isEmpty()) {
                search(query);
            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.isEmpty()) {
                    binding.tvNoSearchResult.setVisibility(View.VISIBLE);
                    sonsList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void search(String query) {
        binding.tvNoSearchResult.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        sonsList.clear();
        adapter.notifyDataSetChanged();

        Api.getService(Tags.base_url).search("Bearer " + userModel.getData().getToken(), query, userModel.getData().getId())
                .enqueue(new Callback<MySonsDataModel>() {
                    @Override
                    public void onResponse(Call<MySonsDataModel> call, Response<MySonsDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getData() != null) {
                                sonsList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoSearchResult.setVisibility(View.GONE);
                                    sonsList.addAll(response.body().getData());
                                } else {
                                    binding.tvNoSearchResult.setVisibility(View.VISIBLE);

                                }

                                adapter.notifyDataSetChanged();


                            }
                        } else {
                            binding.progBar.setVisibility(View.GONE);
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<MySonsDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ParentAddSonActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(ParentAddSonActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void addSon(UserModel.User user) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addSon("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), user.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            refresh = true;
                            CreateDialogAlert();
                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(ParentAddSonActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 408) {
                                Toast.makeText(ParentAddSonActivity.this, R.string.request_sent, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ParentAddSonActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ParentAddSonActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ParentAddSonActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void CreateDialogAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(R.string.reqest_sent_pls_wait);
        binding.btnCancel.setOnClickListener(v ->
                {
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(ParentAddSonActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (refresh) {
            setResult(RESULT_OK);
            finish();
            Toast.makeText(ParentAddSonActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();

        } else {
            super.onBackPressed();

        }
    }
}