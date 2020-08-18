package com.ssreader.novel.ui.audio.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshAudioShelf;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * 有声详情推荐适配器
 */
public class AudioInfoRecommendAdapter extends BaseRecAdapter<BaseBookComic, AudioInfoRecommendAdapter.ViewHolder> {

    private int imgWidth;
    private int imgHeight;

    public AudioInfoRecommendAdapter(List<BaseBookComic> list, Activity activity) {
        super(list, activity);
        imgWidth = (ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 70)) / 3;
        imgHeight = imgWidth * 4 / 3;
    }

    @Override
    public void onHolder(ViewHolder viewHolder, BaseBookComic bean, int position) {
        if (bean != null) {
            viewHolder.line.setVisibility(position == NoLinePosition ? View.GONE : View.VISIBLE);
            viewHolder.item_store_lable_male_horizontal_player.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = viewHolder.itemStoreLabelMaleHorizontalImg.getLayoutParams();
            layoutParams.height = imgHeight;
            layoutParams.width = imgWidth;
            viewHolder.itemStoreLabelMaleHorizontalImg.setLayoutParams(layoutParams);
            MyGlide.GlideImageNoSize(activity, bean.cover, viewHolder.itemStoreLabelMaleHorizontalImg);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHolder.item_store_lable_male_horizontal_player.getLayoutParams();
            params.height = params.width = (int) (imgHeight * 0.1);
            params.leftMargin = params.bottomMargin = (int) (imgHeight * 0.05);
            viewHolder.item_store_lable_male_horizontal_player.setLayoutParams(params);

            viewHolder.itemStoreLabelMaleHorizontalName.setText(bean.getName());
            viewHolder.itemStoreLabelMaleHorizontalDescription.setText(bean.getDescription());
            viewHolder.itemStorePlayerNum.setText(bean.total_views);
            viewHolder.itemStoreLableCollection.setTextSize(12);
            viewHolder.itemStoreLableCollection.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
            viewHolder.mAudio = ObjectBoxUtils.getAudio(bean.audio_id);
            if (viewHolder.mAudio == null || viewHolder.mAudio.is_collect == 0) {
                viewHolder.itemStoreLableCollection.setText(LanguageUtil.getString(activity, R.string.audio_collection));
            } else {
                viewHolder.itemStoreLableCollection.setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_collection));
            }
            viewHolder.itemStoreLableCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.mAudio == null) {
                        viewHolder.mAudio = new Audio();
                        viewHolder.mAudio.audio_id = bean.audio_id;
                        viewHolder.mAudio.finished = bean.is_finished;
//                        if (bean.author != null && !bean.author.isEmpty() && !TextUtils.isEmpty(bean.author.get(0))) {
//                            StringBuilder name = new StringBuilder();
//                            for (int i = 0; i < bean.author.size(); i++) {
//                                if (i == 0) {
//                                    name.append(bean.author.get(i));
//                                } else {
//                                    name.append(" ").append(bean.author.get(i));
//                                }
//                            }
//                            viewHolder.mAudio.author = name.toString();
//                        }
                        viewHolder.mAudio.author = bean.author.replaceAll(",", " ");
                        viewHolder.mAudio.cover = bean.getCover();
                        viewHolder.mAudio.name = bean.getName();
                        viewHolder.mAudio.description = bean.getDescription();
                    }
                    if (viewHolder.mAudio.is_collect == 0) {
                        viewHolder.mAudio.is_collect = 1;
                        ObjectBoxUtils.addData(viewHolder.mAudio, Audio.class);
                        viewHolder.itemStoreLableCollection.setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_collection));
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujias));
                        EventBus.getDefault().post(new RefreshShelf(Constant.AUDIO_CONSTANT, new RefreshAudioShelf(viewHolder.mAudio, 1)));
                    }
                }
            });

            viewHolder.itemStoreLableCollection.setBackground(MyShape.setMyshapeStroke2(activity, 20, 2, ContextCompat.getColor(activity, R.color.maincolor), 0));
            viewHolder.itemStatus.setText(bean.finished);
            //收藏点击事件
            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, AudioInfoActivity.class)
                            .putExtra("audio_id", bean.audio_id));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_store_label_male_horizontal));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_store_label_male_horizontal_img)
        ImageView itemStoreLabelMaleHorizontalImg;
        @BindView(R.id.item_store_label_male_horizontal_name)
        TextView itemStoreLabelMaleHorizontalName;
        @BindView(R.id.item_store_label_male_horizontal_description)
        TextView itemStoreLabelMaleHorizontalDescription;
        @BindView(R.id.item_store_label_male_horizontal_status)
        TextView itemStatus;
        @BindView(R.id.item_store_player_num)
        TextView itemStorePlayerNum;
        @BindView(R.id.item_store_lable_collection)
        TextView itemStoreLableCollection;
        @BindView(R.id.item_layout)
        LinearLayout itemLayout;
        @BindView(R.id.item_store_lable_male_horizontal_player)
        ImageView item_store_lable_male_horizontal_player;
        @BindView(R.id.public_list_line_id)
        View line;

        public Audio mAudio;

        ViewHolder(View view) {
            super(view);
        }
    }
}
