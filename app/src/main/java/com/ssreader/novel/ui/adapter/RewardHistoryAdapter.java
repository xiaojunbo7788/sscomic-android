package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.PayGoldDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardHistoryAdapter extends BaseRecAdapter<PayGoldDetail,RewardHistoryAdapter.ViewHolder> {

    public RewardHistoryAdapter(List<PayGoldDetail> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public void onHolder(ViewHolder viewHolder, PayGoldDetail bean, int position) {
        viewHolder.itemRewardTitle.setText(bean.title);
        viewHolder.itemRewardMoney.setText(bean.desc);
        viewHolder.itemRewardTime.setText(bean.time);
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_reward_recording, null));
    }

    class ViewHolder extends BaseRecViewHolder {
        @BindView(R.id.item_reward_title)
        TextView itemRewardTitle;
        @BindView(R.id.item_reward_money)
        TextView itemRewardMoney;
        @BindView(R.id.item_reward_time)
        TextView itemRewardTime;
        @BindView(R.id.item_reward_details)
        TextView itemRewardDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
