package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.suke.widget.SwitchButton;
import com.ssreader.novel.R;
import com.ssreader.novel.ui.read.util.BrightnessUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.SystemUtil;

public class LookComicSetDialog {

    public static void getLookComicSetDialog(Activity activity,int mode,LookComicSetDialogListener lookComicSetDialogListener) {
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

        TextView mode1View = view.findViewById(R.id.lookcomicset_mode1);
        TextView mode2View = view.findViewById(R.id.lookcomicset_mode2);
        if (mode == 1) {
            mode1View.setBackgroundResource(R.drawable.comic_mode_sel);
            mode2View.setBackgroundResource(R.drawable.comic_mode_unsel);
            mode1View.setTextColor(ContextCompat.getColor(activity, R.color.white));
            mode2View.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
        } else {
            mode2View.setBackgroundResource(R.drawable.comic_mode_sel);
            mode1View.setBackgroundResource(R.drawable.comic_mode_unsel);
            mode2View.setTextColor(ContextCompat.getColor(activity, R.color.white));
            mode1View.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
        }
        mode1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode1View.setBackgroundResource(R.drawable.comic_mode_sel);
                mode2View.setBackgroundResource(R.drawable.comic_mode_unsel);
                mode1View.setTextColor(ContextCompat.getColor(activity, R.color.white));
                mode2View.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));

                if (lookComicSetDialogListener != null) {
                    lookComicSetDialogListener.changeReaderMode(1);
                }
            }
        });

        mode2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mode2View.setBackgroundResource(R.drawable.comic_mode_sel);
                mode1View.setBackgroundResource(R.drawable.comic_mode_unsel);
                mode2View.setTextColor(ContextCompat.getColor(activity, R.color.white));
                mode1View.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));

                if (lookComicSetDialogListener != null) {
                    lookComicSetDialogListener.changeReaderMode(2);
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

    public interface LookComicSetDialogListener {
        void changeReaderMode(int mode);
    }
}
