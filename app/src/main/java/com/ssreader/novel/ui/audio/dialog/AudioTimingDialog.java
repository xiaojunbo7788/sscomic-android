package com.ssreader.novel.ui.audio.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.ui.adapter.DialogAudioSpeedAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

public class AudioTimingDialog {

    public static void showDownoption(Activity activity, List list, AudioSpeedBean audioSpeedBean) {
        Dialog  bottomDialog = new Dialog(activity, R.style.BottomDialog);
        bottomDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_autio_speed, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_audio_title);
        title.setText(LanguageUtil.getString(activity, R.string.audio_timing));
        View close_dialog_layout = view.findViewById(R.id.close_dialog_layout);
        close_dialog_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        RecyclerView dialog_speed_rcy = view.findViewById(R.id.dialog_speed_rcy);
        DialogAudioSpeedAdapter dialogAudioSpeedAdapter = new DialogAudioSpeedAdapter(list, audioSpeedBean, activity, bottomDialog);
        dialog_speed_rcy.setLayoutManager(new LinearLayoutManager(activity));
        dialog_speed_rcy.setAdapter(dialogAudioSpeedAdapter);
        view.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10), "#f8f8f8"));
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
