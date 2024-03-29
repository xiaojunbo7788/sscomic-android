package com.ssreader.novel.ui.view;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


import android.content.Context;
import android.graphics.PointF;

import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager {
    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();

    float oldx = 0.0f;
    float first = 0.0f;
    OnSingleTouchListener onSingleTouchListener;

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ChildViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //当拦截触摸事件到达此位置的时候，返回true，
        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //每次进行onTouch事件都记录当前的按下的坐标
        curP.x = arg0.getX();
        curP.y = arg0.getY();

        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            oldx = downP.x;
            first = 0.0f;
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
            float newx = arg0.getX();
            int curpostion = getCurrentItem();
            int count = this.getAdapter().getCount() - 1;

            //预测本次滑动的的方向（first>0 说明本次手势向右滑动）
            if (first == 0.0f) {  //第一次进来
                if (Math.abs(newx - oldx) > 0) {
                    first = newx - oldx;
                }
            }

            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            //当子viewpager的position处于0时,检测如果是向右滑动说明要通知父ViewPager滑动;
            //当子viewpager的position处于size-1时,检测如果是向左滑动说明要通知父ViewPager滑动;
            //(newx-oldx)实时检测滑动方向
            if ((curpostion == count && (newx - oldx) < 0 && first < 0) || (curpostion == 0 && (newx - oldx) > 0 && first > 0)) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }

            oldx = newx;
        }

        if (arg0.getAction() == MotionEvent.ACTION_UP) {
            //在up时判断是否按下和松手的坐标为一个点
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            if (downP.x == curP.x && downP.y == curP.y) {
                onSingleTouch();
                return true;
            }
        }

        return super.onTouchEvent(arg0);
    }

    /**
     * 单击
     */
    public void onSingleTouch() {
        if (onSingleTouchListener != null) {

            onSingleTouchListener.onSingleTouch();
        }
    }

    /**
     * 创建点击事件接口
     *
     * @author wanpg
     */
    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

}
