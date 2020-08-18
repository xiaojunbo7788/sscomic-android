package com.ssreader.novel.ui.view.seekBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

/**
 * 自定义拖动seekBar
 */
public class DragSeekBar extends View {

    // seekBar默认宽度
    private int seekBarDefaultWidth;
    // 最大值
    private int maxProgress = 0;
    // 是否开始滑动
    private boolean mDrag;
    // 是否允许滑动
    private boolean isEnableSlide = true;

    // 按钮高度
    private int btnHeight;
    // 文字颜色
    private int btnColor;
    // 文字背景颜色
    private int btnBgColor;

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
    private Paint btnPaint = new Paint();

    private DragProgressListener dragProgressListener;

    public void setDragProgressListener(DragProgressListener dragProgressListener) {
        this.dragProgressListener = dragProgressListener;
    }

    public DragSeekBar(Context context) {
        this(context, null);
    }

    public DragSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        seekBarDefaultWidth = ScreenSizeUtils.getInstance(context).getScreenWidth();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragSeekBar);
        maxProgress = array.getInt(R.styleable.DragSeekBar_max_progress, 0);
        // 按钮
        btnHeight = array.getDimensionPixelOffset(R.styleable.DragSeekBar_btn_height, 20);
        btnColor = array.getColor(R.styleable.DragSeekBar_btn_color, 0);
        btnBgColor = array.getColor(R.styleable.DragSeekBar_btn_bg_color, 0);
        // 缓存
        cacheProgressBarHeight = array.getDimensionPixelOffset(R.styleable.DragSeekBar_cache_height, 0);
        cacheProgressBarColor = array.getColor(R.styleable.DragSeekBar_cache_color, 0);
        // 进度条
        progressBarHeight = array.getDimensionPixelOffset(R.styleable.DragSeekBar_progress_height, 0);
        progressBarColor = array.getColor(R.styleable.DragSeekBar_progress_color, 0);
        array.recycle();
        // 画笔
        progressBarPaint.setColor(progressBarColor);
        progressBarPaint.setAntiAlias(false);
        progressBarPaint.setStyle(Paint.Style.FILL);
        // 头尾圆角
        progressBarPaint.setStrokeCap(Paint.Cap.ROUND);

        cacheProgressBarPaint.setColor(cacheProgressBarColor);
        cacheProgressBarPaint.setAntiAlias(false);
        cacheProgressBarPaint.setStyle(Paint.Style.FILL);
        cacheProgressBarPaint.setStrokeCap(Paint.Cap.ROUND);

        btnPaint.setColor(btnColor);
        btnPaint.setAntiAlias(true);
        btnPaint.setStyle(Paint.Style.FILL);
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
            height = btnHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = btnHeight;
        int width = getWidth();
        float progressBarRealWidth = width;
        int shadowHeight = ImageUtil.dp2px(getContext(), 1);
        // 画背景条
        canvas.drawRect(shadowHeight, (height >> 1) - (progressBarHeight >> 1)
                , width - shadowHeight, (height >> 1) + (progressBarHeight >> 1)
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
        canvas.drawRect(shadowHeight, (height >> 1) - (cacheProgressBarHeight >> 1)
                , currentCache - shadowHeight, (height >> 1) + (cacheProgressBarHeight >> 1)
                , cacheProgressBarPaint);
        // 阴影
        btnPaint.setShadowLayer(2, 0, 0, btnBgColor);
        // 确定位置
        float btnBgStartX = 0.0f;
        if (maxProgress == 0) {
            btnBgStartX = (float) progress * progressBarRealWidth / 1 - (float) height / 1 * progress;
        } else {
            btnBgStartX = (float) progress * progressBarRealWidth / maxProgress - (float) height / maxProgress * progress;
        }
        // 画背景
        canvas.drawCircle(btnBgStartX + (height >> 1)
                , height >> 1
                , (height - shadowHeight * 2) >> 1
                , btnPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnableSlide) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                mDrag = true;
                if (dragProgressListener != null) {
                    dragProgressListener.onStart();
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                touchUpdate(event.getX());
                if (dragProgressListener != null) {
                    dragProgressListener.onProgressChanged(progress);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mDrag = false;
                touchUpdate(event.getX());
                if (dragProgressListener != null) {
                    dragProgressListener.onStop();
                }
            } else {
                mDrag = false;
                touchUpdate(event.getX());
                if (dragProgressListener != null) {
                    dragProgressListener.onStop();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param x
     */
    private void touchUpdate(float x) {
        progress = (int) (x * maxProgress / getWidth());
        cacheProgress = progress;
        postInvalidate();
    }

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
     * 事件监听
     */
    public interface DragProgressListener {

        void onStart();

        void onProgressChanged(int progress);

        void onStop();
    }
}
