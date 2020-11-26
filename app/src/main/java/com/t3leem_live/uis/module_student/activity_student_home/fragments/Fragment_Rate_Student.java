package com.t3leem_live.uis.module_student.activity_student_home.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_student.activity_student_home.StudentHomeActivity;
import com.t3leem_live.databinding.FragmentRateStudentBinding;
import com.t3leem_live.models.StudentRateDataModel;
import com.t3leem_live.models.StudentRateModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import devlight.io.library.ArcProgressStackView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Rate_Student extends Fragment {
    private FragmentRateStudentBinding binding;
    private List<StudentRateModel> studentRateModelList;
    private StudentHomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String lang="ar";

    public static Fragment_Rate_Student newInstance(){
        return new Fragment_Rate_Student();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate_student,container,false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRate();
        updateStage();

    }



    private void initView() {
        activity = (StudentHomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        studentRateModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

    }

    private void getRate()
    {
        Api.getService(Tags.base_url)
                .getStudentRate(userModel.getData().getId())
                .enqueue(new Callback<StudentRateDataModel>() {
                    @Override
                    public void onResponse(Call<StudentRateDataModel> call, Response<StudentRateDataModel> response) {
                       binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body().getData()!=null) {
                            studentRateModelList.clear();
                            studentRateModelList.addAll(response.body().getData());

                            updateUi();

                        } else {
                            binding.progBar.setVisibility(View.GONE);

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
                    public void onFailure(Call<StudentRateDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

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

    private void updateUi() {
        if (studentRateModelList.size()>0){
            double total_rate=0.0;
            double total_degree = 0.0;
            int [] colors = {ContextCompat.getColor(activity,R.color.color17),ContextCompat.getColor(activity,R.color.color18),ContextCompat.getColor(activity,R.color.color19),ContextCompat.getColor(activity,R.color.color20),ContextCompat.getColor(activity,R.color.color21)};
            final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
            for (int index=0;index<studentRateModelList.size();index++){
                StudentRateModel studentRateModel = studentRateModelList.get(index);
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

                models.add(new ArcProgressStackView.Model(title, degree, ContextCompat.getColor(activity,R.color.gray2), colors[color_pos]));

            }
            double total_percentage = (total_rate/total_degree)*100;
            binding.arcView.setModels(models);
            binding.arcView.setTextColor(ContextCompat.getColor(activity,R.color.white));
            binding.tvTotalRate.setText(String.format(Locale.ENGLISH,"%.1f%s",total_percentage,"%"));

        }else {
            final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
            models.add(new ArcProgressStackView.Model(getString(R.string.no_rates), 0, ContextCompat.getColor(activity,R.color.gray2),ContextCompat.getColor(activity,R.color.gray2)));
            binding.arcView.setTextColor(ContextCompat.getColor(activity,R.color.black));
            binding.arcView.setModels(models);
            binding.tvTotalRate.setText("0%");
        }
        binding.arcView.setVisibility(View.VISIBLE);


    }

    private void updateStage() {
        String data="";
        if (userModel.getData().getStage_fk()!=null){
            if (lang.equals("ar")){
                data += userModel.getData().getStage_fk().getTitle();

            }else {
                data += userModel.getData().getStage_fk().getTitle_en();

            }
        }
        if (userModel.getData().getClass_fk()!=null){
            data +=",";
            if (lang.equals("ar")){
                data += userModel.getData().getClass_fk().getTitle();

            }else {
                data += userModel.getData().getClass_fk().getTitle_en();

            }
        }
        if (userModel.getData().getDepartment_fk()!=null){

            data +=",";
            if (lang.equals("ar")){
                data += userModel.getData().getDepartment_fk().getTitle();

            }else {
                data += userModel.getData().getDepartment_fk().getTitle_en();

            }
        }
        binding.setStage(data);
    }


}
