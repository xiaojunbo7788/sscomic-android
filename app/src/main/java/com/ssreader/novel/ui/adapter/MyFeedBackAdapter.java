package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AnswerFeedBackListBean;
import com.ssreader.novel.ui.activity.LookBigImageActivity;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.GridViewForScrollView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFeedBackAdapter extends BaseRecAdapter<AnswerFeedBackListBean,MyFeedBackAdapter.ViewHolder> {


    public MyFeedBackAdapter(Activity activity, List<AnswerFeedBackListBean> list) {
        super(list,activity);
    }

    @Override
    public void onHolder(ViewHolder viewHolder, AnswerFeedBackListBean bean, int position) {
        if (!TextUtils.isEmpty(bean.getContent())) {
            viewHolder.activityFeedBackContent.setText(bean.getContent());
        } else {
            viewHolder.activityFeedBackContent.setVisibility(View.GONE);
        }
        if (bean.getReply() != null && !bean.getReply().isEmpty()) {
            viewHolder.activityFeedBackReply.setText(bean.getReply());
            viewHolder.item_huifu.setVisibility(View.VISIBLE);
            viewHolder.activity_my_feed_back_re_view.setVisibility(View.VISIBLE);

        } else {
            viewHolder.activity_my_feed_back_re_view.setVisibility(View.GONE);

            viewHolder.activityFeedBackReply.setVisibility(View.GONE);
            viewHolder.item_huifu.setVisibility(View.GONE);
        }
        viewHolder.myFeedBackTime.setText(bean.getCreated_at());
        if (bean.getImages() != null && !bean.getImages().isEmpty()) {
            viewHolder.activityMyFeedBackPhoto.setVisibility(View.VISIBLE);
            FeedBackImaPhotoAdapter feedBackImaPhotoAdapter = new FeedBackImaPhotoAdapter(activity, bean.getImages());
            viewHolder.activityMyFeedBackPhoto.setAdapter(feedBackImaPhotoAdapter);
            viewHolder.activityMyFeedBackPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(activity, LookBigImageActivity.class);
                    intent.putExtra("click_position", position);
                    intent.putExtra("lookbigimgcontent", "");
                    intent.putExtra("snsShowPictures", (Serializable) bean.getImages());
                    activity.startActivity(intent);
                }
            });
            feedBackImaPhotoAdapter.notifyDataSetChanged();
        } else {
            viewHolder.activityMyFeedBackPhoto.setVisibility(View.GONE);

        }
    }


    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_my_feed_back));
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.my_feed_back_time)
        TextView myFeedBackTime;
        @BindView(R.id.activity_feed_back_content)
        TextView activityFeedBackContent;
        @BindView(R.id.activity_my_feed_back_photo)
        GridViewForScrollView activityMyFeedBackPhoto;
        @BindView(R.id.activity_feed_back_reply)
        TextView activityFeedBackReply;
        @BindView(R.id.item_huifu)
        TextView item_huifu;
        @BindView(R.id.activity_my_feed_back_re_view)
        View activity_my_feed_back_re_view;



        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
