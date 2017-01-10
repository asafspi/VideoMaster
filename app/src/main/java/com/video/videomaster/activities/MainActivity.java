package com.video.videomaster.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.interstitial.RevMobFullscreen;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.StartAppNativeAd;
import com.video.videomaster.R;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static long downId;
    public static String pathId;
    public final static int CODE_STORAGE_PERMISSION = 100;
    public static final int CODE_REQUEST_DELETE = 200;
    private RevMob revmob;
    private RevMobFullscreen fullscreen;
    private boolean fullscreenIsLoaded;
    public static boolean showRevMob = false;
    public static Integer counter = 0;
    private StartAppNativeAd startAppNativeAd = new StartAppNativeAd(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String startAppId = "208673432";
        StartAppSDK.init(this, startAppId, true);
        startAppNativeAd.loadAd(new NativeAdPreferences());
        checkPermission();
        setViews();

        fullscreenIsLoaded = false;
        revmob = RevMob.startWithListener(this, null, "5868cc144713185a029036d2");
    }

    public void loadFullscreen() {
        //load it with RevMob listeners to control the events fired
        fullscreen = revmob.createFullscreen(this, new RevMobAdsListener() {
            @Override
            public void onRevMobAdReceived() {
                Log.i("RevMob", "Fullscreen loaded.");
                fullscreenIsLoaded = true;
                showFullscreen();
            }

            @Override
            public void onRevMobAdNotReceived(String message) {
                Log.i("RevMob", "Fullscreen not received.");
            }

            @Override
            public void onRevMobAdDismissed() {
                Log.i("RevMob", "Fullscreen dismissed.");
            }

            @Override
            public void onRevMobAdClicked() {
                Log.i("RevMob", "Fullscreen clicked.");
            }

            @Override
            public void onRevMobAdDisplayed() {
                Log.i("RevMob", "Fullscreen displayed.");
            }
        });
    }

    public void showFullscreen() {
        if (fullscreenIsLoaded) {
            fullscreen.show(); // call it wherever you want to show the fullscreen ad
        } else {
            Log.i("RevMob", "Ad not loaded yet.");
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("ZAQ", "Permission is granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
        return false;
    }

    private void setViews() {
        ImageView vuImage = (ImageView) findViewById(R.id.vuImageView);
        ImageView vimeoImage = (ImageView) findViewById(R.id.vimeoImageView);
        ImageView vineImage = (ImageView) findViewById(R.id.vineImageView);
        ImageView facebookImageView = (ImageView) findViewById(R.id.facebookImageView);
        ImageView instagramImageView = (ImageView) findViewById(R.id.instagramImageView);
        ImageView youtubeImageView = (ImageView) findViewById(R.id.youTubeImageView);
        ImageView dailyMotionImageView = (ImageView) findViewById(R.id.dailyImageView);
        ImageView spotifyImageView = (ImageView) findViewById(R.id.spotifyImageView);
        ImageView shareImageView = (ImageView) findViewById(R.id.shareImageView);
        ImageView rateImageView = (ImageView) findViewById(R.id.rateImageView);
        ImageView moreApps = (ImageView) findViewById(R.id.moreApps);
        ImageView twitterImageView = (ImageView) findViewById(R.id.twitterImageView);
        twitterImageView.setOnClickListener(this);
        vuImage.setOnClickListener(this);
        vimeoImage.setOnClickListener(this);
        shareImageView.setOnClickListener(this);
        rateImageView.setOnClickListener(this);
        vineImage.setOnClickListener(this);
        facebookImageView.setOnClickListener(this);
        instagramImageView.setOnClickListener(this);
        youtubeImageView.setOnClickListener(this);
        dailyMotionImageView.setOnClickListener(this);
        spotifyImageView.setOnClickListener(this);
        moreApps.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.vimeoImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.VIMEO);
                break;
            case R.id.vuImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.VUCLIP);
                break;
            case R.id.vineImageView:
                intent = new Intent(this, VineActivity.class);
                break;
            case R.id.facebookImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.FACEBOOK);
                break;
            case R.id.instagramImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.INSTAGRAM);
                break;
            case R.id.youTubeImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.YOUTUBE);
                break;
            case R.id.dailyImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.DAILYMOTION);
                break;
            case R.id.spotifyImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.SPOTIFY);
                break;
            case R.id.twitterImageView:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, WebViewActivity.TWITTER);
                break;
            case R.id.moreApps:
                startAppNativeAd = new StartAppNativeAd(this);
                startAppNativeAd.loadAd(new NativeAdPreferences());

                // Declare Native Ad Preferences
                NativeAdPreferences nativePrefs = new NativeAdPreferences()
                        .setAdsNumber(3)                // Load 3 Native Ads
                        .setAutoBitmapDownload(true)    // Retrieve Images object
                        .setPrimaryImageSize(2);        // 150x150 image

                // Declare Ad Callbacks Listener
                AdEventListener adListener = new AdEventListener() {     // Callback Listener
                    @Override
                    public void onReceiveAd(Ad arg0) {
                        // Native Ad received
                        ArrayList<NativeAdDetails> ads = startAppNativeAd.getNativeAds();    // get NativeAds list
                        ads.get(0).sendClick(getApplicationContext());

                        // Print all ads details to log
                        Iterator<NativeAdDetails> iterator = ads.iterator();
                        while (iterator.hasNext()) {
                            Log.d("MyApplication", iterator.next().toString());
                        }
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad arg0) {
                        // Native Ad failed to receive
                        Log.e("MyApplication", "Error while loading Ad");
                    }
                };

//                Load Native Ads
                startAppNativeAd.loadAd(nativePrefs, adListener);
                return;
            case R.id.rateImageView:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                return;
            case R.id.shareImageView:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Check out this Video Downloader -" + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey! ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.getItem(0).setIcon(android.R.drawable.presence_video_online);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filesOptionMenu:
                Intent intent = new Intent(this, DownloadsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        if (counter % 3 == 0) {
            loadFullscreen(); // pre-cache it without showing it
        }
        showRevMob = !showRevMob;
        counter++;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}
