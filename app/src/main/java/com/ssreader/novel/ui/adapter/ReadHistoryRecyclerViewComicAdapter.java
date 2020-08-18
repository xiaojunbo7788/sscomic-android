package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.BaseReadHistory;
;
;
import com.ssreader.novel.ui.fragment.ReadHistoryFragment;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadHistoryRecyclerViewComicAdapter extends BaseRecAdapter {
    private List<BaseReadHistory> list;
    private ReadHistoryFragment.GetPosition getPosition;

    public ReadHistoryRecyclerViewComicAdapter(Activity activity, List<BaseReadHistory> list, ReadHistoryFragment.GetPosition getPosition) {
        super(list, activity);
        this.list = list;
        this.getPosition = getPosition;
    }

    @Override
    public void onHolder(BaseRecViewHolder holder1, Object bean, int position) {
        ViewHolder holder = (ViewHolder) holder1;
        final BaseReadHistory readHistoryBook = list.get(position);
        //判断是否设置了监听器

        //为ItemView设置监听器

        if (readHistoryBook.ad_type == 0) {
            holder.mRecyclerviewItemReadhistoryHorizontalScrollView.scrollTo(0,0);
            holder.mRecyclerviewItemReadhistoryHorizontalScrollView.setVisibility(View.VISIBLE);
            holder.mListAdViewLayout.setVisibility(View.GONE);
            holder.mRecyclerviewItemReadhistoryName.setText(readHistoryBook.getName());
            holder.mRecyclerviewItemReadhistoryDes.setText(readHistoryBook.chapter_title);
            holder.mRecyclerviewItemReadhistoryTime.setText(readHistoryBook.getLast_chapter_time()==null?"":readHistoryBook.getLast_chapter_time() + "  " + (readHistoryBook.getTotal_chapters()==null?"":String.format(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_total_chapter), readHistoryBook.getTotal_chapters())));
            MyGlide.GlideImageNoSize(activity,readHistoryBook.cover,holder.mRecyclerviewItemReadhistoryImg);
            holder.mRecyclerviewItemReadhistoryBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getPosition.getPosition(0, readHistoryBook, position);
                }
            });
            holder.mRecyclerviewItemReadhistoryGoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getPosition.getPosition(1, readHistoryBook, position);
                }
            });
            holder.mRecyclerviewItemReadhistoryDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getPosition.getPosition(2, readHistoryBook, position);
                }
            });

        } else {
            holder.mRecyclerviewItemReadhistoryHorizontalScrollView.setVisibility(View.GONE);
            holder.mListAdViewLayout.setVisibility(View.VISIBLE);
            readHistoryBook.setAd(activity, holder.mListAdViewLayout,1);

        }
    }

    @Override
    public BaseRecViewHolder onCreateHolder() {

        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_recyclerview_item_readhistory, null));

    }
    class ViewHolder extends BaseRecViewHolder{
        @BindView(R.id.recyclerview_item_readhistory_img)
        ImageView mRecyclerviewItemReadhistoryImg;
        @BindView(R.id.recyclerview_item_readhistory_name)
        TextView mRecyclerviewItemReadhistoryName;
        @BindView(R.id.recyclerview_item_readhistory_des)
        TextView mRecyclerviewItemReadhistoryDes;
        @BindView(R.id.recyclerview_item_readhistory_time)
        TextView mRecyclerviewItemReadhistoryTime;
        @BindView(R.id.recyclerview_item_readhistory_goon)
        Button mRecyclerviewItemReadhistoryGoon;
        @BindView(R.id.recyclerview_item_readhistory_book)
        LinearLayout mRecyclerviewItemReadhistoryBook;
        @BindView(R.id.recyclerview_item_readhistory_del)
        TextView mRecyclerviewItemReadhistoryDel;
        @BindView(R.id.recyclerview_item_readhistory_HorizontalScrollView)
        HorizontalScrollView mRecyclerviewItemReadhistoryHorizontalScrollView;
        @BindView(R.id.list_ad_view_img)
        ImageView mListAdViewImg;
        @BindView(R.id.list_ad_view_layout)
        FrameLayout mListAdViewLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRecyclerviewItemReadhistoryBook.getLayoutParams();
            layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
            mRecyclerviewItemReadhistoryBook.setLayoutParams(layoutParams);
        }
    }
}
