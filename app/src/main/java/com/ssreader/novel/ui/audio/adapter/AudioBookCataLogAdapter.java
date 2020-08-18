package com.ssreader.novel.ui.audio.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.BookChapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioBookCataLogAdapter extends BaseRecAdapter<Object, AudioBookCataLogAdapter.ViewHolder> {

    public long currentListenerChapterId;

    private OnCatalogClickListener onCatalogClickListener;

    public void setOnCatalogClickListener(OnCatalogClickListener onCatalogClickListener) {
        this.onCatalogClickListener = onCatalogClickListener;
    }

    public AudioBookCataLogAdapter(Activity activity, List<Object> list) {
        super(list, activity);
    }

    @Override
    public void onHolder(ViewHolder viewHolder, Object bean, int position) {
        if (bean != null) {
            if (bean instanceof BookChapter) {
                if (currentListenerChapterId != 0) {
                    if (((BookChapter) bean).getChapter_id() == currentListenerChapterId) {
                        viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                        viewHolder.itemAudioPkayImg.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.itemAudioPkayImg.setVisibility(View.GONE);
                        if (((BookChapter) bean).is_read == 0) {
                            viewHolder.itemChapterCatalogTitle.setTextColor(Color.BLACK);
                        } else {
                            viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                        }
                    }
                } else {
                    // 本地书籍
                    if (position == 0) {
                        viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                        viewHolder.itemAudioPkayImg.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.itemAudioPkayImg.setVisibility(View.GONE);
                        if (((BookChapter) bean).is_read == 0) {
                            viewHolder.itemChapterCatalogTitle.setTextColor(Color.BLACK);
                        } else {
                            viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                        }
                    }
                }
                // 去除章节名首位空格
                if (!((BookChapter) bean).getChapter_title().isEmpty()) {
                    viewHolder.itemChapterCatalogTitle.setText(((BookChapter) bean).getChapter_title().trim());
                }
                viewHolder.itemChapterCatalogVip.setVisibility(((BookChapter) bean).getIs_preview() == 1 ? View.VISIBLE : View.GONE);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCatalogClickListener != null) {
                            onCatalogClickListener.onBookChapter(position, (BookChapter) bean);
                        }
                    }
                });
            } else if (bean instanceof AudioChapter){
                // 有声
                if (currentListenerChapterId != 0) {
                    if (((AudioChapter) bean).getChapter_id() == currentListenerChapterId) {
                        viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                        viewHolder.itemAudioPkayImg.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.itemAudioPkayImg.setVisibility(View.GONE);
                        if (((AudioChapter) bean).is_read == 0) {
                            viewHolder.itemChapterCatalogTitle.setTextColor(Color.BLACK);
                        } else {
                            viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                        }
                    }
                } else {
                    // 本地书籍
                    if (position == 0) {
                        viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                        viewHolder.itemAudioPkayImg.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.itemAudioPkayImg.setVisibility(View.GONE);
                        if (((AudioChapter) bean).is_read == 0) {
                            viewHolder.itemChapterCatalogTitle.setTextColor(Color.BLACK);
                        } else {
                            viewHolder.itemChapterCatalogTitle.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                        }
                    }
                }
                // 去除章节名首位空格
                if (!((AudioChapter) bean).getChapter_title().isEmpty()) {
                    viewHolder.itemChapterCatalogTitle.setText(((AudioChapter) bean).getChapter_title().trim());
                }
                viewHolder.itemChapterCatalogVip.setVisibility(((AudioChapter) bean).getIs_preview() == 1 ? View.VISIBLE : View.GONE);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCatalogClickListener != null) {
                            onCatalogClickListener.onAudioChapter(position, (AudioChapter) bean);
                        }
                    }
                });
            }
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_audio_bookcatelog));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_audio_pkay_img)
        ImageView itemAudioPkayImg;
        @BindView(R.id.item_chapter_catalog_title)
        TextView itemChapterCatalogTitle;
        @BindView(R.id.item_chapter_catalog_vip)
        ImageView itemChapterCatalogVip;
        @BindView(R.id.item_chapter_catalog_linearLayout)
        LinearLayout itemChapterCatalogLinearLayout;

        ViewHolder(View view) {
            super(view);
        }
    }

    public interface OnCatalogClickListener{

        void onBookChapter(int index, BookChapter bookChapter);

        void onAudioChapter(int index, AudioChapter audioChapter);
    }
}
