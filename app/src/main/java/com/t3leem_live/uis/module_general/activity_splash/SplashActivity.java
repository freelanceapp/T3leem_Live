package com.t3leem_live.uis.module_general.activity_splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_center_course.activity_home_center.CenterHomeActivity;
import com.t3leem_live.uis.module_parent.activity_home_parent.ParentHomeActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_home.TeacherHomeActivity;
import com.t3leem_live.uis.module_general.activity_on_boarding1.OnBoarding1Activity;
import com.t3leem_live.uis.module_student.activity_student_home.StudentHomeActivity;
import com.t3leem_live.databinding.ActivitySplashBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Animation animation;
    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        animation = AnimationUtils.loadAnimation(this,R.anim.anim_but1);
        binding.imageLogo.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent;
                if (userModel==null){
                    intent = new Intent(SplashActivity.this, OnBoarding1Activity.class);
                }else {
                    if (userModel.getData().getUser_type().equals("student")){
                        intent = new Intent(SplashActivity.this, StudentHomeActivity.class);
                    }else if (userModel.getData().getUser_type().equals("teacher")){
                        intent = new Intent(SplashActivity.this, TeacherHomeActivity.class);

                    }else if(userModel.getData().getUser_type().equals("parent")){
                        intent = new Intent(SplashActivity.this, ParentHomeActivity.class);

                    }
                    else {
                        intent = new Intent(SplashActivity.this, CenterHomeActivity.class);

                    }
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}