package com.ssreader.novel.ui.bwad;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.ssreader.novel.ui.utils.MyToash;

/**
 * bitmap工具类
 */
public class ViewToBitmapUtil {

    public static Bitmap convertViewToBitmap(View v, int y, int AD_HEIGHT) {
        try {
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), y, v.getRight(), y + AD_HEIGHT);
            Drawable bgDrawable = v.getBackground();
            if (bgDrawable != null) {
                bgDrawable.draw(c);
            } else {
                c.drawColor(Color.WHITE);
            }
            v.draw(c);
            return b;
        } catch (Throwable e) {
        }
        return null;
    }


    public static Bitmap convertViewToBitmap(View v) {
        try {
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            Drawable bgDrawable = v.getBackground();
            if (bgDrawable != null) {
                bgDrawable.draw(c);
            } else {
                c.drawColor(Color.WHITE);
            }
            v.draw(c);
            return b;
        } catch (Throwable e) {
        }
        return null;
    }


    /**
     * 获取layout的布局
     * @param view
     * @param bgColor
     * @return
     */
    public static Bitmap getLayoutBitmap(View view, int bgColor) {
        int w = view.getWidth();
        int h = view.getHeight();
        if (w <= 0 || h <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(bgColor);
        view.layout(0, 0, w, h);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap getLayoutBitmap(View view) {
        int w = view.getWidth();
        int h = view.getHeight();
        if (w <= 0 || h <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        view.layout(0, 0, w, h);
        view.draw(canvas);
        return bitmap;
    }
}
