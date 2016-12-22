package com.video.videomaster.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.video.videomaster.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static long downId;
    public static String pathId;
    public final static int CODE_STORAGE_PERMISSION = 100;
    public static final int CODE_REQUEST_DELETE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        setViews();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("ZAQ","Permission is granted");
                return true;
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
        return false;
    }

    private void setViews() {
        ImageView vuImage = (ImageView)findViewById(R.id.vuImageView);
        ImageView vimeoImage = (ImageView)findViewById(R.id.vimeoImageView);
        ImageView vineImage = (ImageView)findViewById(R.id.vineImageView);
        ImageView facebookImageView = (ImageView)findViewById(R.id.facebookImageView);
        ImageView instagramImageView = (ImageView)findViewById(R.id.instagramImageView);
        vuImage.setOnClickListener(this);
        vimeoImage.setOnClickListener(this);
        vineImage.setOnClickListener(this);
        facebookImageView.setOnClickListener(this);
        instagramImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
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
}
