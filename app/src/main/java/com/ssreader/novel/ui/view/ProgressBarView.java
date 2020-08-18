package com.ssreader.novel.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.ssreader.novel.R;

public class ProgressBarView extends ProgressBar {

    //字体大小
    protected int mTextSize = 8;
    //字体颜色
    protected int mTextColor = Color.BLACK;
    //没有到达(右边progressbar的颜色)
    protected int mUnReachColor = Color.GREEN;
    //progressbar的高度
    protected int mProgressHeight = 6;
    //progressbar进度的颜色
    protected int mReachColor = mTextColor;
    //字体间距
    protected int mTextOffset = 9;
    protected Paint mPaint;
    //progressbar真正的宽度
    protected int mRealHeight;
    protected int mRealWith;
    // 半径
    protected int radius = dp2px(5);
    Context context;

    public ProgressBarView(Context context) {
        this(context, null);
    }

    public ProgressBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        obtainStyledAttrs(attrs);

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs) {
        mTextSize = dp2px(11);
        mTextColor = Color.BLACK;
        mUnReachColor = Color.parseColor("#6B6B6B");
        mProgressHeight = dp2px(22);
        mReachColor = Color.parseColor("#376ee5");
        // mTextOffset = dp2px(15)
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRealHeight = getHeight();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthVal, height);
        //计算progressbar真正宽度=控件的宽度-paddingleft-paddingright
        mRealWith = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        Path path = new Path();
        //保存画布
        canvas.save();
        // 抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //移动画布
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        //定义变量用来控制是否要绘制右边progressbar  如果宽度不够的时候就不进行绘制
        boolean noNeedUnRech = false;
        //计算左边进度在整个控件宽度的占比
        float radio = getProgress() * 1.0f / getMax();
        //获取左边进度的宽度
        float progressX = radio * mRealWith;
        //中间文字
        String text = getProgress() + "%";
        //获取文字的宽度
        int textWidth = (int) mPaint.measureText(text);
        // 圆角
//        canvas.clipPath(getPath(path, textWidth));
        if (progressX + textWidth > mRealWith) {
            //左边进度+文字的宽度超过progressbar的宽度 重新计算左边进度的宽度 这个时候也就意味着不需要绘制右边进度
            progressX = mRealWith - textWidth;
            noNeedUnRech = true;
        }
        //计算左边进度结束的位置 如果结束的位置小于0就不需要绘制左边的进度
        float endX = progressX - mTextOffset / 2;
        if (endX > 0) {
            //绘制左边进度
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mProgressHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }
        mPaint.setColor(mTextColor);
        if (getProgress() != 0) {
            //计算文字基线
            int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
            //绘制文字
            canvas.drawText(text, progressX, y, mPaint);
        }
        if (!noNeedUnRech) {
            //右边进度的开始位置=左边进度+文字间距的一半+文字宽度
            float start;
            if (getProgress() == 0) {
                start = progressX;
            } else {
                start = progressX + mTextOffset / 2 + textWidth;
            }
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mProgressHeight);
            //绘制右边进度
            canvas.drawLine(start, 0, mRealWith, 0, mPaint);
        }
        //重置画布
        canvas.restore();
    }

    private Path getPath(Path path, int textWidth) {
        // 右上
        path.moveTo(radius, 0);
        path.lineTo(mRealWith - radius, 0);
        path.quadTo(mRealWith, 0, mRealWith, radius);
        // 右下
        path.lineTo(mRealWith, mRealHeight - radius);
        path.quadTo(mRealWith, mRealHeight, mRealWith - radius, mRealHeight);
        // 左下
        path.lineTo(radius, mRealHeight);
        path.quadTo(0, mRealHeight, 0, mRealHeight - radius);
        // 左上
        path.lineTo(0, radius);
        path.quadTo(0, 0, radius, 0);
        return path;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        //获取高度模式
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽度模式
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            //精准模式 用户设置为 比如80dp  match_parent fill_parent
            result = size;
        } else {
            //计算中间文字的高度
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            //paddingTop+paddingBottom+ progressbar高度和文字高度的最大值
            result = getPaddingTop() + getPaddingBottom() + Math.max(mProgressHeight, Math.abs(textHeight));
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    protected int dp2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    protected int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}