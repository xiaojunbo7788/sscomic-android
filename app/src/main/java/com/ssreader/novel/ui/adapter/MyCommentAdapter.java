package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * 我的评论的适配器
 */
public class MyCommentAdapter extends BaseRecAdapter<Comment, MyCommentAdapter.ViewHolder> {

    private OnCommentListener onCommentListener;

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }

    public MyCommentAdapter(Activity activity, List<Comment> list) {
        super(list, activity);
    }

    @Override
    public void onHolder(ViewHolder holder, Comment bean, int position) {
        if (bean != null) {
            MyGlide.GlideCicleHead(activity, bean.getAvatar(), holder.imageViewList.get(0));
            if (bean.getIs_vip() == 1) {
                holder.imageViewList.get(1).setVisibility(View.VISIBLE);
            } else {
                holder.imageViewList.get(1).setVisibility(View.GONE);
            }
            holder.textViewList.get(0).setText(bean.getNickname());
            holder.textViewList.get(1).setText(bean.getTime());
            holder.textViewList.get(2).setText(bean.getContent());
            holder.textViewList.get(3).setText(bean.getName_title());
            holder.textViewList.get(4).setText(String.format(LanguageUtil.getString(activity,
                    R.string.CommentInfoActivity_replayNum), bean.getReply_num()));
            if (TextUtils.isEmpty(bean.getReply_info())) {
                holder.replyInfo.setVisibility(View.GONE);
            } else {
                holder.replyInfo.setVisibility(View.VISIBLE);
                holder.replyInfo.setText(bean.getReply_info());
            }
            holder.commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentListener != null) {
                        onCommentListener.onClick(bean);
                    }
                }
            });
            holder.public_list_line_id.setVisibility((position==NoLinePosition)?View.GONE:View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_my_comment));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_my_comment_layout)
        LinearLayout commentLayout;
        @BindViews({R.id.item_my_comment_head_image, R.id.item_my_comment_vip_image})
        List<ImageView> imageViewList;
        @BindViews({R.id.item_my_comment_name, R.id.item_my_comment_time, R.id.item_my_comment_context,
                R.id.item_my_comment_chapter_name, R.id.item_my_comment_reply_num})
        List<TextView> textViewList;
               @BindView(R.id.item_my_comment_reply_info)
        TextView replyInfo;

        @BindView(R.id.public_list_line_id)
        View public_list_line_id;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnCommentListener {

        void onClick(Comment bean);
    }
}
