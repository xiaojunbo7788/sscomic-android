package com.ssreader.novel.ui.audio.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.AudioPronunciationBean;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.ui.utils.MyToash;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioListAdapter extends BaseListAdapter {
    int type;
    public AudioListAdapter(Activity activity, List list,int type) {
        super(activity, list);
        this.type = type;
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_audio_list;
    }

    @Override
    public View getOwnView(int position, Object been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        MyToash.Log("getOwnView",mList.size());
        //语速
        if(type==1){
            AudioSpeedBean audioSpeedBean = (AudioSpeedBean) mList.get(position);
            viewHolder.itemAudioName.setText(audioSpeedBean.audioName);
        }else{
        //发音人
            AudioPronunciationBean audioSpeedBean = (AudioPronunciationBean) mList.get(position);
            viewHolder.itemAudioName.setText(audioSpeedBean.pronunciationName);
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item_audio_name)
        TextView itemAudioName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
