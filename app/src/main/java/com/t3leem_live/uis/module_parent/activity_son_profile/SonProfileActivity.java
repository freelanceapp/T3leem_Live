package com.t3leem_live.uis.module_parent.activity_son_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivitySonProfileBinding;
import com.t3leem_live.databinding.ActivityStudentProfileBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StudentRateDataModel;
import com.t3leem_live.models.StudentRateModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import devlight.io.library.ArcProgressStackView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SonProfileActivity extends AppCompatActivity {
    private ActivitySonProfileBinding binding;
    private UserModel.User user;
    private List<StudentRateModel> sonRateModelList;
    private String lang = "ar";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_son_profile);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        user = (UserModel.User) intent.getSerializableExtra("data");
    }

    private void initView() {
        binding.setModel(user);
        sonRateModelList = new ArrayList<>();
        updateStage();
    }


    private void updateUi() {
        if (sonRateModelList.size()>0){
            double total_rate=0.0;
            double total_degree = 0.0;
            int [] colors = {ContextCompat.getColor(this,R.color.color17),ContextCompat.getColor(this,R.color.color18),ContextCompat.getColor(this,R.color.color19),ContextCompat.getColor(this,R.color.color20),ContextCompat.getColor(this,R.color.color21)};
            final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
            for (int index = 0; index< sonRateModelList.size(); index++){
                StudentRateModel studentRateModel = sonRateModelList.get(index);
                float degree = (float) ((Double.parseDouble(studentRateModel.getRate())/Double.parseDouble(studentRateModel.getFrom_total_rate()))*100);
                total_rate += Double.parseDouble(studentRateModel.getRate());
                total_degree +=Double.parseDouble(studentRateModel.getFrom_total_rate());

                String title="";
                if (lang.equals("ar")){
                    title = studentRateModel.getSubject_fk().getTitle();
                }else {
                    title = studentRateModel.getSubject_fk().getTitle_en();

                }
                int color_pos = (index%colors.length);

                models.add(new ArcProgressStackView.Model(title, degree, ContextCompat.getColor(this,R.color.gray2), colors[color_pos]));

            }
            double total_percentage = (total_rate/total_degree)*100;
            binding.arcView.setModels(models);
            binding.arcView.setTextColor(ContextCompat.getColor(this,R.color.white));
            binding.tvTotalRate.setText(String.format(Locale.ENGLISH,"%.1f%s",total_percentage,"%"));

        }else {
            final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
            models.add(new ArcProgressStackView.Model(getString(R.string.no_rates), 0, ContextCompat.getColor(this,R.color.gray2),ContextCompat.getColor(this,R.color.gray2)));
            binding.arcView.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.arcView.setModels(models);
            binding.tvTotalRate.setText("0%");
        }
        binding.arcView.setVisibility(View.VISIBLE);


    }

    private void updateStage() {
        String data="";
        if (user.getStage_fk()!=null){
            if (lang.equals("ar")){
                data += user.getStage_fk().getTitle();

            }else {
                data += user.getStage_fk().getTitle_en();

            }
        }
        if (user.getClass_fk()!=null){
            data +=",";
            if (lang.equals("ar")){
                data += user.getClass_fk().getTitle();

            }else {
                data += user.getClass_fk().getTitle_en();

            }
        }
        if (user.getDepartment_fk()!=null){

            data +=",";
            if (lang.equals("ar")){
                data += user.getDepartment_fk().getTitle();

            }else {
                data += user.getDepartment_fk().getTitle_en();

            }
        }
        binding.setStage(data);
        getRate();
    }

    private void getRate()
    {
        Api.getService(Tags.base_url)
                .getStudentRate(user.getId())
                .enqueue(new Callback<StudentRateDataModel>() {
                    @Override
                    public void onResponse(Call<StudentRateDataModel> call, Response<StudentRateDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body().getData()!=null) {
                            sonRateModelList.clear();
                            sonRateModelList.addAll(response.body().getData());

                            updateUi();

                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(SonProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SonProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentRateDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SonProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SonProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }
}