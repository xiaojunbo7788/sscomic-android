package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.PayGoldDetail;

import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.MONTHTICKETHISTORY;

/**
 * Created by abc on 2017/4/28.
 */
public class GoldRecordAdapter extends BaseRecAdapter<PayGoldDetail, GoldRecordAdapter.ViewHolder> {
    public int OPTION;
    public GoldRecordAdapter(List<PayGoldDetail> optionBeenList, Activity activity,int OPTION) {
        super(optionBeenList, activity);
        this.OPTION = OPTION;
    }

    @Override
    public void onHolder(ViewHolder viewHolder, PayGoldDetail optionBeen, int position) {
      viewHolder.public_list_line_id.setVisibility((position==NoLinePosition)?View.GONE:View.VISIBLE);

        if(OPTION!=MONTHTICKETHISTORY){
            viewHolder.mItemPayGoldDetailArticle.setText(optionBeen.getArticle());
            viewHolder.mItemPayGoldDetailDate.setText(optionBeen.getDate());
            viewHolder.mItemPayGoldDetailDetail.setText(optionBeen.getDetail());
        }else{
            viewHolder.mItemPayGoldDetailArticle.setText(optionBeen.title);
            viewHolder.mItemPayGoldDetailDate.setText(optionBeen.time);
            viewHolder.mItemPayGoldDetailDetail.setText(optionBeen.desc);
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_pay_gold_detail));
    }

    class ViewHolder extends BaseRecViewHolder {
        @BindView(R.id.item_pay_gold_detail_article)
        TextView mItemPayGoldDetailArticle;
        @BindView(R.id.item_pay_gold_detail_date)
        TextView mItemPayGoldDetailDate;
        @BindView(R.id.item_pay_gold_detail_detail)
        TextView mItemPayGoldDetailDetail;
        @BindView(R.id.public_list_line_id)
        View public_list_line_id;

        ViewHolder(View view) {
            super(view);
        }
    }
}



