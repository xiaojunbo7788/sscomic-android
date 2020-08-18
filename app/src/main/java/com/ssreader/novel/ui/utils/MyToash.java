package com.ssreader.novel.ui.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.dialog.ZToast;
import com.ssreader.novel.utils.LanguageUtil;

import static com.ssreader.novel.constant.Constant.SUE_LOG;

public class MyToash {

    public static void Toash(Context activity, String Message) {
        Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
    }

    public static void ToashSuccess(Activity activity, String Message) {
        if (activity != null && !activity.isFinishing() && Message != null) {
            ZToast.MakeText(activity, Message, 1200, R.mipmap.tips_success, null).show();
        }
    }

    public static void ToashSuccess(Activity activity, int Message) {
        if (activity != null && !activity.isFinishing() && Message != 0) {
            ZToast.MakeText(activity, LanguageUtil.getString(activity,Message), 1200, R.mipmap.tips_success, null).show();
        }
    }
    public static void ToashError(Activity activity, String Message) {
        if (activity != null && !activity.isFinishing() && Message != null) {
            ZToast.MakeText(activity, Message, 1200, R.mipmap.tips_error, null).show();
        }
    }
    public static void ToashError(Activity activity, int Message) {
        if (activity != null && !activity.isFinishing() && Message != 0) {
            ZToast.MakeText(activity, LanguageUtil.getString(activity,Message), 1200, R.mipmap.tips_error, null).show();
        }
    }

    /**
     * 可以监听吐司消失
     * @param activity
     * @param Message
     * @param listener
     */
    public static void ToastError(Activity activity, String Message, ToastDismissListener listener) {
        if (activity != null && !activity.isFinishing() && !TextUtils.isEmpty(Message)) {
            ZToast.MakeText(activity, Message, 1200, R.mipmap.tips_error, listener).show();
        } else {
            listener.onDismiss();
        }
    }

    public static void Log(Object Message) {
        if (SUE_LOG) {
            Log.i("bei_wo", Message.toString());
        }
    }

    public static void Log(String Message, String message) {
        if (SUE_LOG) {
            Log.i(Message, (message == null) ? "null" : message);
        }
    }

    public static void Log(String Message, int message) {
        if (SUE_LOG) {
            Log.i(Message, message + "");
        }
    }

    public static void setDelayedFinash(Activity activity) {
        setDelayedHandle(700, new DelayedHandle() {
            @Override
            public void handle() {
                activity.finish();
            }
        });
    }

    public static void setDelayedFinash(Activity activity, int string) {
        ToashSuccess(activity, LanguageUtil.getString(activity, string));
        setDelayedHandle(700, new DelayedHandle() {
            @Override
            public void handle() {
                activity.finish();
            }
        });
    }

    public interface DelayedHandle {
        void handle();
    }

    @SuppressLint("HandlerLeak")
    public static void setDelayedHandle(int time, DelayedHandle delayedHandle) {
        new Handler() {
        }.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayedHandle.handle();
            }
        }, time);
    }

    public interface ToastDismissListener{

        void onDismiss();
    }
}
