package com.ssreader.novel.ui.audio.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.ui.utils.ImageUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * 有声目录的适配器
 */
public class AudioCatalogAdapter extends BaseRecAdapter<AudioChapter, AudioCatalogAdapter.ViewHolder> {

    private long currentChapterId = 0;

    private OnCatalogListener onCatalogListener;

    public void setOnCatalogListener(OnCatalogListener onCatalogListener) {
        this.onCatalogListener = onCatalogListener;
    }

    /**
     * 构造函数
     *
     * @param activity
     * @param list
     * @param audioId
     */
    public AudioCatalogAdapter(Activity activity, List<AudioChapter> list, long audioId) {
        super(list, activity);
    }

    @Override
    public void onHolder(ViewHolder holder, AudioChapter bean, int position) {
        if (bean != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.relativeLayout.getLayoutParams();
            if (position == 0) {
                params.topMargin = ImageUtil.dp2px(activity, 5);
            } else {
                params.topMargin = ImageUtil.dp2px(activity, 15);
            }
            holder.relativeLayout.setLayoutParams(params);
            holder.name.setText(bean.getChapter_title());
            if (currentChapterId != 0 && currentChapterId == bean.chapter_id) {
                bean.is_read = 1;
                holder.name.setTextColor(ContextCompat.getColor(activity, R.color.add_shelf_bg));
            } else {
                if (bean.is_read == 1) {
                    holder.name.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                } else {
                    holder.name.setTextColor(ContextCompat.getColor(activity, R.color.black_3));
                }
            }
//            holder.statusText.get(0).setText(bean.getDuration_time());
            if (TextUtils.isEmpty(bean.getPlay_num())) {
                holder.playerNumLayout.setVisibility(View.GONE);
            } else {
                holder.playerNumLayout.setVisibility(View.VISIBLE);
                holder.statusText.get(0).setText(bean.getPlay_num());
            }
            holder.statusText.get(1).setText(bean.getUpdate_time());
            if (position == list.size() - 1) {
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }
            final String path = bean.path;
            if (bean.status == 1) {
                holder.downImage.setImageResource(R.mipmap.icon_down_load);
            } else {
                if (bean.status != 2) {
                    if (path != null && (new File(path).exists())) { //本地存在
                        // 已下载
                        holder.downImage.setImageResource(R.mipmap.icon_downed);
                    } else {
                        if (bean.getIs_preview() == 1) {
                            holder.downImage.setImageResource(R.mipmap.icon_down_vip);
                        } else {
                            holder.downImage.setImageResource(R.mipmap.icon_down_gray);
                        }
                    }
                } else {
                    holder.downImage.setImageResource(R.mipmap.icon_down_load_fire);
                }
            }

            holder.bgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCatalogListener != null) {
                        onCatalogListener.onPlayer(bean);
                    }
                }
            });
            holder.downLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCatalogListener != null) {
                        // 只有未下载的才可以下载
                        if (path == null || !(new File(path).exists())) {
                            onCatalogListener.onDown(bean);
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_audio_catalog));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_audio_catalog_layout)
        LinearLayout bgLayout;
        @BindView(R.id.item_audio_catalog_content_layout)
        RelativeLayout relativeLayout;
        @BindView(R.id.item_audio_catalog_name)
        TextView name;
        @BindView(R.id.item_audio_catalog_player_num_layout)
        LinearLayout playerNumLayout;
        @BindViews({R.id.item_audio_catalog_player_num, R.id.item_audio_catalog_update_time})
        List<TextView> statusText;
        @BindView(R.id.item_audio_catalog_down_layout)
        RelativeLayout downLayout;
        @BindView(R.id.item_audio_catalog_down)
        ImageView downImage;
        @BindView(R.id.item_audio_catalog_line)
        View line;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnCatalogListener {

        void onDown(AudioChapter bean);

        void onPlayer(AudioChapter bean);
    }

    /**
     * 更新当前播放的章节id
     *
     * @param currentChapterId
     */
    public void setCurrentChapterId(long currentChapterId) {
        this.currentChapterId = currentChapterId;
    }
}
