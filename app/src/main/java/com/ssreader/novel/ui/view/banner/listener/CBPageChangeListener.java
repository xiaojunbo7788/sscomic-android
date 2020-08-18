package com.ssreader.novel.ui.view.banner.listener;

import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

/**
 * 翻页监听
 */
public class CBPageChangeListener implements ViewPager.OnPageChangeListener {

    private ArrayList<ImageView> pointViews;
    private int[] page_indicatorId;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public CBPageChangeListener(ArrayList<ImageView> pointViews, int page_indicatorId[]) {
        this.pointViews = pointViews;
        this.page_indicatorId = page_indicatorId;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) onPageChangeListener.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null)
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int index) {
        for (int i = 0; i < pointViews.size(); i++) {
            if (index != i) {
                pointViews.get(i).setBackgroundResource(page_indicatorId[0]);
            } else {
                pointViews.get(index).setBackgroundResource(page_indicatorId[1]);
            }
        }
        if (onPageChangeListener != null) onPageChangeListener.onPageSelected(index);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
