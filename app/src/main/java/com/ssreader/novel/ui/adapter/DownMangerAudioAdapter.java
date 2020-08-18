package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.ui.activity.AudioDownActivity;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.audio.AudioSoundActivity;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownMangerAudioAdapter extends BaseListAdapter<Audio> {
    public List<Audio> checkList;
    public boolean Edit;

    public DownMangerAudioAdapter(Activity activity, List<Audio> list, SCOnItemClickListener scOnItemClickListener) {
        super(activity, list,scOnItemClickListener);
        checkList = new ArrayList<>();
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_downmangercomic;
    }

    @Override
    public View getOwnView(int position, Audio audio, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(convertView);
      /*  if (position == mList.size() - 1) {
            viewHolder.item_down_comic_view.setVisibility(View.GONE);
        }*/
        viewHolder.item_player_img.setVisibility(View.VISIBLE);
        viewHolder.mItemDowmmangerOpen.setBackground(MyShape.setMyCustomizeShape(activity, 22, R.color.maincolor));

        viewHolder.mItemDowmmangerOpen.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_gone_audio));

        viewHolder.mItemDowmmangerCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Edit) {
                    Intent intent = new Intent(activity, AudioInfoActivity.class);
                    intent.putExtra("audio_id", audio.audio_id);
                    activity.startActivity(intent);
                } else {
                    onClickEdit(position, audio, viewHolder.itemBianjiCheckBox);
                }
            }
        });
        viewHolder.mItemDowmmangerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Edit) {
                    // 调起服务
                    AudioManager.openService(activity);
                    // 进入有声界面
                    Intent soundIntent = new Intent();
                    soundIntent.putExtra("audio", audio);
                    soundIntent.setClass(activity, AudioSoundActivity.class);
                    activity.startActivity(soundIntent);
                    activity.overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
                } else {
                    onClickEdit(position, audio, viewHolder.itemBianjiCheckBox);
                }
            }
        });
        MyGlide.GlideImage(activity, audio.cover, viewHolder.mItemDowmmangerCover, ImageUtil.dp2px(activity, 113), ImageUtil.dp2px(activity, 150));
        viewHolder.mItemDowmmangerName.setText(audio.getName());
        viewHolder.mItemDowmmangerXiazaiprocess.setText(String.format(LanguageUtil.getString(activity, R.string.ComicDownActivity_downprocess_audio),
                audio.down_chapters , audio.total_chapter));
        viewHolder.mItemDowmmangerLinearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edit) {
                    onClickEdit(position, audio,viewHolder.itemBianjiCheckBox);
                }else {
                    Intent intent = new Intent(activity, AudioDownActivity.class);
                    intent.putExtra("audio", audio);
                    intent.putExtra("flag", true);
                    activity.startActivity(intent);
                }
            }
        });
        if (Edit) {
            viewHolder.mItemDowmmangerOpen.setVisibility(View.GONE);
            viewHolder.itemBianjiCheckBox.setVisibility(View.VISIBLE);
            if(checkList.contains(audio)){
                viewHolder.itemBianjiCheckBox.setChecked(true);
            }else {
                viewHolder.itemBianjiCheckBox.setChecked(false);
            }
        } else {
            viewHolder.mItemDowmmangerOpen.setVisibility(View.VISIBLE);
            viewHolder.itemBianjiCheckBox.setVisibility(View.GONE);

        }
        return convertView;
    }

    private void onClickEdit(int position, Audio downoption, CheckBox itemBianjiCheckBox) {
        if (checkList.contains(downoption)) {
            checkList.remove(downoption);
            itemBianjiCheckBox.setChecked(false);
        } else {
            checkList.add(downoption);
            itemBianjiCheckBox.setChecked(true);
        }
        if (scOnItemClickListener != null) {
            scOnItemClickListener.OnItemClickListener(checkList.size(), position, downoption);
        }
    }

    class ViewHolder {
        @BindView(R.id.item_dowmmanger_cover)
        ImageView mItemDowmmangerCover;
        @BindView(R.id.item_dowmmanger_name)
        TextView mItemDowmmangerName;
        @BindView(R.id.item_dowmmanger_xiazaiprocess)
        TextView mItemDowmmangerXiazaiprocess;
        @BindView(R.id.item_dowmmanger_open)
        TextView mItemDowmmangerOpen;
        @BindView(R.id.item_dowmmanger_LinearLayout1)
        LinearLayout mItemDowmmangerLinearLayout1;


        @BindView(R.id.item_player_img)
        ImageView item_player_img;
        @BindView(R.id.item_bianji_checkBox)
        public CheckBox itemBianjiCheckBox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
