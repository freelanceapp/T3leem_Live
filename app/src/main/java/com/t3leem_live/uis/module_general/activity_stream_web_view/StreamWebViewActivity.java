package com.t3leem_live.uis.module_general.activity_stream_web_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivityStreamWebViewBinding;
import com.t3leem_live.language.Language;

import io.paperdb.Paper;

public class StreamWebViewActivity extends AppCompatActivity {

    private ActivityStreamWebViewBinding binding;
    private String url ="";
    private String lang = "ar";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_stream_web_view);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        Log.e("url2",url);
        binding.setLang(lang);
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
                binding.flLoading.setVisibility(View.GONE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                binding.flLoading.setVisibility(View.GONE);
            }
        });

        binding.webView.loadUrl(url);



    }

}