package com.t3leem_live.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.t3leem_live.language.Language;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }

}

