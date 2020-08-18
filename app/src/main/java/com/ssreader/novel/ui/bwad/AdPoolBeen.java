package com.ssreader.novel.ui.bwad;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.ssreader.novel.model.BaseAd;

public class AdPoolBeen {

    public View view;
    public Bitmap bitmap;
    public int position;
    public long chapter_id;
    public long book_id;
    public String addTime;
    public BaseAd baseAd;
    public TTNativeExpressAd mTTAd;

    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            return addTime.equals(((AdPoolBeen) obj).addTime);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return addTime.hashCode();
    }

    public AdPoolBeen(String addTime, BaseAd baseAd, TTNativeExpressAd mTTAd) {
        this.baseAd = baseAd;
        this.addTime = addTime;
        this.mTTAd = mTTAd;
    }

    public AdPoolBeen(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public AdPoolBeen(String addTime, Bitmap bitmap, BaseAd baseAd) {
        this.bitmap = bitmap;
        this.baseAd = baseAd;
        this.addTime = addTime;
    }
}
