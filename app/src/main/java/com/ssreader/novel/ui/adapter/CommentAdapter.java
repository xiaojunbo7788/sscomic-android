package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * 作品评论列表的adapter
 */
public class CommentAdapter extends BaseRecAdapter<Comment, CommentAdapter.ViewHolder> {

    public int total_count;
    private boolean IsNoPriverComment;

    public CommentAdapter(Activity context, List<Comment> list, SCOnItemClickListener scOnItemClickListener) {
        super(list, context, scOnItemClickListener);
    }

    public CommentAdapter(boolean i, Activity context, List<Comment> list, SCOnItemClickListener scOnItemClickListener) {
        super(list, context, scOnItemClickListener);
        IsNoPriverComment = true;
    }

    @Override
    public void onHolder(ViewHolder viewHolder, Comment bean, int position) {
        if (bean != null && bean.comment_id != null) {
            viewHolder.public_list_line_id.setVisibility((position == NoLinePosition) ? View.GONE : View.VISIBLE);
            viewHolder.linearLayouts.get(1).setVisibility(View.GONE);
            viewHolder.linearLayouts.get(0).setVisibility(View.VISIBLE);
            viewHolder.content.setVisibility(View.VISIBLE);
            MyGlide.GlideCicleHead(activity, bean.getAvatar(), viewHolder.imageView);
            viewHolder.content.setText(bean.getContent());
            viewHolder.replay.setText(bean.getReply_info());
            viewHolder.replay.setVisibility(TextUtils.isEmpty(bean.getReply_info()) ? View.GONE : View.VISIBLE);
            viewHolder.nickname.setText(bean.getNickname());
            if (bean.getIs_vip() == 1) {
                viewHolder.isVip.setVisibility(View.VISIBLE);
            } else {
                viewHolder.isVip.setVisibility(View.GONE);
            }
            viewHolder.time.setText(bean.getTime());
            if (!IsNoPriverComment) {
                Comment commentT = list.get(list.size() - 1);
                boolean flag;
                if (commentT.comment_id == null) {
                    flag = position == list.size() - 2;
                } else flag = position == list.size() - 1;
                if (flag) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.public_list_line_id.getLayoutParams();
                    layoutParams.leftMargin = ImageUtil.dp2px(activity, 10);
                    viewHolder.public_list_line_id.setLayoutParams(layoutParams);
                }else {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.public_list_line_id.getLayoutParams();
                    layoutParams.leftMargin = ImageUtil.dp2px(activity, 45);
                    viewHolder.public_list_line_id.setLayoutParams(layoutParams);
                }
            }

        } else {
            viewHolder.linearLayouts.get(0).setVisibility(View.GONE);
            viewHolder.replay.setVisibility(View.GONE);
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.public_list_line_id.setVisibility(View.GONE);
            viewHolder.linearLayouts.get(1).setVisibility(View.VISIBLE);
            if (total_count == 0) {
                viewHolder.audio_look_all_comment.setText(LanguageUtil.getString(activity, R.string.CommentListActivity_no_pinglun));
            } else {
                viewHolder.audio_look_all_comment.setText(String.format(LanguageUtil.getString(activity, R.string.CommentListActivity_lookpinglun),
                        total_count >= 1000 ? "999+" : total_count));
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
        @BindView(R.id.activity_Book_info_content_comment_item_avatar)
        ImageView imageView;
        @BindView(R.id.activity_Book_info_content_comment_item_content)
        TextView content;
        @BindView(R.id.activity_Book_info_content_comment_item_reply_info)
        TextView replay;
        @BindView(R.id.activity_Book_info_content_comment_item_nickname)
        TextView nickname;
        @BindView(R.id.activity_Book_info_content_comment_item_time)
        TextView time;
        @BindView(R.id.activity_Book_info_content_comment_item_isvip)
        ImageView isVip;
        @BindView(R.id.item_comment_all)
        TextView audio_look_all_comment;

        @BindView(R.id.public_list_line_id)
        View public_list_line_id;

        public ViewHolder(View itemView) {
            super(itemView);
            audio_look_all_comment.setBackground(MyShape.setMyshapeStroke2(activity,
                    20, 2, ContextCompat.getColor(activity, R.color.maincolor), 0));
        }
    }
}
