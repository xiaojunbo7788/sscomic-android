package com.ssreader.novel.ui.audio.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.Locale;

/**
 * 听书界面进度条(仿喜马拉雅)
 * @author admin
 * @version 2020/02/08
 */
public class TimeSeekBar extends View {

    // seekBar默认宽度
    private int seekBarDefaultWidth;
    // 最大值
    private int maxProgress = 0;
    // 是否开始滑动
    private boolean mDrag;
    // 是否允许滑动
    private boolean isEnableSlide = false;
    // 实时文字
    private String seekBarTips;

    // 文字背景
    private int textBgHeight, textBgWidth;
    // 文字颜色
    private int textColor;
    // 文字背景颜色
    private int textBgColor;
    // 文字大小
    private int textSize;
    // 文本高度
    private int textHeight;

    // 进度条高度
    private int progressBarHeight;
    // 进度条颜色
    private int progressBarColor;
    // 进度条
    private int progress;

    // 缓存高度
    private int cacheProgressBarHeight;
    // 缓存颜色
    private int cacheProgressBarColor;
    // 缓存进度值
    private int cacheProgress;

    private Paint cacheProgressBarPaint = new Paint();
    private Paint progressBarPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint textBgPaint = new Paint();

    // 监听事件
    private SeekBarProgressListener progressListener;

