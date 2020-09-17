package com.ssreader.novel.ui.utils;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

/**
 * 主色调工具类
 */
public class PaletteHelper {
    public interface PaletteHelperColor {
        void getColor(int color);
    }

    /**
     * 设置图片主色调
     *
     * @param bitmap
     * @return
     */
    public static void setPaletteColor(Bitmap bitmap, PaletteHelperColor paletteHelperColor) {
        if (bitmap == null) {
            return;
        }
        Palette.from(bitmap).maximumColorCount(10).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
//                List<Palette.Swatch> list = palette.getSwatches();
//                int colorSize = 0;
//                Palette.Swatch maxSwatch = null;
//                for (int i = 0; i < list.size(); i++) {
//                    Palette.Swatch swatch = list.get(i);
//                    if (swatch != null) {
//                        int population = swatch.getPopulation();
//                        if (colorSize < population) {
//                            colorSize = population;
//                            maxSwatch = swatch;
//                        }
//                    }
//                }
                Palette.Swatch s;
                s = palette.getDarkVibrantSwatch();    //获取充满活力的黑
                if (s == null) {
                    s = palette.getDarkMutedSwatch();      //获取柔和的黑
                    MyToash.Log("palette","1");
                }
                if (s == null) {
                    s = palette.getDominantSwatch();//独特的一种
                    MyToash.Log("palette","2");
                }
                if (s == null) {
                    s = palette.getVibrantSwatch();       //获取到充满活力的这种色调
                    MyToash.Log("palette","3");
                }
                if (s == null) {
                    s = palette.getLightVibrantSwatch();   //获取充满活力的亮
                    MyToash.Log("palette","4");
                }
                if (s == null) {
                    s = palette.getMutedSwatch();           //获取柔和的色调
                    MyToash.Log("palette","5");
                }
                if (s == null) {
                    s = palette.getLightMutedSwatch();    //获取柔和的亮
                    MyToash.Log("palette","6");
                }
                if (paletteHelperColor != null && s != null) {
                    paletteHelperColor.getColor(s.getRgb());
                }

            }
        });

    }

    /**
     * 对rgb色彩加入透明度
     *
     * @param alpha     透明度，取值范围 0.0f -- 1.0f.
     * @param baseColor
     * @return a color with alpha made from base color
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
