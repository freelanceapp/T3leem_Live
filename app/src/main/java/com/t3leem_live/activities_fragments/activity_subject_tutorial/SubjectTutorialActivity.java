package com.t3leem_live.activities_fragments.activity_subject_tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivitySubjectTutorialBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;

import io.paperdb.Paper;

public class SubjectTutorialActivity extends AppCompatActivity {
    private ActivitySubjectTutorialBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private StageClassModel stageClassModel;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_subject_tutorial);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        preferences  = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        if (lang.equals("ar")){
            binding.setSubject(stageClassModel.getTitle());
        }else {
            binding.setSubject(stageClassModel.getTitle_en());

        }
        updateUiUserClassName();
    }

    private void updateUiUserClassName()
    {
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

        binding.setClassName(data);
    }
}