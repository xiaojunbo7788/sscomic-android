package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suke.widget.SwitchButton;
import com.ssreader.novel.R;
import com.ssreader.novel.ui.read.util.BrightnessUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.SystemUtil;

public class LookComicSetDialog {

    public static void getLookComicSetDialog(Activity activity) {
        Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_lookcomicset, null);
        SwitchButton pageModelBtn = view.findViewById(R.id.dialog_lookcomicset_fanye_ToggleButton);
        SwitchButton nightModelBtn = view.findViewById(R.id.dialog_lookcomicset_yejian_ToggleButton);

        if (ShareUitls.getBoolean(activity, "fanye_ToggleButton", true)) {
            pageModelBtn.setChecked(true);
        } else {
            pageModelBtn.setChecked(false);
        }
        if (ShareUitls.getBoolean(activity, "yejian_ToggleButton", false)) {
            nightModelBtn.setChecked(true);
        } else {
            nightModelBtn.setChecked(false);
        }
        pageModelBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                ShareUitls.putBoolean(activity, "fanye_ToggleButton", isChecked);
            }
        });
        nightModelBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                ShareUitls.putBoolean(activity, "yejian_ToggleButton", isChecked);
                if (isChecked) {
                    BrightnessUtil.setBrightness(activity, 30);
                } else {
                    // 恢复默认亮度
                    BrightnessUtil.setBrightness(activity, BrightnessUtil.getScreenBrightness(activity));
                }
            }
        });
        bottomDialog.setContentView(view);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        view.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.TranslateDialogFragment);
        bottomDialog.show();
        bottomDialog.onWindowFocusChanged(false);
        bottomDialog.setCanceledOnTouchOutside(true);
    }
}
