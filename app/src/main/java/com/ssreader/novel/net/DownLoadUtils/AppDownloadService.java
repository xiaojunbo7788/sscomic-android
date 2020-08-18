package com.ssreader.novel.net.DownLoadUtils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.R;
import com.ssreader.novel.model.ChapterContent;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.utils.FileManager;

import java.io.File;

/**
 * 下载服务
 */
public class AppDownloadService extends Service {

    final String CHANNEL_ID = "channel_id_1";
    final String CHANNEL_NAME = "App Update";

    private DownloadTask downloadTask;
    private String downloadUrl;

    private DownloadBinder mBinder = new DownloadBinder();

    public class DownloadBinder extends Binder {

        public void startDownload(String url, DownloadListener listener) {
            if (downloadTask == null) {
                downloadUrl = url;
                String localpath = FileManager.getAPP();
                String houzhui = downloadUrl.substring(downloadUrl.lastIndexOf("."));//后缀
                houzhui= ("BW"+BuildConfig.VERSION_CODE) +houzhui;
                File file = new File(localpath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                localpath = localpath + houzhui;
                downloadTask = new DownloadTask(listener, getApplication());
                downloadTask.execute(downloadUrl, localpath);
            }
        }

        public void startDownload(ChapterContent chapterContent, DownloadListener listener) {
            if (downloadTask == null) {
                downloadUrl = chapterContent.getContent();
                String localpath = FileManager.getAudioLocal(chapterContent);

                String houzhui = downloadUrl.substring(downloadUrl.lastIndexOf("."));//后缀
                localpath = localpath +houzhui;

                downloadTask = new DownloadTask(listener, getApplication());
                downloadTask.execute(downloadUrl, localpath);
            }
        }


        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }
            if (downloadUrl != null) {
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStorageDirectory().getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(AppDownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = 26)
    private Notification getNotification(String s, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivities(this, 0, new Intent[]{intent}, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            getNotificationManager().createNotificationChannel(notificationChannel);
        }
        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.appic);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.appic));
        builder.setContentIntent(pi);
        builder.setContentTitle(s);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}
