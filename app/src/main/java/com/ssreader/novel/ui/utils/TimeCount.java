package com.ssreader.novel.ui.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.RegularUtlis;

/**
 * 用于验证码倒计时
 */
public class TimeCount {

    private static TimeCount timeCount;
    private Activity mActivity;
    private TextView messageText, PhoneTextView;
    public boolean IsRunIng;
    private Count count;


    public static TimeCount getInstance() {
        if (timeCount == null) {
            timeCount = new TimeCount();
        }
        return timeCount;
    }

    /**
     * @param activity
     * @param textView
     * @return
     */
    public TimeCount setActivity(Activity activity, TextView textView) {
        mActivity = activity;
        messageText = textView;
        return timeCount;
    }

    public void setActivity(TextView PhoneTextView) {
        this.PhoneTextView = PhoneTextView;
    }

    public TimeCount() {
        if (count != null) {
            count.cancel();
            count = null;
        }
    }

    /**
     * 开始计时
     *
     * @param millisInFuture
     */
    public void startCountTimer(long millisInFuture) {
        if (count == null) {
            count = new Count(millisInFuture, 1000);
        }
        IsRunIng = true;
        count.start();
        messageText.setEnabled(true);
        messageText.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_b0));
    }

    /**
     * 停止倒计时
     */
    public void stopCountTimer() {
        if (count != null) {
            count.cancel();
            count = null;
        }
    }

    public class Count extends CountDownTimer {

        public Count(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            IsRunIng = true;
            if (messageText != null && messageText.getVisibility() == View.VISIBLE &&
                    mActivity != null && !mActivity.isFinishing()) {
                messageText.setClickable(false);
                messageText.setText("(" + millisUntilFinished / 1000 + ") "
                        + LanguageUtil.getString(mActivity, R.string.LoginActivity_againsend));
            }
        }

        @Override
        public void onFinish() {
            if(RegularUtlis.isNumber(PhoneTextView.getText().toString())) {
                messageText.setEnabled(true);
                messageText.setTextColor(ContextCompat.getColor(mActivity, R.color.red));
            }
            IsRunIng = false;
            if (messageText != null && messageText.getVisibility() == View.VISIBLE &&
                    mActivity != null && !mActivity.isFinishing()) {
                messageText.setText(LanguageUtil.getString(mActivity, R.string.LoginActivity_againgetcode));
                messageText.setClickable(true);
            }
            cancel();
            count = null;
        }
    }
}
