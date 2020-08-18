package com.ssreader.novel.ui.read.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.read.util.BrightnessUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.read.manager.ReadSettingManager;
import com.ssreader.novel.ui.view.seekBar.DragSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrightnessDialog extends Dialog {

    @BindView(R.id.auto_setting_layout)
    View auto_setting_layout;
    @BindView(R.id.dialog_bright_seekBar)
    DragSeekBar seekBar;

    private Activity mContext;

    public BrightnessDialog(Context context) {
        this(context, R.style.setting_dialog);
        mContext = (Activity) context;
    }

    public BrightnessDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private ReadSettingManager config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_brightness);
        // 初始化View注入
        ButterKnife.bind(this);
        auto_setting_layout.setBackground(MyShape.setMyshapeStroke(mContext, 8, 8,
                0, 0, 1, ContextCompat.getColor(mContext, R.color.graybg), Color.WHITE));
        config = ReadSettingManager.getInstance();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
        setBrightness(config.getBrightness());
        seekBar.setDragProgressListener(new DragSeekBar.DragProgressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgressChanged(int progress) {
                changeBright(progress);
            }

            @Override
            public void onStop() {

            }
        });
    }

    /**
     * 改变亮度
     * @param brightness
     */
    public void changeBright(int brightness) {
        config.setBrightness(brightness);
        config.setNightMode(false);
        BrightnessUtil.setBrightness(mContext, brightness);
    }

    /**
     * 设置亮度
     * @param brightness
     */
    public void setBrightness(float brightness) {
        seekBar.setProgress((int) (brightness ));
    }
}
