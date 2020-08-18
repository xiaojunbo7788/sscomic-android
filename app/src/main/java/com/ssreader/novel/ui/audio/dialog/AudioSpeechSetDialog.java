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
import com.ssreader.novel.model.AudioPronunciationBean;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.ui.audio.adapter.AudioSpeechSpeedAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.PublicStaticMethod;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于设置语速和语音
 */
public class AudioSpeechSetDialog {

    public static void showSpeechDialog(Activity activity, int type, String value, OnSpeechSpeedListener onSpeechSpeedListener) {
        Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);
        List<Object> list = new ArrayList();
        bottomDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_autio_speed, null);
        view.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10), "#f8f8f8"));
        bottomDialog.setContentView(view);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        view.setLayoutParams(params);
        TextView title = (TextView) view.findViewById(R.id.dialog_audio_title);
        View close = view.findViewById(R.id.close_dialog_layout);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        if (!list.isEmpty()) {
            list.clear();
        }
        if (type == 1) {
            // 语速
            title.setText(LanguageUtil.getString(activity, R.string.audio_speed));
            List<AudioSpeedBean> audioSpeedDate = PublicStaticMethod.getAudioSpeedDate(activity);
            list.addAll(audioSpeedDate);
        } else {
            // 发音人
            title.setText(LanguageUtil.getString(activity, R.string.audio_voice));
            List<AudioPronunciationBean> pronunciationDate = PublicStaticMethod.getPronunciationDate(activity);
            list.addAll(pronunciationDate);
        }
        AudioSpeechSpeedAdapter speechSpeedAdapter = new AudioSpeechSpeedAdapter(activity, list, value);
        RecyclerView recyclerView = view.findViewById(R.id.dialog_speed_rcy);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(speechSpeedAdapter);
        speechSpeedAdapter.setSpeechSpeedListener(new AudioSpeechSpeedAdapter.OnSpeechSpeedListener() {
            @Override
            public void onClick(String name, String value) {
                if (onSpeechSpeedListener != null) {
                    bottomDialog.dismiss();
                    onSpeechSpeedListener.onClick(name, value);
                }
            }
        });

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.TranslateDialogFragment);
        bottomDialog.show();
        bottomDialog.onWindowFocusChanged(false);
        bottomDialog.setCanceledOnTouchOutside(true);
    }

    public interface OnSpeechSpeedListener{

        void onClick(String name, String value);
    }
}
