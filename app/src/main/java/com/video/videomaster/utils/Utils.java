package com.video.videomaster.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;

import com.video.videomaster.objects.Folder;
import com.video.videomaster.objects.Video;
import com.video.videomaster.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }



    private static void getMediaInFolder(Context context, SparseArray<String> map, SparseArray<Integer> counterMap) {
        final String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;

        final String vselection = MediaStore.Video.VideoColumns.BUCKET_ID + "=?";

        for (int i = 0; i < map.size(); i++) {
            Integer bucketId = map.keyAt(i);
            final String[] selectionArgs = {bucketId.toString()};

            Cursor videocursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , VIDEO_PROJECTION
                    , vselection
                    , selectionArgs
                    , orderBy);

            if (videocursor != null && videocursor.moveToFirst()) {
                int video_column_index = videocursor.getColumnIndex(MediaStore.Video.Media._ID);
                int vdataColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DATA);
                int vdisplay_name_index = videocursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                int vduration_index = videocursor.getColumnIndex(MediaStore.Video.Media.DURATION);
                do {
                    int thumbId = videocursor.getInt(video_column_index);
                    Log.d("TAG", "tumbId: " + thumbId);
                    String mediaData = videocursor.getString(vdataColumnIndex);
                    Log.d("TAG", "mediaData:" + mediaData);
                    String displayName = videocursor.getString(vdisplay_name_index);
                    Log.d("TAG", "displayName: " + displayName);
                    String size = videocursor.getString(vduration_index);
                    Log.d("TAG", "size" + size);

                } while (videocursor.moveToNext());
                videocursor.close();
            }

            if (videocursor != null)
                videocursor.close();
        }

    }

    public static long downloadFile(Context context, String url, String fileName) {
        Log.d("TAG", "downloading: " + fileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading from server");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(context.getString(R.string.app_name), fileName);
        request.allowScanningByMediaScanner();
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }

    public static boolean deleteFile(String path) {
        File videofiles = new File(path);
        return videofiles.delete();
    }

    /**
     * @return Number of bytes available on internal storage
     */
    public static long getInternalAvailableSpace() {
        long availableSpace = -1L;
        try {StatFs stat = new StatFs(Environment.getDataDirectory()
                .getPath());
            stat.restat(Environment.getDataDirectory().getPath());
            availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableSpace;
    }

    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    private static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    static void youTubeDownload(String url, final Context context){

        String youtubeLink = "https://www.youtube.com/watch?v=dAsNMkBKpeU";

        YouTubeUriExtractor ytEx = new YouTubeUriExtractor(context) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 22;
                    String downloadUrl = ytFiles.get(itag).getUrl();

                    downloadFile(context, downloadUrl, "test3.mp3");
                }
            }
        };

        ytEx.execute(youtubeLink);

    }
    static void getVideoFromInstagram(final String url, String fileName){

        new Thread(new Runnable() {
            public void run() {
                try {
                    org.jsoup.nodes.Element connectionElement = Jsoup.connect(url).timeout(8000).ignoreHttpErrors(true).get().body();
                     Element element = connectionElement.child(7);
                    element.getElementsByAttribute("video_url");
                    element.getElementsContainingText("video_url");


                    connectionElement.getElementsContainingText("video_url");
                    connectionElement.getElementsByAttribute("video_url");

                    //String s = connectionElement.data();

                    //Log.d("zaq", s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static ArrayList<Folder> getRootFolders(Context context) {


        Log.d("TAG", "getRootFolders is called");


        String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.SIZE};


        ArrayList<Folder> folders = new ArrayList<>();
        Cursor vidCsr = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                null, null, null);//MediaStore.Video.VideoColumns.DATE_ADDED
        getMediaIdnName(folders, vidCsr, context.getString(R.string.app_name));
        Log.d("TAG", "video folders loading finished");
        return folders;

    }
    private static ArrayList<Folder> getMediaIdnName(ArrayList<Folder> folders, Cursor videoCsr, String appName) {
        // getting Bucket ID and Display Name Column number for both Images and Videos

        int colId = 0;
        int colData = 1;
        int colDisplayName = 2;
        int colDuration = 3;
        int colBucketId = 4;
        int colBucketDisplayName = 5;
        int colSize = 6;

        // Image and Video count as returned by Cursor
        SparseArray<Folder> folderMap = new SparseArray<>();
        if (videoCsr.moveToFirst()) {
            do {
                Video video = new Video(
                        videoCsr.getString(colDisplayName),
                        videoCsr.getInt(colId),
                        videoCsr.getInt(colDuration),
                        videoCsr.getString(colData),
                        formatSize(videoCsr.getLong(colSize)));
                int vidBucketId = videoCsr.getInt(colBucketId);
                String vidBucketName = videoCsr.getString(colBucketDisplayName);
                Folder folder = folderMap.get(vidBucketId);
                if (folder == null) {
                    folder = new Folder(vidBucketName, vidBucketId);
                    folderMap.put(vidBucketId, folder);
                }
                folder.getVideos().add(video);

            } while (videoCsr.moveToNext());
        }
        videoCsr.close();

        for (int i = 0; i < folderMap.size(); i++) {
            Folder folder = folderMap.valueAt(i);
            if (folder.getFolderName().equals(appName)){
                folders.add(0, folder);
            } else {
                folders.add(folder);
            }
        }
        return folders;
    }
}
