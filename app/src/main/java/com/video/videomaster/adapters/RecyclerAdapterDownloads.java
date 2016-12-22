package com.video.videomaster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.videomaster.R;
import com.video.videomaster.objects.Folder;
import com.video.videomaster.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class RecyclerAdapterDownloads extends RecyclerView.Adapter<RecyclerAdapterDownloads.ViewHolder> {

    private ArrayList<Folder> folderList;

    public RecyclerAdapterDownloads(ArrayList<Folder> videoList) {
        this.folderList = videoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView folderLabel, folderCount;

        public ViewHolder(View itemView) {
            super(itemView);
            folderLabel = (TextView) itemView.findViewById(R.id.folder_label);
            //folderCount = (TextView)itemView.findViewById(R.id.folder_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Folder folder = (Folder) itemView.getTag();
            EventBus.getDefault().post(new MessageEvent(getAdapterPosition()));
        }


    }

    @Override
    public RecyclerAdapterDownloads.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_row, parent, false);
        return new RecyclerAdapterDownloads.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterDownloads.ViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.folderLabel.setText(folder.getFolderName());
        holder.itemView.setTag(folder);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }


}
