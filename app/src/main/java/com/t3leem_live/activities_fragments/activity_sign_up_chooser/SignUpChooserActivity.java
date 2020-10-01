package com.t3leem_live.activities_fragments.activity_sign_up_chooser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_login.LoginActivity;
import com.t3leem_live.activities_fragments.activity_student_sign_up.StudentSignUpActivity;
import com.t3leem_live.activities_fragments.activity_teacher_sign_up.TeacherSignUpActivity;
import com.t3leem_live.databinding.ActivitySignUpChooserBinding;
import com.t3leem_live.language.Language;

public class SignUpChooserActivity extends AppCompatActivity {
    private ActivitySignUpChooserBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up_chooser);
        initView();
    }

    private void initView() {

        binding.imageLogo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but1));
        binding.btnTeacher.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but2));
        binding.btnStudent.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but3));
        binding.btnStudent.setOnClickListener(view -> navigateToStudentSignUpActivity());
        binding.btnTeacher.setOnClickListener(view -> navigateToTeacherSignUpActivity());
    }


    private void navigateToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void navigateToStudentSignUpActivity(){
        Intent intent = new Intent(this, StudentSignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTeacherSignUpActivity(){
        Intent intent = new Intent(this, TeacherSignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateToLoginActivity();
    }
}