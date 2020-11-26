package com.t3leem_live.uis.module_general.activity_on_boarding2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_login.LoginActivity;
import com.t3leem_live.uis.module_general.activity_on_boarding1.OnBoarding1Activity;
import com.t3leem_live.databinding.ActivityOnBoarding2Binding;
import com.t3leem_live.language.Language;

public class OnBoarding2Activity extends AppCompatActivity {
    private ActivityOnBoarding2Binding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_on_boarding2);
        initView();
    }

    private void initView() {

        binding.tv1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but1));
        binding.tv2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but2));
        binding.tv3.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but3));
        binding.tv4.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but4));
        binding.btnNext.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but5));

        binding.btnNext.setOnClickListener(view -> {
            navigateToLoginActivity();
        });

    }

    private void navigateToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void navigateToOnBoarding1Activity(){
        Intent intent = new Intent(this, OnBoarding1Activity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onBackPressed() {
        navigateToOnBoarding1Activity();
    }
}