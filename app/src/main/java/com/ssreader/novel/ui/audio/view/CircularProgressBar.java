package com.ssreader.novel.ui.audio.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;

/**
 * 圆形进度条
 */
public class CircularProgressBar extends View {

    private Paint backgroundPaint, progressPaint;
    // 绘制区域
    private RectF mRectF;
    // 进度
    private int progress = 0;
    private int maxProgress = 100;

    private OnProgressListener progressListener;

    public void setProgressListener(OnProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * 初始化
     * @param context
     */
    private void initView(Context context) {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setDither(true);
        backgroundPaint.setStrokeWidth(ImageUtil.dp2px(context, 2));
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.graybg));

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setStrokeWidth(ImageUtil.dp2px(context, 2));
        progressPaint.setColor(ContextCompat.getColor(context, R.color.maincolor));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWide = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHigh = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int mRectLength = (int) (Math.min(viewWide, viewWide) -
                (Math.min(backgroundPaint.getStrokeWidth(), progressPaint.getStrokeWidth())) * 2);
        int mRectL = getPaddingLeft() + (viewWide - mRectLength) / 2;
        int mRectT = getPaddingTop() + (viewHigh - mRectLength) / 2;
        mRectF = new RectF(mRectL, mRectT, mRectL + mRectLength, mRectT + mRectLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectF, 0, 360, false, backgroundPaint);
        canvas.drawArc(mRectF, 275, 360 * progress / maxProgress, false, progressPaint);
        if (progress == maxProgress && progressListener != null) {
            progressListener.onEnd();
        }
    }

    /**********************************************************************************************/

    /**
     * 设置最大值
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * 获取当前进度
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 设置当前进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public interface OnProgressListener{

        void onEnd();
    }
}
