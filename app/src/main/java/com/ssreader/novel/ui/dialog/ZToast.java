package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.MyToash;

public class ZToast {

    private static ZToast mToastInstance;

    private Activity mActivity;
    private RelativeLayout mToastLayout;
    private ToastLayout mToast;
    private String text;
    private long times;
    private MyToash.ToastDismissListener listener;

    /**
     * 静态可设置参数
     */
    public static int TextColor = Color.BLACK;
    public static int BgColor = Color.WHITE;
    public static boolean isShowIcon = true;
    public static int height = 80;
    public static int resId;

    /**
     * 设置小图标
     *
     * @param resId
     */
    public static void setResId(int resId) {
        ZToast.resId = resId;
    }

    /**
     * 设置高度
     *
     * @param height
     */
    public static void setHeight(int height) {
        ZToast.height = height;
    }

    /**
     * 图标是否显示
     *
     * @param isShowIcon
     */
    public static void setIsShowIcon(boolean isShowIcon) {
        ZToast.isShowIcon = isShowIcon;
    }

    /**
     * 背景色
     *
     * @param bgColor
     */
    public static void setBgColor(int bgColor) {
        BgColor = bgColor;
    }

    /**
     * 文字颜色
     *
     * @param textColor
     */
    public static void setTextColor(int textColor) {
        TextColor = textColor;
    }

    /**
     * 初始化
     *
     * @param BgColor   背景颜色
     * @param TextColor 文字颜色
     * @param isIcon    图标是否显示
     * @param resId     图标
     * @param height    高度
     */
    public static void init(int BgColor, int TextColor, boolean isIcon, int resId, int height) {
        ZToast.BgColor = BgColor;
        ZToast.TextColor = TextColor;
        ZToast.isShowIcon = isIcon;
        ZToast.height = height;
        ZToast.resId = resId;
    }

    public ZToast(Activity mActivity, String text, long times, MyToash.ToastDismissListener listener) {
        this.mActivity = mActivity;
        this.text = text;
        this.times = times;
        this.listener = listener;
    }

    public static ZToast MakeText(Activity mActivity, String text, long times, int resId, MyToash.ToastDismissListener listener) {
        ZToast.resId = resId;
        mToastInstance = new ZToast(mActivity, text, times, listener);
        return mToastInstance;
    }

    /**
     * 展示
     */
    public void show() {
        if (mActivity != null) {
            mToastLayout = mActivity.findViewById(R.id.rl_toast);
            if (mToastLayout == null) {//判断是否已经添加进母VIEW里，没有则添加进去
                mToast = new ToastLayout(mActivity);
                initToast(mToast);
                mActivity.addContentView(mToast, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ToastLayout.dip2px(mActivity, height)));
            } else {//如果有，则直接取出
                mToast = (ToastLayout) mToastLayout.getParent();
                if (resId != 0) {
                    mToast.setIcon(resId);
                }
            }
            mToast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mToast.setContent(text);
            mToast.showToast(times);
            mToast.addAnimationListener(new ToastLayout.AnimationStatusListener() {
                @Override
                public void onStatus() {
                    if (listener != null) {
                        listener.onDismiss();
                    }
                }
            });
        }
    }

    /**
     * 设置各个参数
     *
     * @param mToast
     */
    private void initToast(ToastLayout mToast) {
        if (TextColor != 0) {
            mToast.setTextColor(TextColor);
        }
        if (BgColor != 0) {
            mToast.setBgColor(BgColor);
        }
        if (resId != 0) {
            mToast.setIcon(resId);
        }

        mToast.setIconVisible(isShowIcon);
        mToast.setHeight(height);
    }

    private boolean isShowToast() {
        if (mToast == null) {
            return false;
        }
        return mToast.isShow();
    }

    /**
     * 是否在展示
     *
     * @return
     */
    public static boolean isShow() {
        if (mToastInstance == null) {
            return false;
        } else {
            boolean isShow = mToastInstance.isShowToast();
            mToastInstance = null;
            return isShow;
        }
    }
}