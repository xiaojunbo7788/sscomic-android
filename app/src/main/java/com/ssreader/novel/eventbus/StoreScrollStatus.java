package com.ssreader.novel.eventbus;

import android.widget.TextView;

/**
 * 用于传递书城滑动
 */
public class StoreScrollStatus {

    public int productType;
    public int scrollY;
    public boolean status, isChangeBg;
    public TextView textView;

    public StoreScrollStatus(int productType, boolean isChange, int scrollY) {
        this.productType = productType;
        this.isChangeBg = isChange;
        this.scrollY = scrollY;
    }

    public StoreScrollStatus(int productType, boolean status, TextView textView) {
        this.productType = productType;
        this.status = status;
        this.textView = textView;
    }
}
