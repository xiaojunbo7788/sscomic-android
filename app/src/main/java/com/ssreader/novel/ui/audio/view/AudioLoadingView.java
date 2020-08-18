package com.ssreader.novel.ui.audio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.ssreader.novel.R;

/**
 * 用于显示加载动画
 */
public class AudioLoadingView extends AppCompatImageView {

    // 旋转角度
    private int rotateDegree = 0;
    // 是否继续旋转
    private boolean needRotate = false;

    private int type;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rotateDegree += 15;
            rotateDegree = rotateDegree <= 360 ? rotateDegree : 0;
            invalidate();
            // 是否继续旋转
            if (needRotate) {
                handler.postDelayed(runnable, 60);
            }
        }
    };

    public AudioLoadingView(Context context) {
        this(context, null);
    }

    public AudioLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 开启硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AudioLoadingView);
        type = array.getInt(R.styleable.AudioLoadingView_type, 0);
        array.recycle();
        // 设置图标
        if (type == 1) {
            setImageResource(R.mipmap.audio_load_white);
        } else {
            setImageResource(R.mipmap.audio_load_black);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 绑定到window的时候
        super.onAttachedToWindow();
        needRotate = true;
        // 绑定到window的时候
        handler.postDelayed(runnable, 0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 从window中解绑
        needRotate = false;
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 第一个参数是旋转的角度
         * 第二个参数是旋转的x坐标
         * 第三个参数是旋转的y坐标
         */
        canvas.rotate(rotateDegree, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
