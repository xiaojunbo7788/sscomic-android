package com.ssreader.novel.base;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.iflytek.cloud.SpeechUtility;
import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.StarrySkyBuilder;
import com.lzx.starrysky.StarrySkyConfig;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.MyObjectBox;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.net.OkHttp3;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.net.ResultCallback;
import com.ssreader.novel.ui.bwad.TTAdManagerHolder;
import com.ssreader.novel.ui.utils.Cockroach;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;
import com.ssreader.novel.utils.cache.ClearCacheManager;

import io.objectbox.BoxStore;
import okhttp3.Request;

import static com.ssreader.novel.constant.Constant.*;

/**
 * 松鼠仓库
 */
public class BWNApplication extends Application {

    public static BWNApplication applicationContext;
    private BoxStore boxStore;
    private FragmentActivity activity;
    private boolean IsMainActivityStartUp;

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public void setBoxStore(BoxStore boxStore) {
        this.boxStore = boxStore;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public boolean isMainActivityStartUp() {
        return IsMainActivityStartUp;
    }

    public void setMainActivityStartUp(boolean mainActivityStartUp) {
        IsMainActivityStartUp = mainActivityStartUp;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        if (!SUE_LOG) {
            Cockroach.install(new Cockroach.ExceptionHandler() {
                @Override
                public void handlerException(final Thread thread, final Throwable throwable) {

                }
            });
        }
        //数据库
        if (!ShareUitls.getBoolean(this, "SS" + BuildConfig.VERSION_CODE, false)) {
            ClearCacheManager.clearObjectBoxAllCache(applicationContext);
            ShareUitls.putBoolean(this, "SS" + BuildConfig.VERSION_CODE, true);
        }
        boxStore = MyObjectBox.builder().androidContext(applicationContext).build();
        // 讯飞
        SpeechUtility.createUtility(this, "appid=" + KDXF_APPID);
        // 友盟
        initUM();
        // 阿里移动推送
        initCloudChannel(this);
        // 音频播放框架
        initStarrySky();
        //监听所有Activity 的生命周期回调
        registerActivityLifecycleCallbacks(new ActivityLifecycleListener());
        // 广告
        if (USE_AD_FINAL) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    String processName = getProcessName(this);
                    if (!BuildConfig.APPLICATION_ID.equals(processName)) {
                        WebView.setDataDirectorySuffix(processName);
                    }
                }
            } catch (Throwable e) {
            }
            TTAdManagerHolder.init(this);
        }
    }

    private void initUM() {
        UMConfigure.init(this, UMENG, UserUtils.getChannelName(this),
                UMConfigure.DEVICE_TYPE_PHONE, "");
        if (USE_WEIXIN) {
            PlatformConfig.setWeixin(WEIXIN_PAY_APPID, WEIXIN_APP_SECRET);
        }
        if (USE_QQ) {
            PlatformConfig.setQQZone(QQ_APPID, QQ_SECRET);
        }
    }

    /**
     * 创建NotificationChannel
     *
     * @param applicationContext
     */
    private void initCloudChannel(final Context applicationContext) {
        this.createNotificationChannel();
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                String PUSH_TOKEN = PushServiceFactory.getCloudPushService().getDeviceId();
                ShareUitls.putString(BWNApplication.this, "PUSH_TOKEN", PUSH_TOKEN);
                if (!TextUtils.isEmpty(PUSH_TOKEN)) {
                    syncDeviceID(PUSH_TOKEN);
                }
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {

            }
        });
    }

    public void syncDeviceID(String PUSH_TOKEN) {
        try {
            if (TextUtils.isEmpty(PUSH_TOKEN)) {
                PUSH_TOKEN = ShareUitls.getString(BWNApplication.this, "PUSH_TOKEN", "");
            }
            MyToash.Log("PUSH_TOKEN", PUSH_TOKEN);
            if (!TextUtils.isEmpty(PUSH_TOKEN)) {
                final ReaderParams params = new ReaderParams(getBaseContext());
                params.putExtraParams("device_id", PUSH_TOKEN);
                String json = params.generateParamsJson();
                OkHttp3.getInstance(getBaseContext()).postAsyncHttp(Api.mSyncDeviceIdUrl, json, new ResultCallback() {
                    @Override
                    public void onFailure(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(final String result) {
                    }
                });
            }
        } catch (Throwable e) {
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = BuildConfig.APPLICATION_ID;
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * 多媒体工具
     */
    private void initStarrySky() {
        StarrySky.init(applicationContext, new StarrySkyConfig() {
            @Override
            public void applyOptions(@NonNull Context context, @NonNull StarrySkyBuilder builder) {
                super.applyOptions(context, builder);
                builder.setOpenCache(true);
                String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ssreader/cache/";
                builder.setCacheDestFileDir(destFileDir);
            }
        });
    }

    public String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
