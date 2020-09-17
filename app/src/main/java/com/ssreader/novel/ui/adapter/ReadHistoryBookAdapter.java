package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseReadHistory;
import com.ssreader.novel.ui.fragment.ReadHistoryFragment;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;

public class ReadHistoryBookAdapter extends BaseRecAdapter<BaseReadHistory, ReadHistoryBookAdapter.ViewHolder> {

    private ReadHistoryFragment.GetPosition getPosition;
    private int productType;
    //是否喜欢进入
    private boolean isLike;

    public ReadHistoryBookAdapter(Activity activity, List<BaseReadHistory> list, ReadHistoryFragment.GetPosition getPosition, int productType) {
        super(list, activity);
        this.getPosition = getPosition;
        this.productType = productType;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    @Override
    public void onHolder(ReadHistoryBookAdapter.ViewHolder viewHolder, BaseReadHistory readHistory, int position) {
        if (readHistory.ad_type == 0) {
            viewHolder.mListAdViewLayout.setVisibility(View.GONE);
            viewHolder.mRecyclerviewItemReadhistoryHorizontalScrollView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = viewHolder.mRecyclerviewItemReadhistoryBook.getLayoutParams();
            layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
            viewHolder.mRecyclerviewItemReadhistoryBook.setLayoutParams(layoutParams);
            viewHolder.mRecyclerviewItemReadhistoryHorizontalScrollView.scrollTo(0, 0);
            viewHolder.mRecyclerviewItemReadhistoryName.setText(readHistory.getName());
            viewHolder.mRecyclerviewItemReadhistoryTime.setText(readHistory.getLast_chapter_time() == null ? "" : readHistory.getLast_chapter_time() + "  " + (readHistory.getTotal_chapters() == null ? "" : String.format(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_total_chapter), readHistory.getTotal_chapters())));
            if (readHistory.audio_id != 0 && Constant.AUDIO_CONSTANT == productType) {
                viewHolder.item_history_player_img.setVisibility(View.VISIBLE);
            } else {
                viewHolder.item_history_player_img.setVisibility(View.GONE);
            }
            // 设置继续阅读的样式
            viewHolder.mRecyclerviewItemReadhistoryGoon.setBackground(MyShape.setMyCustomizeShape(activity, 22, R.color.maincolor));
            if (productType == Constant.AUDIO_CONSTANT) {
                viewHolder.mRecyclerviewItemReadhistoryGoon.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_gone_audio));
                viewHolder.mRecyclerviewItemReadhistoryDes.setText(LanguageUtil.getString(activity, R.string.BookHistroy_last_listener) + readHistory.chapter_title);
            } else {
                viewHolder.mRecyclerviewItemReadhistoryGoon.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_goon_read));
                if (!isLike) {
                    viewHolder.mRecyclerviewItemReadhistoryDes.setText(LanguageUtil.getString(activity, R.string.BookHistroy_last_read) + readHistory.chapter_title);
                }
            }
            if (productType != Constant.COMIC_CONSTANT) {
                MyGlide.GlideImageNoSize(activity, readHistory.cover, viewHolder.mRecyclerviewItemReadhistoryImg);
            } else {
                MyGlide.GlideImageNoSize(activity, readHistory.vertical_cover, viewHolder.mRecyclerviewItemReadhistoryImg);
            }
            viewHolder.mRecyclerviewItemReadhistoryBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPosition.getPosition(0, readHistory, position, productType);
                }
            });
            viewHolder.mRecyclerviewItemReadhistoryGoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPosition.getPosition(1, readHistory, position, productType);
                }
            });
            viewHolder.mRecyclerviewItemReadhistoryDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPosition.getPosition(2, readHistory, position, productType);
                }
            });
        } else {
            viewHolder.mRecyclerviewItemReadhistoryHorizontalScrollView.setVisibility(View.GONE);
            viewHolder.mListAdViewLayout.setVisibility(View.VISIBLE);
            readHistory.setAd(activity, viewHolder.mListAdViewLayout, 1);
        }
        viewHolder.public_list_line_id.setVisibility((readHistory.ad_type!=0||position==NoLinePosition)?View.GONE:View.VISIBLE);

    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_recyclerview_item_readhistory));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.recyclerview_item_readhistory_img)
        ImageView mRecyclerviewItemReadhistoryImg;
        @BindView(R.id.recyclerview_item_readhistory_name)
        TextView mRecyclerviewItemReadhistoryName;
        @BindView(R.id.recyclerview_item_readhistory_des)
        TextView mRecyclerviewItemReadhistoryDes;
        @BindView(R.id.recyclerview_item_readhistory_time)
        TextView mRecyclerviewItemReadhistoryTime;
        @BindView(R.id.recyclerview_item_readhistory_goon)
        TextView mRecyclerviewItemReadhistoryGoon;
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
        @BindView(R.id.item_history_player_img)
        ImageView item_history_player_img;

        @BindView(R.id.public_list_line_id)
        View public_list_line_id;


        ViewHolder(View view) {
            super(view);
        }
    }
}
