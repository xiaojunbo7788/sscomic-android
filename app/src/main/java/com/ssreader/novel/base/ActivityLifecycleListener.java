package com.ssreader.novel.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.activity.SplashActivity;
import com.ssreader.novel.utils.ShareUitls;

import static com.ssreader.novel.constant.Constant.getUSE_AD_HOME;

/**
 * 软件界面状态监听
 */
public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

    private long lastTime = 0;
    private long startTime = 0;
    private int appCount = 0;
    private final int minute = 15;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        //BWNApplication.applicationContext.setActivity(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        appCount++;
        startTime = (System.currentTimeMillis() / 1000);
        if (lastTime != 0) {
            if (startTime - lastTime > 60 * minute && !(activity instanceof SplashActivity)) {
                if (getUSE_AD_HOME(activity)) {
                    Intent intent = new Intent();
                    intent.setClass(activity, SplashActivity.class);
                    intent.putExtra("HOME_AD",true);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_alpha_open, R.anim.activity_stay);
                }
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ShareUitls.putLong(activity, "Home_AD_lastTime", System.currentTimeMillis());
        appCount--;
        if(appCount == 0) {
            lastTime = (System.currentTimeMillis() / 1000);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
