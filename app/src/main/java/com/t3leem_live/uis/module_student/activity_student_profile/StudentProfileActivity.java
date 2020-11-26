package com.t3leem_live.uis.module_student.activity_student_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivityStudentProfileBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StudentRateModel;
import com.t3leem_live.models.TeacherModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import devlight.io.library.ArcProgressStackView;

public class StudentProfileActivity extends AppCompatActivity {
    private ActivityStudentProfileBinding binding;
    private TeacherModel teacherModel;
    private List<StudentRateModel> studentRateModelList;
    private String lang = "ar";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_profile);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        teacherModel = (TeacherModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        binding.setModel(teacherModel);
        studentRateModelList = new ArrayList<>();
        studentRateModelList.addAll(teacherModel.getStudent_rates());
        updateUi();
        updateStage();
    }


    private void updateUi() {
        if (studentRateModelList.size()>0){
            double total_rate=0.0;
            double total_degree = 0.0;
            int [] colors = {ContextCompat.getColor(this,R.color.color17),ContextCompat.getColor(this,R.color.color18),ContextCompat.getColor(this,R.color.color19),ContextCompat.getColor(this,R.color.color20),ContextCompat.getColor(this,R.color.color21)};
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
        if (teacherModel.getStage_fk()!=null){
            if (lang.equals("ar")){
                data += teacherModel.getStage_fk().getTitle();

            }else {
                data += teacherModel.getStage_fk().getTitle_en();

            }
        }
        if (teacherModel.getClass_fk()!=null){
            data +=",";
            if (lang.equals("ar")){
                data += teacherModel.getClass_fk().getTitle();

            }else {
                data += teacherModel.getClass_fk().getTitle_en();

            }
        }
        if (teacherModel.getDepartment_fk()!=null){

            data +=",";
            if (lang.equals("ar")){
                data += teacherModel.getDepartment_fk().getTitle();

            }else {
                data += teacherModel.getDepartment_fk().getTitle_en();

            }
        }
        binding.setStage(data);
    }
}