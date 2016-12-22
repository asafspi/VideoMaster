package com.video.videomaster.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.video.videomaster.R;
import com.video.videomaster.activities.DeleteDialogActivity;
import com.video.videomaster.objects.Video;
import com.video.videomaster.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

import static com.video.videomaster.activities.DeleteDialogActivity.EXTRA_PATH;
import static com.video.videomaster.activities.DeleteDialogActivity.EXTRA_URI;
import static com.video.videomaster.activities.DownloadDialogActivity.EXTRA_VIDEO_TITLE;
import static com.video.videomaster.activities.MainActivity.CODE_REQUEST_DELETE;

public class RecyclerAdapterVideoList extends RecyclerView.Adapter<RecyclerAdapterVideoList.ViewHolder> {


    private ArrayList<Video> videos;

    public RecyclerAdapterVideoList(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView videoLabel, videoDuration, videoSize;
        private ImageView thumbnail, iconDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            videoLabel = (TextView) itemView.findViewById(R.id.video_label);
            videoDuration = (TextView) itemView.findViewById(R.id.video_duration);
            videoSize = (TextView)itemView.findViewById(R.id.video_size);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            iconDelete = (ImageView) itemView.findViewById(R.id.icone_delete);
            iconDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Video video = (Video) itemView.getTag();
            if (video != null) {
                if (view.equals(iconDelete)) {
                   Intent deleteIntent = new Intent(view.getContext(), DeleteDialogActivity.class);
                    deleteIntent.putExtra(EXTRA_VIDEO_TITLE, videoLabel.getText());
                    deleteIntent.putExtra(EXTRA_PATH, video.getPathToFile());
                    deleteIntent.putExtra(EXTRA_URI, video.getVideoUri().toString());
                    ((Activity)view.getContext()).startActivityForResult(deleteIntent, CODE_REQUEST_DELETE);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(video.getVideoUri(), "video/mp4");
                    itemView.getContext().startActivity(intent);
                }
            }


        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
        return new RecyclerAdapterVideoList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Video video = videos.get(position);
        Picasso
                .with(holder.itemView.getContext())
                .load(video.getVideoUri())
                .fit()
                .centerInside()
                .into(holder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        String path = video.getPathToFile();
                        Utils.deleteFile(path);
                        Uri uri = Uri.parse(video.getVideoUri().toString());
                        holder.itemView.getContext().getContentResolver().delete(uri, null, null);
                        holder.itemView.post(new Runnable() {
                            @Override
                            public void run() {
                                videos.remove(video);
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
        String videoName = video.getVideoName();
        if (videoName == null) {
            String path = video.getPathToFile();
            Utils.deleteFile(path);
            Uri uri = Uri.parse(video.getVideoUri().toString());
            holder.itemView.getContext().getContentResolver().delete(uri, null, null);
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    videos.remove(video);
                    notifyDataSetChanged();
                }
            });
            return;

        }
        boolean contain = videoName.contains(".mp4") || videoName.contains(".MP4");
        String splitter = null;
        if (contain) {
            splitter = videoName.contains(".mp4") ? ".mp4" : "MP4";
        }
        String title = (contain ? videoName.split(splitter)[0] : videoName);
        holder.videoLabel.setText(title);
        if (video.getVideoSize() != null) {
            holder.videoSize.setText(video.getVideoSize());
        }
        holder.videoDuration.setText(String.format(Locale.getDefault(), "%02d:%02d", ((video.getVideoLength() / 1000) % 3600) / 60, video.getVideoLength() / 1000 % 60));
        holder.itemView.setTag(video);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


}
