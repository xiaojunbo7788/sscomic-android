package com.ssreader.novel.ui.read.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ssreader.novel.base.BWNApplication;

import java.lang.reflect.Method;

/**
 * Created by -- on 17-5-1.
 */

public class ScreenUtils {

    public static int dpToPx(int dp){
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,metrics);
    }


    public static int spToPx(int sp){
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,metrics);
    }

    public static int pxToSp(int px){
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) (px / metrics.scaledDensity);
    }

    public static DisplayMetrics getDisplayMetrics(){
        DisplayMetrics metrics = BWNApplication.applicationContext
                .getResources()
                .getDisplayMetrics();
        return metrics;
    }
}
