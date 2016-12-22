package com.video.videomaster;

import android.app.Application;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Y1ekJsWXUFwP9FhxsAcQAsw65";
    private static final String TWITTER_SECRET = "QK8x0D68PArBk3NaYOBATz5dUnIxaPUwOD9b8j56b60r96HOVk";
    private Twitter twitter;


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        twitter = new Twitter(authConfig);
        Fabric.with(this, new Crashlytics(), twitter);
//        ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//        clipBoard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
//            @Override
//            public void onPrimaryClipChanged() {
//                Toast.makeText(getApplicationContext(), "clippp", Toast.LENGTH_SHORT).show();
//            }
//        });
        startService(new Intent(this, ClipboardService.class));
    }
}

