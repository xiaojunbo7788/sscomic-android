package com.ssreader.novel.ui.audio.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BasePopupWindow;
import com.ssreader.novel.model.AudioPronunciationBean;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.ui.audio.adapter.AudioListAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.PublicStaticMethod;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 语速
 */
public class AudioSpeedSoundPopwindow extends BasePopupWindow {

    public int type;
    public List list;

    public AudioSpeedSoundPopwindow(Activity activity, View view, int width, int height, int type) {
        super(width, height);
        this.activity = activity;
        this.type = type;
        onCreate(activity);
        setFocusable(true);
        showAsDropDown(view);
    }

    @Override
    public int initContentView() {
        return R.layout.piblic_listview;
    }

    @Override
    public void initView() {
        ViewHolder viewHolder = new ViewHolder(view);
        list = new ArrayList();
        if (type == 1) {
            //语速
            List<AudioSpeedBean> audioSpeedDate = PublicStaticMethod.getAudioSpeedDate(activity);
            list.addAll(audioSpeedDate);
        } else {
            //播音人
            List<AudioPronunciationBean> pronunciationDate = PublicStaticMethod.getPronunciationDate(activity);
            list.addAll(pronunciationDate);
        }
        AudioListAdapter audioListAdapter = new AudioListAdapter(activity, list, type);
        viewHolder.publicListviwe.setAdapter(audioListAdapter);
        viewHolder.publicListviwe.setBackground(MyShape.setMyshape(activity,0, 0,
                ImageUtil.dp2px(activity, 5), ImageUtil.dp2px(activity, 5),1,
                ContextCompat.getColor(activity, R.color.grayline),ContextCompat.getColor(activity, R.color.white) ));
        viewHolder.publicListviwe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                onItemClickLisenter.itemClick(list.get(position));
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    class ViewHolder {
        @BindView(R.id.public_listviwe)
        ListView publicListviwe;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    onItemClickLisenter onItemClickLisenter;

    public void setOnItemClickLisenter(AudioSpeedSoundPopwindow.onItemClickLisenter onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
    }

    public interface onItemClickLisenter{
        void itemClick(Object o);
    }
}
