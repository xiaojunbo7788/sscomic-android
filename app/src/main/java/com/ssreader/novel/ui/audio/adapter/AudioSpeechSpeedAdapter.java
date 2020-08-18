package com.ssreader.novel.ui.audio.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AudioPronunciationBean;
import com.ssreader.novel.model.AudioSpeedBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioSpeechSpeedAdapter extends BaseRecAdapter<Object, AudioSpeechSpeedAdapter.ViewHolder> {

    private String value;

    private OnSpeechSpeedListener speechSpeedListener;

    public void setSpeechSpeedListener(OnSpeechSpeedListener speechSpeedListener) {
        this.speechSpeedListener = speechSpeedListener;
    }

    public AudioSpeechSpeedAdapter(Activity activity, List<Object> list, String value) {
        super(list, activity);
        this.value = value;
    }

    @Override
    public void onHolder(ViewHolder holder, Object bean, int position) {
        if (bean != null) {
            if (bean instanceof AudioSpeedBean) {
                // 语速
                holder.name.setText(((AudioSpeedBean) bean).audioName);
                if (value.equals(String.valueOf(((AudioSpeedBean) bean).audioDate))) {
                    holder.status.setVisibility(View.VISIBLE);
                } else {
                    holder.status.setVisibility(View.GONE);
                }
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (speechSpeedListener != null) {
                            speechSpeedListener.onClick(((AudioSpeedBean) bean).audioName, String.valueOf(((AudioSpeedBean) bean).audioDate));
                        }
                    }
                });
            } else if (bean instanceof AudioPronunciationBean) {
                // 发音人
                holder.name.setText(((AudioPronunciationBean) bean).getPronunciationName());
                if (value.equals(((AudioPronunciationBean) bean).getPronunciationParameter())) {
                    holder.status.setVisibility(View.VISIBLE);
                } else {
                    holder.status.setVisibility(View.GONE);
                }
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (speechSpeedListener != null) {
                            speechSpeedListener.onClick(((AudioPronunciationBean) bean).getPronunciationName(),
                                    String.valueOf(((AudioPronunciationBean) bean).getPronunciationParameter()));
                        }
                    }
                });
            }
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_dialog_speed));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_speed_name)
        TextView name;
        @BindView(R.id.item_dialog_speed_img)
        ImageView status;

        @BindView(R.id.item_speed_layout)
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSpeechSpeedListener{

        void onClick(String name, String value);
    }
}
