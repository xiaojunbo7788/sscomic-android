package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.FeedBackAnswerListBean;
import com.ssreader.novel.model.FeedBackAnswerNameBean;
import com.ssreader.novel.ui.utils.MyToash;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedBackAnswerAdapter extends BaseListAdapter {

    private List<FeedBackAnswerNameBean> list;
    private Activity activity;
    private FeedBackAnswerListAdapter.OnScrollListener onScrollListener;

    public FeedBackAnswerAdapter(Activity activity, List list) {
        super(activity, list);
        this.list = list;
        this.activity = activity;
    }

    public void setOnScrollListener(FeedBackAnswerListAdapter.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_feed_face_answer_list;
    }

    @Override
    public View getOwnView(int position, Object been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(convertView);
        viewHolder.itemFeedFaceAnswerTitle.setText(list.get(position).name);
        List<FeedBackAnswerListBean> listAnswer = list.get(position).getList();
        viewHolder.itemFeedFaceAnswerRcy.setLayoutManager(new LinearLayoutManager(activity));
        FeedBackAnswerListAdapter feedBackAnswerListAdapter = new FeedBackAnswerListAdapter(
                listAnswer, activity, (position == list.size() - 1));
        viewHolder.itemFeedFaceAnswerRcy.setAdapter(feedBackAnswerListAdapter);
        feedBackAnswerListAdapter.setOnScrollListener(new FeedBackAnswerListAdapter.OnScrollListener() {
            @Override
            public void onScroll() {
                if (onScrollListener != null) {
                    onScrollListener.onScroll();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.item_feed_face_answer_title)
        TextView itemFeedFaceAnswerTitle;
        @BindView(R.id.item_feed_face_answer_rcy)
        RecyclerView itemFeedFaceAnswerRcy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
