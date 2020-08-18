package com.ssreader.novel.ui.audio.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.ui.audio.view.AudioLoadingView;
import com.ssreader.novel.ui.utils.ImageUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;

/**
 * 批量下载适配器
 */
public class AudioCatalogDownAdapter extends BaseRecAdapter<AudioChapter, AudioCatalogDownAdapter.ViewHolder> {

    private OnCatalogDownListener onCatalogDownListener;

    public void setOnCatalogDownListener(OnCatalogDownListener onCatalogDownListener) {
        this.onCatalogDownListener = onCatalogDownListener;
    }

    public boolean isChooseAll = false;

    private List<AudioChapter> list;
    private List<AudioChapter> audioChooseChapterList;
    private boolean flag;

    public AudioCatalogDownAdapter(Activity activity, boolean flag, List<AudioChapter> list, long audioId, List<AudioChapter> audioChooseChapterList) {
        super(list, activity);
        this.flag = flag;
        this.list = list;
        this.audioChooseChapterList = audioChooseChapterList;
    }

    public void setAudioChooseChapterList(List<AudioChapter> audioChooseChapterList) {
        this.audioChooseChapterList = audioChooseChapterList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHolder(ViewHolder holder, AudioChapter bean, int position) {
        if (bean != null) {
            if (bean.getIs_preview() == 1) {
                SpannableString spannableString = new SpannableString(bean.getChapter_title());
                Drawable drawable = activity.getDrawable(R.mipmap.audio_down_vip);
                drawable.setBounds(0, 0, ImageUtil.dp2px(activity, 35), ImageUtil.dp2px(activity, 16));
                ImageSpan imageSpan = new ImageSpan(drawable, Gravity.LEFT);
                spannableString.setSpan(imageSpan, 0, bean.getChapter_title().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.title.setText("");
                holder.title.append(spannableString);
                holder.title.append(bean.getChapter_title());
            } else {
                holder.title.setText(bean.getChapter_title());
            }
            if (bean.getSize() > 0) {
                holder.size.setText(new DecimalFormat("0.00").format((float) bean.getSize() / 1024 / 1024) + "M");
            }
            if (!flag) {
                if (bean.status == 1) {
                    holder.checkImage.setImageResource(R.mipmap.icon_down_load2);
                    holder.linearLayout.setEnabled(false);
                    holder.title.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                    holder.linearLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.graybg));
                } else {
                    if (bean.status != 2) {
                        if (!TextUtils.isEmpty(bean.path) && (new File(bean.path).exists())) {
                            // 已下载
                            holder.checkImage.setImageResource(R.mipmap.book_unenable);
                            holder.linearLayout.setEnabled(false);
                            holder.title.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.graybg));
                        } else {
                            holder.linearLayout.setEnabled(true);
                            holder.title.setTextColor(ContextCompat.getColor(activity, R.color.gray_title));
                            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                        }
                    } else {
                        holder.checkImage.setImageResource(R.mipmap.icon_down_load_fire2);
                        holder.linearLayout.setEnabled(true);
                        holder.title.setTextColor(ContextCompat.getColor(activity, R.color.gray_title));
                        holder.linearLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                    }
                }

                if (!audioChooseChapterList.isEmpty() && audioChooseChapterList.contains(bean)) {
                    bean.setIsChoose(true);
                    holder.checkImage.setImageResource(R.mipmap.book_checked);
                } else {
                    if (bean.status == 0) {
                        bean.setIsChoose(false);
                        holder.checkImage.setImageResource(R.mipmap.book_unchecked);
                    }
                }
            } else {
                if (!audioChooseChapterList.isEmpty() && audioChooseChapterList.contains(bean)) {
                    holder.checkImage.setImageResource(R.mipmap.book_checked);
                } else {
                    bean.setIsChoose(false);
                    holder.checkImage.setImageResource(R.mipmap.book_unchecked);
                }
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCatalogDownListener != null) {
                        bean.setIsChoose(!bean.getIsChoose());
                        if (bean.getIsChoose()) {
                            holder.checkImage.setImageResource(R.mipmap.book_checked);
                        } else {
                            holder.checkImage.setImageResource(R.mipmap.book_unchecked);
                        }
                        onCatalogDownListener.onChoose(position, bean);
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_audio_catalog_down));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_audio_catalog_down_layout)
        LinearLayout linearLayout;
        @BindView(R.id.audio_catalog_down_image)
        ImageView checkImage;
        @BindView(R.id.audio_catalog_down_chapter_title)
        TextView title;
        @BindView(R.id.audio_catalog_down_chapter_size)
        TextView size;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnCatalogDownListener {

        void onChoose(int position, AudioChapter audioChapter);

    }

    public int getChooseSumNum() {
        int sumNum = 0;
        for (AudioChapter audioChapter : list) {
            if (!flag) {
                // 下载
                if (TextUtils.isEmpty(audioChapter.path)) {
                    sumNum++;
                }
            } else {
                if (TextUtils.isEmpty(audioChapter.path)) {
                    sumNum++;
                }
            }
        }
        return sumNum;
    }
}
