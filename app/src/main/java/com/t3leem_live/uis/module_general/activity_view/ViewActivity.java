package com.t3leem_live.uis.module_general.activity_view;

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
import com.t3leem_live.databinding.ActivityViewBinding;
import com.t3leem_live.language.Language;

import java.util.HashMap;

public class ViewActivity extends AppCompatActivity {

    private ActivityViewBinding binding;
    private String url ="";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));

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

        if (url.contains(".pdf")){
            String u = "https://docs.google.com/gview?embedded=true&url="+url;
            binding.webView.loadUrl(u,new HashMap<>());
        }else {
            binding.webView.loadUrl(url);

        }


    }
}