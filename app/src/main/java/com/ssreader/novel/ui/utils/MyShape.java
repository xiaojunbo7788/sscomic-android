package com.ssreader.novel.ui.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;

public class MyShape {

    public static GradientDrawable setMyshape(int radius, int bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setColor(bg);
        return drawable;
    }

    /**
     * 设置圆角、背景、透明度
     *
     * @param radius
     * @param bg
     * @param alpha
     * @return
     */
    public static GradientDrawable setMyShapeWithAlpha(int radius, int bg, int alpha) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setColor(bg);
        drawable.setAlpha(alpha);
        return drawable;
    }

    public static GradientDrawable setMyshapeOVAL(int bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        if (bg != 0) {
            drawable.setColor(bg);
        }
        return drawable;
    }

    public static GradientDrawable setMyshapeOVAL() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        return drawable;
    }

    public static GradientDrawable setMyshapeOVAL(Context context) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(ContextCompat.getColor(context, R.color.maincolor));
        return drawable;
    }

    public static GradientDrawable setMyshape(int radius, String bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setColor(Color.parseColor(bg));
        return drawable;
    }

    public static GradientDrawable setMyshape(int topleft, int topright, int buttomleft, int buttomright, String bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{topleft, topleft, topright, topright, buttomleft, buttomleft, buttomright, buttomright});
        drawable.setColor(Color.parseColor(bg));
        return drawable;
    }

    public static GradientDrawable setMyshape(int topleft, int topright, int buttomleft, int buttomright, int bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{topleft, topleft, topright, topright, buttomleft, buttomleft, buttomright, buttomright});
        drawable.setColor(bg);
        return drawable;
    }

    public static GradientDrawable setMyshape(Activity activity, int topleft, int topright, int buttomleft, int buttomright, int bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{topleft, topleft, topright, topright, buttomleft, buttomleft, buttomright, buttomright});
        drawable.setColor(ContextCompat.getColor(activity, bg));
        return drawable;
    }

    public static GradientDrawable setMyshapeComicBg(Activity activity) {
        int s = ImageUtil.dp2px(activity, 18);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{s, s, s, s, 0, 0, 0, 0});
        drawable.setColor(Color.WHITE);
        return drawable;
    }

    /**
     * 绘制边框
     *
     * @param activity 上下文
     * @param radius   圆角半径
     * @param width    线宽
     * @param bg       填充颜色
     * @return
     */
    public static GradientDrawable setMyshapeStroke(Context activity, int radius, int width, int bg) {
        GradientDrawable drawable = new GradientDrawable();
        if (radius != 0) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
        }
        drawable.setStroke(ImageUtil.dp2px(activity, width), bg);
        drawable.setColor(Color.WHITE);
        return drawable;
    }

    public static GradientDrawable setMyshape(Context activity, int topleft, int topright, int buttomleft, int buttomright, int width, int strokeBg, int bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{topleft, topleft, topright, topright,
                buttomleft, buttomleft, buttomright, buttomright});
        drawable.setColor(bg);
        drawable.setStroke(ImageUtil.dp2px(activity, width), strokeBg);
        return drawable;
    }

    /**
     * 设置带边框的椭圆，并可以自定义内部颜色
     *
     * @param activity
     * @param radius
     * @param width
     * @param strokeBg
     * @param bg
     * @return
     */
    public static GradientDrawable setMyshapeStroke(Context activity, int radius, int width, int strokeBg, int bg) {
        GradientDrawable drawable = new GradientDrawable();
        if (radius != 0) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
        }
        drawable.setStroke(ImageUtil.dp2px(activity, width), strokeBg);
        drawable.setColor(bg);
        return drawable;
    }

    public static GradientDrawable setMyshapeStroke(Context activity, int topleft, int topright, int buttomleft, int buttomright, int width, int broad_bg, int bg) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{ImageUtil.dp2px(activity, topleft), ImageUtil.dp2px(activity, topleft),
                ImageUtil.dp2px(activity, topright), ImageUtil.dp2px(activity, topright),
                ImageUtil.dp2px(activity, buttomleft), ImageUtil.dp2px(activity, buttomleft),
                ImageUtil.dp2px(activity, buttomright), ImageUtil.dp2px(activity, buttomright)});
        drawable.setStroke(ImageUtil.dp2px(activity, width), broad_bg);
        drawable.setColor(bg);
        return drawable;
    }

    /**
     * 绘制边框
     *
     * @param activity    上下文
     * @param radius      圆角半径
     * @param width       线宽
     * @param Strokecolor 线的颜色
     * @param bg2         填充颜色
     * @return
     */
    public static GradientDrawable setMyshapeStrokeMyBg(Context activity, int radius, int width, int Strokecolor, int bg2) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
        drawable.setStroke(width, Strokecolor);
        drawable.setColor(bg2);
        return drawable;
    }

    /**
     * 设置圆角
     *
     * @param activity
     * @param radius
     * @param color
     * @return
     */
    public static GradientDrawable setMyCustomizeShape(Context activity, int radius, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
        drawable.setColor(ContextCompat.getColor(activity, color));
        return drawable;
    }

    public static GradientDrawable setMyshapeMineStroke(Context activity, int radius, int type) {
        GradientDrawable drawable = new GradientDrawable();
        if (type == 1) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.read_bg_1));
        } else if (type == 2) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.read_bg_2));
        } else if (type == 3) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.read_bg_3));
        } else if (type == 4) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(Color.WHITE);
        } else if (type == 5) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.lightcoral));
        } else if (type == 6) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.maincolor));
        } else if (type == 7) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.shallow_yellow));
        } else if (type == 8) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.white));
        } else if (type == 9) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.lightgray));
        } else if (type == 10) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.Bookshelf_sign2));
        } else if (type == 11) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.graybg));
        } else if (type == 12) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.lightgray1));
        } else if (type == 13) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.maincolor));
        } else if (type == 14) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.lightgray3));
        } else if (type == 15) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.whitetransparent));
        } else if (type == 16) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.login_button));
        } else if (type == 17) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.red));
        } else if (type == 18) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.brown));
        } else if (type == 19) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.red));
        } else if (type == 20) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.gray1));
        } else if (type == 21) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.fenzong));
        } else if (type == 22) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.lightgraybg));
        } else if (type == 23) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.maincolor));
        } else if (type == 24) {
            drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
            drawable.setColor(ContextCompat.getColor(activity, R.color.maincolor));
        }
        return drawable;
    }

    public static GradientDrawable setMyshapeStroke2(Context activity, int radius, int width, int bg, int bg2) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(ImageUtil.dp2px(activity, radius));
        drawable.setStroke(width, bg);
        if (bg2 != -1) {
            drawable.setColor(bg2);
        }
        return drawable;
    }

    /**
     * 设置渐变色
     *
     * @param startColor
     * @param endColor
     * @param radius
     * @param angle
     * @return
     */
    @SuppressLint("WrongConstant")
    public static GradientDrawable setGradient(int startColor, int endColor, int radius, int angle) {
        int[] colors = {startColor, endColor};
        GradientDrawable drawable = null;
        if (angle == 0) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        } else if (angle == 90) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
        } else if (angle == 180) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        } else if (angle == 270) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        } else if (angle == 315) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        }
        drawable.setCornerRadius(radius);
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        return drawable;
    }

    /**
     * 设置渐变色
     *
     * @param startColor
     * @param endColor
     * @param angle
     * @return
     */
    @SuppressLint("WrongConstant")
    public static GradientDrawable setGradient(int startColor, int endColor, int topLeftRadius, int topRightRadius,
                                               int bottomRightRadius, int bottomLeftRadius, int angle) {
        int[] colors = {startColor, endColor};
        GradientDrawable drawable = null;
        if (angle == 0) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        } else if (angle == 90) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
        } else if (angle == 180) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        } else if (angle == 270) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        } else if (angle == 315) {
            drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        }
        drawable.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        return drawable;
    }
}
