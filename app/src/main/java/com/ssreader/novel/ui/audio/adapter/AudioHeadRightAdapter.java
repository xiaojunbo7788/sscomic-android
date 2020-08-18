package com.ssreader.novel.ui.audio.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AudioHeadRightBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioHeadRightAdapter extends BaseRecAdapter<AudioHeadRightBean, AudioHeadRightAdapter.ViewHolder> {

    private boolean isSound;
    private Dialog dialog;

    private OnAudioHeadListener onAudioHeadListener;

    public AudioHeadRightAdapter(boolean isSound, List<AudioHeadRightBean> list, Activity activity,
                                 Dialog dialog, OnAudioHeadListener onAudioHeadListener) {
        super(list, activity);
        this.isSound = isSound;
        this.dialog = dialog;
        this.onAudioHeadListener = onAudioHeadListener;
    }

    @Override
    public void onHolder(ViewHolder viewHolder, AudioHeadRightBean bean, int position) {
        viewHolder.itemAudioHeadRightTv.setText(bean.getImaName());
        viewHolder.itemAudioHeadRightImg.setImageResource(bean.img);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onAudioHeadListener != null) {
                    onAudioHeadListener.onClick(isSound, bean);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateHolder() {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_audio_head_right, null);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends BaseRecViewHolder {
        @BindView(R.id.item_audio_head_right_img)
        ImageView itemAudioHeadRightImg;
        @BindView(R.id.item_audio_head_right_tv)
        TextView itemAudioHeadRightTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnAudioHeadListener{

        void onClick(boolean isSound, AudioHeadRightBean bean);
    }
}
