package com.t3leem_live.uis.module_parent.activity_son_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.adapters.StudentExamsAdapter;
import com.t3leem_live.databinding.ActivitySonDetailsBinding;
import com.t3leem_live.databinding.ActivityStudentExamBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.TeacherExamDataModel;
import com.t3leem_live.models.TeacherExamModel;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_general.activity_view.ViewActivity;
import com.t3leem_live.uis.module_parent.activity_son_profile.SonProfileActivity;
import com.t3leem_live.uis.module_parent.activity_subjects.SubjectActivity;
import com.t3leem_live.uis.module_student.activity_son_subjects.SonSubjectActivity;
import com.t3leem_live.uis.module_student.activity_student_exam.StudentExamActivity;
import com.t3leem_live.uis.module_student.activity_student_profile.StudentProfileActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SonDetailsActivity extends AppCompatActivity {
    private ActivitySonDetailsBinding binding;
    private String lang;
    private UserModel.User user;




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_son_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        user = (UserModel.User) intent.getSerializableExtra("data");
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);

        binding.cardRate.setOnClickListener(view -> {
            Intent intent = new Intent(this, SonProfileActivity.class);
            intent.putExtra("data",user);
            startActivity(intent);
        });

        binding.cardTest.setOnClickListener(view -> {
            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra("data", user);
            startActivity(intent);
        });


    }


}