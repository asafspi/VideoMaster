package com.video.videomaster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.startapp.android.publish.StartAppAd;
import com.video.videomaster.R;
import com.video.videomaster.adapters.RecyclerAdapterDownloads;
import com.video.videomaster.adapters.RecyclerAdapterVideoList;
import com.video.videomaster.events.MessageEvent;
import com.video.videomaster.objects.Folder;
import com.video.videomaster.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class DownloadsActivity extends AppCompatActivity {

    private ArrayList<Folder> folders;
    private RecyclerView recyclerView;
    private boolean insideFolder;
    public static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        folders = Utils.getRootFolders(this);
        setRecyclerView();
    }

    public void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.downloads_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(new RecyclerAdapterDownloads(folders));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        insideFolder = true;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        position = event.getPosition();
        recyclerView.setAdapter(new RecyclerAdapterVideoList(folders.get(event.getPosition()).getVideos()));
    }

    @Override
    public void onBackPressed() {
        if(insideFolder){
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setAdapter(new RecyclerAdapterDownloads(folders));
            insideFolder = false;
            return;
        }
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        if(insideFolder){
            folders = Utils.getRootFolders(this);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(new RecyclerAdapterVideoList(folders.get(position).getVideos()));
        }
        super.onResume();
    }

}
