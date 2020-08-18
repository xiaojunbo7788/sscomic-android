package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.eventbus.AudioSpeedBeanEventbus;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.utils.LanguageUtil;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogAudioSpeedAdapter extends BaseRecAdapter {

    private AudioSpeedBean audioSpeedBean;
    private Dialog dialog;

    public DialogAudioSpeedAdapter(List<AudioSpeedBean> list, AudioSpeedBean audioSpeedBean, Activity activity, Dialog dialog) {
        super(list, activity);
        this.audioSpeedBean = audioSpeedBean;
        if (this.audioSpeedBean == null) {
            this.audioSpeedBean = new AudioSpeedBean(LanguageUtil.getString(activity, R.string.audio_not_open), 0, true);
        }
        this.dialog = dialog;
    }

    @Override
    public void onHolder(BaseRecViewHolder holder, Object bean, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        AudioSpeedBean speedBean = (AudioSpeedBean) list.get(position);
        viewHolder.itemSpeedName.setText(speedBean.audioName);
        if (speedBean.audioName.equals(audioSpeedBean.audioName)) {
            viewHolder.itemDialogSpeedImg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.itemDialogSpeedImg.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new AudioSpeedBeanEventbus((speedBean), true));
            }
        });
    }

    @Override
    public BaseRecViewHolder onCreateHolder() {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_dialog_speed, null);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_speed_name)
        TextView itemSpeedName;
        @BindView(R.id.item_dialog_speed_img)
        ImageView itemDialogSpeedImg;

        @BindView(R.id.item_speed_layout)
        View item_speed_layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
