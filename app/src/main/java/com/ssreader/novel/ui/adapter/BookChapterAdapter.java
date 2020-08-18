package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;

import java.util.List;

import butterknife.BindView;

/**
 * 作品章节目录的adapter
 */
public class BookChapterAdapter extends BaseRecAdapter<BookChapter, BookChapterAdapter.ViewHolder> {

    public long current_chapter_id;

    public BookChapterAdapter(Activity activity, List<BookChapter> list, SCOnItemClickListener scOnItemClickListener) {
        super(list, activity, scOnItemClickListener);
    }

    @Override
    public void onHolder(ViewHolder viewHolder, BookChapter chapterItem, int position) {
        if (chapterItem != null) {
            viewHolder.public_list_line_id.setVisibility((position == NoLinePosition) ? View.GONE : View.VISIBLE);

            if (chapterItem.book_id < Constant.LOCAL_BOOKID) {
                if (chapterItem.getChapter_id() == current_chapter_id) {
                    viewHolder.title.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                } else {
                    if (chapterItem.getIs_read() == 0) {
                        viewHolder.title.setTextColor(Color.BLACK);
                    } else {
                        viewHolder.title.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
                    }
                }
            } else {
                // 本地书籍
                if (position == 0) {
                    viewHolder.title.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                } else {
                    if (chapterItem.is_read == 0) {
                        viewHolder.title.setTextColor(Color.BLACK);
                    } else {
                        viewHolder.title.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
                    }
                }
            }
            //去除章节名首位空格
            if (!chapterItem.getChapter_title().isEmpty()) {
                viewHolder.title.setText(chapterItem.getChapter_title().trim());
            }
            viewHolder.isCanRead.setVisibility(chapterItem.getIs_preview() == 1 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_chapter_catalog));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_chapter_catalog_linearLayout)
        LinearLayout catalogLinearLayout;
        @BindView(R.id.item_chapter_catalog_title)
        TextView title;
        @BindView(R.id.item_chapter_catalog_vip)
        ImageView isCanRead;
        @BindView(R.id.public_list_line_id)
        View public_list_line_id;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
