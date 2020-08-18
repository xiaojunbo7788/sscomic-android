package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.CommentInfoBean;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

public class CommentInfoAdapter extends BaseRecAdapter<CommentInfoBean.CommentInfo, CommentInfoAdapter.ViewHolder> {

    public CommentInfoAdapter(Activity activity, List<CommentInfoBean.CommentInfo> list) {
        super(list, activity, 1);
    }

    @Override
    public void onHolder(ViewHolder holder, CommentInfoBean.CommentInfo bean, int position) {
        holder.textViews.get(3).setVisibility(View.GONE);
        holder.textViews.get(4).setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
        ViewGroup.LayoutParams params = holder.textViews.get(4).getLayoutParams();
        params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        holder.textViews.get(4).setLayoutParams(params);
        if (bean != null) {
            holder.linearLayouts.get(0).setVisibility(View.VISIBLE);
            holder.textViews.get(2).setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
            if (position == list.size() - 1) {
                holder.linearLayouts.get(1).setVisibility(View.VISIBLE);
                holder.textViews.get(4).setText(LanguageUtil.getString(activity, R.string.CommentInfoActivity_full));
            } else {
                holder.linearLayouts.get(1).setVisibility(View.GONE);
            }
            MyGlide.GlideCicleHead(activity, bean.getAvatar(), holder.imageViews.get(0));
            if (bean.getIs_vip() == 1) {
                holder.imageViews.get(1).setVisibility(View.VISIBLE);
            } else {
                holder.imageViews.get(1).setVisibility(View.GONE);
            }
            holder.textViews.get(0).setText(bean.getNickname());
            holder.textViews.get(1).setText(bean.getTime());
            holder.textViews.get(2).setText(bean.getContent());
        } else {
            holder.linearLayouts.get(0).setVisibility(View.GONE);
            holder.textViews.get(2).setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
            if (list.size() == 0) {
                holder.linearLayouts.get(1).setVisibility(View.VISIBLE);
                holder.textViews.get(4).setText(LanguageUtil.getString(activity, R.string.app_no_comment_replay));
            }
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.activity_book_info_content_comment_item));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindViews({R.id.activity_Book_info_content_comment_layout, R.id.comment_bottom_layout})
        List<LinearLayout> linearLayouts;
        @BindViews({R.id.activity_Book_info_content_comment_item_avatar, R.id.activity_Book_info_content_comment_item_isvip})
        List<ImageView> imageViews;
        @BindViews({R.id.activity_Book_info_content_comment_item_nickname, R.id.activity_Book_info_content_comment_item_time,
                R.id.activity_Book_info_content_comment_item_content, R.id.activity_Book_info_content_comment_item_reply_info,
                R.id.item_comment_all})
        List<TextView> textViews;
        @BindView(R.id.public_list_line_id)
        View line;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