    public void setProgressListener(SeekBarProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public TimeSeekBar(Context context) {
        this(context, null);
    }

    public TimeSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void initView(Context context) {
        seekBarDefaultWidth = ScreenSizeUtils.getInstance(context).getScreenWidth();
        cacheProgressBarHeight = ImageUtil.dp2px(context, 3f);
        progressBarHeight = ImageUtil.dp2px(context, 2);
        textSize = ImageUtil.dp2px(context,10);

        progressBarColor = ContextCompat.getColor(context, R.color.grayline);
        cacheProgressBarColor = ContextCompat.getColor(context, R.color.maincolor);
        textColor = ContextCompat.getColor(context, R.color.black);
        textBgColor = ContextCompat.getColor(context, R.color.white);

        progressBarPaint.setColor(progressBarColor);
        progressBarPaint.setAntiAlias(false);
        progressBarPaint.setStyle(Paint.Style.FILL);
        // 头尾圆角
        progressBarPaint.setStrokeCap(Paint.Cap.ROUND);

        cacheProgressBarPaint.setColor(cacheProgressBarColor);
        cacheProgressBarPaint.setAntiAlias(false);
        cacheProgressBarPaint.setStyle(Paint.Style.FILL);

        textBgPaint.setColor(textBgColor);
        textBgPaint.setAntiAlias(true);
        textBgPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(textSize);
        textHeight = getTextHeight(textPaint, "00:00/00:00");
        textBgHeight = textHeight + ImageUtil.dp2px(context, 8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 测量宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {
            // 大小确定
            width = widthSize;
        } else {
            width = seekBarDefaultWidth;
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //当wrap_content的时候 高度为textBgHeight
            // 或者是所有部分的最高那个高度
            height = textBgHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        float progressBarRealWidth = width;
        // 画背景条
        canvas.drawRect(0, (height >> 1) - (progressBarHeight >> 1)
                , width, (height >> 1) + (progressBarHeight >> 1)
                , progressBarPaint);
        // 判断界限
        if (progress < 0) {
            progress = 0;
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        if (cacheProgress < 0) {
            cacheProgress = 0;
        }
        if (cacheProgress > maxProgress) {
            cacheProgress = maxProgress;
        }
        // 画缓存条
        float currentCache = (float) cacheProgress * progressBarRealWidth / maxProgress;
        canvas.drawRect(0, (height >> 1) - (cacheProgressBarHeight >> 1)
                , currentCache, (height >> 1) + (cacheProgressBarHeight >> 1)
                , cacheProgressBarPaint);
        // 计算文字，文字长度可能会变 会不断的测量
        String progressText = seekBarTips = getProgressText();
        // 获取文字宽度
        int textWidth = getTextWidth(textPaint, progressText);
        // 画文字背景 背景宽高随文字变化而变化
        textBgWidth = textWidth + ImageUtil.dp2px(getContext(), 15);
        int shadowHeight = ImageUtil.dp2px(getContext(), 1);
        // 阴影
        textBgPaint.setShadowLayer(3, 0, 0,
                ContextCompat.getColor(getContext(), R.color.black_3f));
        // 确定位置
        float textBgStartX = 0.0f;
        if (maxProgress == 0) {
            textBgStartX = (float) progress * (progressBarRealWidth) / 1 - (float) textBgWidth / 1 * progress;
        } else {
            textBgStartX = (float) progress * (progressBarRealWidth) / maxProgress - (float) textBgWidth / maxProgress * progress;
        }
        float textBgEndX = textBgStartX + textBgWidth;
        // 画背景
        canvas.drawRoundRect(textBgStartX + shadowHeight, shadowHeight
                , textBgEndX - (shadowHeight >> 1), height - shadowHeight
                , (height + shadowHeight) >> 1 //x半径
                , (height - shadowHeight) >> 1 //y半径
                , textBgPaint);
        // 画文字，计算起点
        float textStartY = (height >> 1) + (textHeight >> 1);
        float textStartX = textBgStartX + ((textBgWidth >> 1) - (textWidth >> 1));
        canvas.drawText(progressText, textStartX, textStartY, textPaint);
    }

    /**
     * 获取文字宽度
     * @param paint
     * @param str
     * @return
     */
    private int getTextWidth(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }

    /**
     * 获取文字高度
     * @param paint
     * @param str
     * @return
     */
    private int getTextHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    /**
     * 设置文字
     * @return
     */
    private String getProgressText() {
        String progressText = formatProgress(this.progress);
        String maxProgressText = formatProgress(this.maxProgress);
        return progressText + "/" + maxProgressText;
    }

    /**
     * @param seconds 进度(秒)
     * @return
     */
    private String formatProgress(int seconds) {
        String standardTime;
        if (seconds <= 0) {
            standardTime = "00:00";
        } else if (seconds < 60) {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60);
        } else if (seconds < 3600) {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        } else {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60);
        }
        return standardTime;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnableSlide) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                mDrag = true;
                if (progressListener != null) {
                    progressListener.onStartSlide(progress, seekBarTips, event.getX());
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                touchUpdate(event.getX());
                if (progressListener != null) {
                    progressListener.onProgressChanged(progress, seekBarTips, event.getX());
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mDrag = false;
                touchUpdate(event.getX());
                if (progressListener != null) {
                    progressListener.onStopSlide(progress);
                }
            } else {
                mDrag = false;
                touchUpdate(event.getX());
                if (progressListener != null) {
                    progressListener.onStopSlide(progress);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void touchUpdate(float x) {
        progress = (int) (x * maxProgress / getWidth());
        cacheProgress = progress;
        postInvalidate();
    }

    /*************************************************************************************************/

    /**
     * 设置是否可以滑动
     * @param isEnableSlide
     */
    public void setEnableSlide(boolean isEnableSlide) {
        this.isEnableSlide = isEnableSlide;
    }

    /**
     * 获取状态
     * @return
     */
    public boolean isEnableSlide() {
        return isEnableSlide;
    }

    /**
     * 设置最大值
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        postInvalidate();
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    /**
     * 设置当前进度
     * @param progress 进度(秒)
     */
    public void setProgress(int progress) {
        if (mDrag) {
            return;
        }
        if (maxProgress != 0 && progress > maxProgress) {
            this.progress = maxProgress;
            this.cacheProgress = progress;
            postInvalidate();
            return;
        }
        this.progress = progress;
        this.cacheProgress = progress;
        postInvalidate();
    }

    public int getProgress() {
        return this.progress;
    }

    /**
     * 清除
     */
    public void onCancel() {
        seekBarTips = "";
        isEnableSlide = false;
    }

    /**
     * 事件监听
     */
    public interface SeekBarProgressListener {

        void onStartSlide(int progress, String tipsText, float width);

        void onProgressChanged(int progress, String tipsText, float width);

        void onStopSlide(int progress);
    }
}
