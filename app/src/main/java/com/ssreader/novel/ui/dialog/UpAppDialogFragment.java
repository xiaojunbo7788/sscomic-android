package com.ssreader.novel.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseDialogFragment;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.net.DownLoadUtils.AppDownloadService;
import com.ssreader.novel.net.DownLoadUtils.DownloadListener;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.TextStyleUtils;
import com.ssreader.novel.ui.view.ProgressBarView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.io.File;

import butterknife.BindView;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * 更新弹窗
 */
public class UpAppDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialog_update_desc_layout)
    RelativeLayout descLayout;
    @BindView(R.id.dialog_updateapp_sec)
    TextView upAppDesc;
    @BindView(R.id.imageView)
    View imageView;

    @BindView(R.id.dialog_updateapp_no)
    TextView dialog_updateapp_no;
    @BindView(R.id.dialog_updateapp_yes)
    TextView dialog_updateapp_yes;
    @BindView(R.id.dialog_updateapp_layout)
    View dialog_updateapp_layout;

    @BindView(R.id.dialog_updateapp_view)
    View dialog_updateapp_view;
    @BindView(R.id.materialSeekBar)
    ProgressBarView materialSeekBar;

    private AppDownloadService.DownloadBinder downloadBinder;
    private AppUpdate.VersionUpdateBean updateBean;
    private int mWidth, mHeight, imageHeight;
    // 通知栏管理
    private NotificationManager notificationManager;
    private boolean flag;

    public UpAppDialogFragment() {

    }

    public UpAppDialogFragment(Activity activity, AppUpdate.VersionUpdateBean updateBean) {
        super(false);
        this.setCancelable(false);
        this.activity = activity;
        this.updateBean = updateBean;
        mWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 80);
        mHeight = ScreenSizeUtils.getScreenHeight(activity) / 2;
        notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int initContentView() {
        return R.layout.dialog_update_app;
    }

    @Override
    public void initView() {
        descLayout.setBackground(MyShape.setMyshape(0, 0, ImageUtil.dp2px(activity, 8),
                ImageUtil.dp2px(activity, 10), ContextCompat.getColor(activity, R.color.white)));
        dialog_updateapp_yes.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 25), ContextCompat.getColor(activity, R.color.updateblue)));
        // 图片
        ViewGroup.LayoutParams imageParams = imageView.getLayoutParams();
        imageParams.width = mWidth;
        imageParams.height = imageHeight = mWidth * 249 / 520;
        imageView.setLayoutParams(imageParams);
        // 文字, 最低高度图片高度，最高高度屏幕高度2分之一减图片高度
        upAppDesc.setText(updateBean.getMsg());
        int descLineCount = TextStyleUtils.getTextViewLines(upAppDesc, mWidth);
        ViewGroup.LayoutParams layoutParams = descLayout.getLayoutParams();
        RelativeLayout.LayoutParams descParams = (RelativeLayout.LayoutParams) upAppDesc.getLayoutParams();
        if (descLineCount > 11) {
            descParams.height = mHeight - imageHeight;
        } else if (descLineCount > 6) {
            descParams.height = descLineCount * ImageUtil.dp2px(activity, 20.5f);
        } else {
            descParams.height = imageHeight;
        }
        layoutParams.height = descParams.height + ImageUtil.dp2px(activity, 40);
        upAppDesc.setLayoutParams(descParams);
        descLayout.setLayoutParams(layoutParams);

        upAppDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (updateBean.getStatus() == 1) {
            dialog_updateapp_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAllowingStateLoss();
                }
            });
        } else {
            dialog_updateapp_view.setVisibility(View.GONE);
            dialog_updateapp_no.setVisibility(View.GONE);
        }
        initListener();
    }

    private void initListener() {
        dialog_updateapp_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    flag = true;
                    Intent intent = new Intent(activity, AppDownloadService.class);
                    activity.bindService(intent, connection, BIND_AUTO_CREATE);
                }
                if (updateBean.getStatus() == 1) {
                    dismissAllowingStateLoss();
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.app_upapp_hoytai));
                } else {
                    materialSeekBar.setVisibility(View.VISIBLE);
                    dialog_updateapp_layout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (AppDownloadService.DownloadBinder) service;
            downloadBinder.startDownload(updateBean.getUrl(), listener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private DownloadListener listener = new DownloadListener() {
        //(api = 26)
        @SuppressLint("StringFormatInvalid")
        @Override
        public void onProgress(long total, int progress) {
            if (updateBean.getStatus() == 2) {
                materialSeekBar.setProgress(progress);
            } else {
                // 通知栏显示下载进度
                NotificationCompat.Builder notification = new NotificationCompat.Builder(activity, BuildConfig.APPLICATION_ID)
                        .setSmallIcon(R.mipmap.appic)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setProgress(100, progress, false)
                        .setContentTitle(LanguageUtil.getString(activity, R.string.ComicDownActivity_downing))
                        .setContentText(activity.getResources().getString(R.string.UpAppPopWindow_downing, progress + ""))
                        .setAutoCancel(false);
                notificationManager.notify(BuildConfig.APPLICATION_ID, 1, notification.build());
            }
        }

        //(api = 26)
        @Override
        public void onSuccess(File file) {
            if (updateBean.getStatus() == 2) {
                materialSeekBar.setProgress(100);
                activity.startActivity(installApp(activity, file));
            } else {
                NotificationCompat.Builder notification = new NotificationCompat.Builder(activity, BuildConfig.APPLICATION_ID)
                        .setSmallIcon(R.mipmap.appic)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setProgress(100, 100, false)
                        .setContentTitle(LanguageUtil.getString(activity, R.string.UpAppPopWindow_downed))
                        .setContentText(LanguageUtil.getString(activity, R.string.UpAppPopWindow_downed_progress))
                        .setAutoCancel(true);
                if (installApp(activity, file) != null) {
                    PendingIntent pendingIntents = PendingIntent.getActivity(activity, 0, installApp(activity, file), PendingIntent.FLAG_ONE_SHOT);
                    notification.setContentIntent(pendingIntents);
                }
                notificationManager.cancelAll();
                notificationManager.notify(BuildConfig.APPLICATION_ID, 1, notification.build());
            }
        }

        //(api = 26)
        @Override
        public void onFailed() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onCanceled() {

        }
    };

    private Intent installApp(Context pContext, File pFile) {
        if (null == pFile)
            return null;
        if (!pFile.exists())
            return null;
        Intent _Intent = new Intent();
        _Intent.setAction(Intent.ACTION_VIEW);
        Uri _uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _Intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            _uri = FileProvider.getUriForFile(pContext, BuildConfig.APPLICATION_ID + ".fileprovider", pFile);
        } else {
            _uri = Uri.fromFile(pFile);
            _Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        _Intent.setDataAndType(_uri, "application/vnd.android.package-archive");
        return _Intent;
    }
}
