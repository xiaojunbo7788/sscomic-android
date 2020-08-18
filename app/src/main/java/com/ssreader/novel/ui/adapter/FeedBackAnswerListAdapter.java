package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.FeedBackAnswerListBean;

import java.util.List;

import butterknife.BindView;

public class FeedBackAnswerListAdapter extends RecyclerView.Adapter {

    private List<FeedBackAnswerListBean> list;
    private Activity activity;
    private boolean isEnd;
    private OnScrollListener onScrollListener;

    public FeedBackAnswerListAdapter(List<FeedBackAnswerListBean> list, Activity activity, boolean isEnd) {
        this.list = list;
        this.activity = activity;
        this.isEnd = isEnd;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_feed_back_answer_caption, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.activityFeedBackTitle.setText("Q："+list.get(position).title);
        viewHolder.activityFeedBackAnswer.setText("A："+list.get(position).answer);
        viewHolder.activityFeedBackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isClick) {
                    list.get(position).setClick(false);
                    viewHolder.activityFeedBackAnswer.setVisibility(View.VISIBLE);
                    if (isEnd && position == list.size() - 1 && onScrollListener != null) {
                        // 最后一个问题, 控制滑动到底部
                        onScrollListener.onScroll();
                    }
                } else {
                    list.get(position).setClick(true);
                    viewHolder.activityFeedBackAnswer.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.activity_feed_back_title)
        TextView activityFeedBackTitle;
        @BindView(R.id.activity_feed_back_answer)
        TextView activityFeedBackAnswer;

        ViewHolder(View view) {
            super(view);
        }
    }

    public interface OnScrollListener{

        void onScroll();
    }
}
