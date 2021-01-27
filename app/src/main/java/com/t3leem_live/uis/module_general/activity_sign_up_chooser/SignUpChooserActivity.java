package com.t3leem_live.uis.module_general.activity_sign_up_chooser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_center_course.activity_center_sign_up.CenterCourseSignUpActivity;
import com.t3leem_live.uis.module_general.activity_login.LoginActivity;
import com.t3leem_live.uis.module_parent.activity_parent_sign_up.ParentSignUpActivity;
import com.t3leem_live.uis.module_student.activity_student_sign_up.StudentSignUpActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_sign_up.TeacherSignUpActivity;
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

        //binding.imageLogo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but1));
        binding.btnTeacher.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but1));
        binding.btnStudent.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but2));
        binding.btnParent.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but3));
        binding.btncenter.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but4));

        binding.btnStudent.setOnClickListener(view -> navigateToStudentSignUpActivity());
        binding.btnTeacher.setOnClickListener(view -> navigateToTeacherSignUpActivity());
        binding.btnParent.setOnClickListener(view -> navigateToParentSignUpActivity());
        binding.btncenter.setOnClickListener(view -> navigateToCenterSignUpActivity());


    }

    private void navigateToCenterSignUpActivity() {
        Intent intent = new Intent(this, CenterCourseSignUpActivity.class);
        startActivity(intent);
        finish();
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

    private void navigateToParentSignUpActivity(){
        Intent intent = new Intent(this, ParentSignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateToLoginActivity();
    }
}