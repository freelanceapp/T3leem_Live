package com.t3leem_live.activities_fragments.activity_about_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_on_boarding1.OnBoarding1Activity;
import com.t3leem_live.activities_fragments.activity_splash.SplashActivity;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
import com.t3leem_live.databinding.ActivityAboutAppBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.SettingDataModel;
import com.t3leem_live.models.SliderDataModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutAppActivity extends AppCompatActivity {
    private ActivityAboutAppBinding binding;
    private String lang = "ar";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about_app);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> finish());
        getSetting();
    }

    private void getSetting() {
        Api.getService(Tags.base_url)
                .getSetting()
                .enqueue(new Callback<SettingDataModel>() {
                    @Override
                    public void onResponse(Call<SettingDataModel> call, Response<SettingDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            String data = response.body().getSettings().getAbout_app();
                            binding.tvData.setText(Html.fromHtml(data));

                        } else {

                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SettingDataModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                            binding.progBar.setVisibility(View.GONE);

                            Toast.makeText(AboutAppActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }
}