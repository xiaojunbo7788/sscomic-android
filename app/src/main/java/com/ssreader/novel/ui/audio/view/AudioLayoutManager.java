package com.ssreader.novel.ui.audio.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 设置是否允许recyclerView接收事件
 * 用于进度条滑动时屏蔽事件
 * @author admin
 * @version 2020/02/10
 */
public class AudioLayoutManager extends LinearLayoutManager {

    // 是否允许接收事件
    private boolean isEnableSlide = true;

    public AudioLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        if (isEnableSlide) {
            return super.canScrollVertically();
        } else {
            return false;
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    /**
     * 设置状态
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
}
