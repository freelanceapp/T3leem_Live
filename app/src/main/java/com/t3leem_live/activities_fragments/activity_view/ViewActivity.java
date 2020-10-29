package com.t3leem_live.activities_fragments.activity_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_on_boarding1.OnBoarding1Activity;
import com.t3leem_live.activities_fragments.activity_splash.SplashActivity;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
import com.t3leem_live.databinding.ActivitySplashBinding;
import com.t3leem_live.databinding.ActivityViewBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;

import java.util.HashMap;

public class ViewActivity extends AppCompatActivity {

    private ActivityViewBinding binding;
    private String url ="";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

    }

    private void initView() {
        Log.e("url2",url);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                binding.progBar.setVisibility(View.GONE);
            }
        });

        Log.e("url",url);
        if (url.contains(".pdf")){
            String u = "https://docs.google.com/gview?embedded=true&url="+url;
            binding.webView.loadUrl(u,new HashMap<>());
        }else {
            binding.webView.loadUrl(url);

        }


    }
}