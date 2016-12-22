package com.video.videomaster;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.video.videomaster.activities.WebViewActivity;

public class ClipboardService extends Service {
    public static String FROM_NOTIFICATION = "fromNotification";
    private Intent notificationIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                if (item.toString().contains("instagram")) {
                    ////Add Notification
                    notificationIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    notificationIntent.putExtra(FROM_NOTIFICATION, true);
                    notificationIntent.putExtra(WebViewActivity.URL, WebViewActivity.INSTAGRAM);

                    PendingIntent contentIntent = PendingIntent.getActivity(
                            getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Press here to download this video")
                                    .setContentText("Just Click")
                                    .setAutoCancel(true)
                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                    .setVibrate(new long[]{150, 150})
                                    .setContentIntent(contentIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());

                    ////Add Floating button
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.TYPE_PHONE,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                            PixelFormat.TRANSLUCENT);
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                    layoutParams.x = 50;
                    layoutParams.y = 50;
                    @SuppressLint("InflateParams") final
                    View btn = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.button, null);
                    final WindowManager windowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
                    windowManager.addView(btn, layoutParams);
                    btn.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            startActivity(notificationIntent);
                            close(v);
                            return false;
                        }
                    });
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            close(btn);
                        }
                    }, 4000);
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY_COMPATIBILITY;
    }

    public void close(View view) {
        try {
            final WindowManager windowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
            windowManager.removeView(view);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopSelf();
        }
    }

}
