package com.t3leem_live.uis.module_student.activity_subject_tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.uis.module_student.activity_notes_pdf.NotesPdfActivity;
import com.t3leem_live.uis.module_student.activity_student_exam.StudentExamActivity;
import com.t3leem_live.uis.module_student.activity_student_honorary_board.HonoraryBoardActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers.StudentTeachersActivity;
import com.t3leem_live.uis.module_student.activity_summary.SummaryActivity;
import com.t3leem_live.uis.module_student.activity_videos.VideosActivity;
import com.t3leem_live.adapters.SummaryAdapter;
import com.t3leem_live.databinding.ActivitySubjectTutorialBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.SummaryDataModel;
import com.t3leem_live.models.SummaryModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectTutorialActivity extends AppCompatActivity {
    private ActivitySubjectTutorialBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private StageClassModel stageClassModel;
    private String lang = "ar";


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_tutorial);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        if (lang.equals("ar")) {
            binding.setSubject(stageClassModel.getTitle());
        } else {
            binding.setSubject(stageClassModel.getTitle_en());

        }


        binding.llVideo.setOnClickListener(view -> {
            Intent intent = new Intent(this, VideosActivity.class);
            intent.putExtra("data", stageClassModel);
            startActivity(intent);
        });

        binding.llNote.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotesPdfActivity.class);
            intent.putExtra("data", stageClassModel);
            startActivity(intent);
        });

        binding.llLiveVideo.setOnClickListener(view -> {
            Intent intent = new Intent(this, StudentTeachersActivity.class);
            intent.putExtra("data", stageClassModel);
            startActivity(intent);
        });

        binding.llQuiz.setOnClickListener(view -> {
            Intent intent = new Intent(this, StudentExamActivity.class);
            intent.putExtra("data", stageClassModel);
            startActivity(intent);
        });

        binding.llSummary.setOnClickListener(view -> {
            Intent intent = new Intent(this, SummaryActivity.class);
            intent.putExtra("data", stageClassModel);
            startActivity(intent);
        });

        binding.llHonoraryBoard.setOnClickListener(view -> {
            Intent intent = new Intent(this, HonoraryBoardActivity.class);
            startActivity(intent);
        });


        updateUiUserClassName();

    }


    private void updateUiUserClassName() {
        String data = "";
        if (userModel.getData().getStage_fk() != null) {
            if (lang.equals("ar")) {
                data += userModel.getData().getStage_fk().getTitle();

            } else {
                data += userModel.getData().getStage_fk().getTitle_en();

            }
        }
        if (userModel.getData().getClass_fk() != null) {
            data += ",";
            if (lang.equals("ar")) {
                data += userModel.getData().getClass_fk().getTitle();

            } else {
                data += userModel.getData().getClass_fk().getTitle_en();

            }
        }

        if (userModel.getData().getDepartment_fk() != null) {

            data += ",";
            if (lang.equals("ar")) {
                data += userModel.getData().getDepartment_fk().getTitle();

            } else {
                data += userModel.getData().getDepartment_fk().getTitle_en();

            }
        }

        binding.setClassName(data);
    }


}