package com.ssreader.novel.ui.read.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import com.ssreader.novel.R;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.read.manager.ReadSettingManager;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.seekBar.DragSeekBar;
import com.ssreader.novel.utils.LanguageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自动阅读设置
 */
public class AutoSettingDialog extends Dialog {

    @BindView(R.id.auto_setting_seekBar)
    IndicatorSeekBar auto_setting_seekBar;
    @BindView(R.id.auto_setting_exit)
    View auto_setting_exit;
    @BindView(R.id.auto_setting_layout)
    View auto_setting_layout;

    private ReadActivity mContext;
    private ReadSettingManager mConfig;
    private int mAutoSpeed;
    private boolean isChanged = false;
    private SettingDialog mSettingDialog;

    private AutoSettingDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public AutoSettingDialog(Context context) {
        this(context, R.style.setting_dialog);
        mContext = (ReadActivity) context;
    }

    public AutoSettingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setSettingDialog(SettingDialog dialog) {
        mSettingDialog = dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_auto_setting);
        // 初始化View注入
        ButterKnife.bind(this);
        auto_setting_layout.setBackground(MyShape.setMyshapeStroke(mContext, 8, 8,
                0, 0, 1, ContextCompat.getColor(mContext, R.color.graybg), Color.WHITE));
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
        mConfig = ReadSettingManager.getInstance();
        //auto_setting_seekBar.setMaxProgress(55);
         mAutoSpeed = (int) mConfig.getAutoSpeed();
        auto_setting_seekBar.setProgress(mAutoSpeed);
        auto_setting_seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                isChanged=true;
               // MyToash.Log("setting_seekBar",seekBar.getProgress()+"");
                mAutoSpeed=seekBar.getProgress();
                mConfig.setAutoSpeed(mAutoSpeed);

            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ImmersionBar.with(mContext).hideBar(BarHide.FLAG_HIDE_BAR).init();
                if (AutoProgress.getInstance().isStop()) {
                    return;
                }
                AutoProgress.getInstance().setTime(mAutoSpeed);
                if (isChanged) {
                    AutoProgress.getInstance().restart();
                } else {
                    AutoProgress.getInstance().goStill();
                }
                isChanged = false;
            }
        });
    }

    public Boolean isShow() {
        return isShowing();
    }

    @OnClick({R.id.auto_setting_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_setting_exit:
                MyToash.ToashSuccess(mContext, LanguageUtil.getString(mContext, R.string.ReadActivity_auto_read_close));
                if (AutoProgress.getInstance().isStarted() && !AutoProgress.getInstance().isStop()) {
                    AutoProgress.getInstance().stop();
                    dismiss();
                }
                if (mSettingDialog != null && mSettingDialog.isShowing()) {
                    mSettingDialog.dismiss();
                }
                break;
        }
    }

  /*  @Override
    public void show() {
        super.show();
        auto_setting_seekBar.setProgress((int) (mConfig.getAutoSpeed()));
    }*/
}