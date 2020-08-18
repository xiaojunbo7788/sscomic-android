package com.ssreader.novel.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.RelativeLayout;

/**
 * 图片模糊处理
 * Created by  on 2018/6/12.
 */
public class BlurImageview {
    /**
     * 水平方向模糊度
     */
    private static float hRadius = 10;
    /**
     * 竖直方向模糊度
     */
    private static float vRadius = 10;
    /**
     * 模糊迭代度
     */
    private static int iterations = 10;

    /**
     * 图片高斯模糊处理
     */
    public static Drawable BlurImages2(Bitmap bmp, Context context) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];

        try {

            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);

            bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
            for (int i = 0; i < iterations; i++) {
                blur(inPixels, outPixels, width, height, hRadius);
                blur(outPixels, inPixels, height, width, vRadius);
            }
            blurFractional(inPixels, outPixels, width, height, hRadius);
            blurFractional(outPixels, inPixels, height, width, vRadius);
            bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            return drawable;
        } catch (Exception e) {
        } catch (Error r) {

            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ALPHA_8);
            bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
            for (int i = 0; i < iterations; i++) {
                blur(inPixels, outPixels, width, height, hRadius);
                blur(outPixels, inPixels, height, width, vRadius);
            }
            blurFractional(inPixels, outPixels, width, height, hRadius);
            blurFractional(outPixels, inPixels, height, width, vRadius);
            bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            return drawable;

        }
        return null;
    }


    public static void BlurImages(Bitmap image, Activity context, RelativeLayout e) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 计算图片缩小后的长宽
                int width = Math.round(image.getWidth() * 0.4f);
                int height = Math.round(image.getHeight() * 0.4f);

                // 将缩小后的图片做为预渲染的图片
                Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
                // 创建一张渲染后的输出图片
                Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

                // 创建RenderScript内核对象
                RenderScript rs = RenderScript.create(context);
                // 创建一个模糊效果的RenderScript的工具对象
                ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

                // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
                // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
                Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
                Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

                // 设置渲染的模糊程度, 25f是最大模糊度
                blurScript.setRadius(15);
                // 设置blurScript对象的输入内存
                blurScript.setInput(tmpIn);
                // 将输出数据保存到输出内存中
                blurScript.forEach(tmpOut);

                // 将数据填充到Allocation中
                tmpOut.copyTo(outputBitmap);

                Drawable drawable = new BitmapDrawable(context.getResources(), outputBitmap);
                try {
                    if (drawable != null && context != null) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                e.setBackground(drawable);
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }


    public static Bitmap BlurBitmapImages(Bitmap image, Context context) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * 0.4f);
        int height = Math.round(image.getHeight() * 0.4f);

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(25);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static Bitmap reloadCoverBg(Activity activity, Bitmap resource) {
        try {
            Bitmap bg = BlurImageview.BlurBitmapImages(resource, activity);
            if (bg != null) {
                Bitmap newbmp = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Bitmap.Config.ARGB_8888);
                Paint paint = new Paint();
                Canvas canvas = new Canvas(newbmp);
                canvas.drawBitmap(bg, 0, 0, paint);
                paint.setColor(0x66ffffff);
                canvas.drawRect(0, 0, bg.getWidth(), bg.getHeight(), paint);
                paint.setColor(0x66000000);
                canvas.drawRect(0, 0, bg.getWidth(), bg.getHeight(), paint);
                bg.recycle();
                bg = null;
               // Drawable drawable = new BitmapDrawable(activity.getResources(), newbmp);
                return newbmp;
            }
        } catch (Error e) {

            Bitmap bg = BlurImageview.BlurBitmapImages(resource, activity);
            if (bg != null) {
                Bitmap newbmp = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Bitmap.Config.ALPHA_8);
                Paint paint = new Paint();
                Canvas canvas = new Canvas(newbmp);
                canvas.drawBitmap(bg, 0, 0, paint);
                paint.setColor(0x66ffffff);
                canvas.drawRect(0, 0, bg.getWidth(), bg.getHeight(), paint);
                paint.setColor(0x66000000);
                canvas.drawRect(0, 0, bg.getWidth(), bg.getHeight(), paint);
                bg.recycle();
                bg = null;
               // Drawable drawable = new BitmapDrawable(activity.getResources(), newbmp);
                return newbmp;
            }

        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 图片高斯模糊算法
     */
    public static void blur(int[] in, int[] out, int width, int height,
                            float radius) {
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = 2 * r + 1;
        int divide[] = new int[256 * tableSize];

        for (int i = 0; i < 256 * tableSize; i++)
            divide[i] = i / tableSize;

        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = in[inIndex + clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
                        | (divide[tg] << 8) | divide[tb];

                int i = x + r + 1;
                if (i > widthMinus1)
                    i = widthMinus1;
                int i2 = x - r;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[inIndex + i];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    /**
     * 图片高斯模糊算法
     */
    public static void blurFractional(int[] in, int[] out, int width,
                                      int height, float radius) {
        radius -= (int) radius;
        float f = 1.0f / (1 + 2 * radius);
        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;

            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];

                int a1 = (rgb1 >> 24) & 0xff;
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;
                int a2 = (rgb2 >> 24) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;
                int a3 = (rgb3 >> 24) & 0xff;
                int r3 = (rgb3 >> 16) & 0xff;
                int g3 = (rgb3 >> 8) & 0xff;
                int b3 = rgb3 & 0xff;
                a1 = a2 + (int) ((a1 + a3) * radius);
                r1 = r2 + (int) ((r1 + r3) * radius);
                g1 = g2 + (int) ((g1 + g3) * radius);
                b1 = b2 + (int) ((b1 + b3) * radius);
                a1 *= f;
                r1 *= f;
                g1 *= f;
                b1 *= f;
                out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    public static int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }
}