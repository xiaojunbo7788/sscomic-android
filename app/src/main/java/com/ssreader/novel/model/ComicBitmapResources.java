package com.ssreader.novel.model;

import android.graphics.Bitmap;

/**
 * 用于存储漫画的bitmap
 */
public class ComicBitmapResources {

    public ComicBitmapResources(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
