package com.t3leem_live.uis.module_general.activity_on_boarding1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_on_boarding2.OnBoarding2Activity;
import com.t3leem_live.databinding.ActivityOnBoarding1Binding;
import com.t3leem_live.language.Language;

public class OnBoarding1Activity extends AppCompatActivity {
    private ActivityOnBoarding1Binding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_on_boarding1);
        initView();
    }

    private void initView() {

        binding.tv1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but1));
        binding.tv2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but2));
        binding.tv3.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but3));
        binding.tv4.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but4));
        binding.btnNext.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_but5));

        binding.btnNext.setOnClickListener(view -> {
            navigateToOnBoarding2Activity();
        });

    }

    private void navigateToOnBoarding2Activity(){
        Intent intent = new Intent(this, OnBoarding2Activity.class);
        startActivity(intent);
        finish();
    }
}