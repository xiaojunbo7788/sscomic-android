package com.ssreader.novel.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ssreader.novel.R;
import com.ssreader.novel.model.Announce;

import java.util.List;

public class MarqueeTextView extends LinearLayout {

    private Context mContext;
    private ViewFlipper viewFlipper;
    private View marqueeTextView;
    private List<Announce> textArrays;
    private MarqueeTextViewClickListener marqueeTextViewClickListener;
    int mWidth;
    int mHeight;
    private Handler mHandler;

    public MarqueeTextView(Context context) {
        super(context);
        mContext = context;
        initBasicView();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBasicView();
    }

    public void setTextArraysAndClickListener(List<Announce> textArrays, MarqueeTextViewClickListener marqueeTextViewClickListener) {
        this.textArrays = textArrays;
        this.marqueeTextViewClickListener = marqueeTextViewClickListener;
        initMarqueeTextView(textArrays, marqueeTextViewClickListener);
    }

    public void initBasicView() {
        mHandler = new Handler(Looper.getMainLooper());
        marqueeTextView = LayoutInflater.from(mContext).inflate(R.layout.marquee_textview_layout, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(marqueeTextView, layoutParams);
        viewFlipper = marqueeTextView.findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
        viewFlipper.startFlipping();
        viewFlipper.setFlipInterval(5000);
    }

    public void initMarqueeTextView(List<Announce> textArrays, final MarqueeTextViewClickListener marqueeTextViewClickListener) {
        if (textArrays.size() == 0) {
            return;
        }
        viewFlipper.removeAllViews();
        checkInit();
    }

    /**
     * 检测控件是否初始化完毕
     */
    public void checkInit() {

        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                if (mHeight > 0) {
                    int i = 0;
                    while (i < textArrays.size()) {
                        final int j = i;
                        TextView textView = new TextView(mContext);
                        textView.setLines(1);
                        textView.setTextColor(Color.BLACK);
                        textView.setText(textArrays.get(i).getTitle());
                        textView.setTextSize(12);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                marqueeTextViewClickListener.onClick(v, j);
                            }
                        });
                        ViewFlipper.LayoutParams lp = new ViewFlipper.LayoutParams(ViewFlipper.LayoutParams.MATCH_PARENT, mHeight);
                        viewFlipper.addView(textView, lp);
                        i++;
                    }
                    mHandler.removeCallbacks(this);
                } else {
                    mHandler.postDelayed(this, 5);
                }
            }
        };
        // 开始检测
        mHandler.post(checkRunnable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void releaseResources() {
        if (marqueeTextView != null) {
            if (viewFlipper != null) {
                viewFlipper.stopFlipping();
                viewFlipper.removeAllViews();
                viewFlipper = null;
            }
            marqueeTextView = null;
        }
    }
}