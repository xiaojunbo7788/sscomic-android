package com.ssreader.novel.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssreader.novel.R;
import com.ssreader.novel.utils.DateUtils;

public class CountDownView extends LinearLayout {

    private TextView tv_hour, tv_minute, tv_second;
    private OnCountDownListener countDownListener;

    public void setCountDownListener(OnCountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            initView(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(Context context) throws Exception{
        View.inflate(context, R.layout.count_down, this);
        tv_hour = (TextView)findViewById(R.id.count_down_hour);
        tv_minute = (TextView) findViewById(R.id.count_down_minute);
        tv_second = (TextView) findViewById(R.id.count_down_second);
    }

    @SuppressLint("HandlerLeak")
    Handler countDownTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (Time==0) {
                countDownListener.onEnd();
            }else {
                --Time;
                String formatStr = DateUtils.secToTime( (Time));
                String[] date = formatStr.split(":");
                tv_hour.setText(date[0] + "");
                tv_minute.setText(date[1] + "");
                tv_second.setText(date[2] + "");
                countDownTimer.sendEmptyMessageDelayed(0,1000);
            }
        }
    };
    int Time;

    public void start(int time) {
        this.Time = time;
        cancel();
        countDownTimer.sendEmptyMessage(0);
    }

    public void cancel() {
        if (countDownTimer != null) {
            countDownTimer.removeMessages(0);
        }
    }

    public interface OnCountDownListener{

        void onEnd();
    }
}
