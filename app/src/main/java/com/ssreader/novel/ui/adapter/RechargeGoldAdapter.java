package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;

import java.util.List;

import butterknife.BindView;

public class RechargeGoldAdapter extends BaseRecAdapter<PayBeen.ItemsBean, RechargeGoldAdapter.ViewHolder> {

    public List<PayBeen.ItemsBean> list;

    public RechargeGoldAdapter(List<PayBeen.ItemsBean> list, Activity activity) {
        super(list, activity);
        this.list = list;
    }

    @Override
    public void onHolder(ViewHolder viewHolder, PayBeen.ItemsBean bean, int position) {
        if (bean.getTag() != null && !bean.getTag().isEmpty()) {
            viewHolder.itemPayGoldHeadLayout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 8), 0, ImageUtil.dp2px(activity, 8), 0, ContextCompat.getColor(activity, R.color.recharge_lable)));
            viewHolder.itemPayGoldHeadLayout.setVisibility(View.VISIBLE);
            viewHolder.itemPayGoldTv.setText(bean.getTag().get(0).getTab());
        } else {
            viewHolder.itemPayGoldHeadLayout.setVisibility(View.GONE);
        }
        SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(bean.getFat_price());
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(13, true);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        style.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(styleSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.itemPayGoldDetailDetail.setText(style);
        viewHolder.itemPayGoldDetailArticle.setText(bean.title);
        viewHolder.itemPayGoldDetailDate.setText(bean.getNote());

        if (bean.choose) {
            viewHolder.item_recharge_layout.setBackground(MyShape.setMyshapeStroke(activity, 2, 1,
                    ContextCompat.getColor(activity, R.color.recharge_line), ContextCompat.getColor(activity, R.color.recharge_bg)));
            viewHolder.itemPayGoldDetailArticle.setTextColor(ContextCompat.getColor(activity, R.color.recharge_movieticket_article));
            viewHolder.itemPayGoldDetailDate.setTextColor(ContextCompat.getColor(activity, R.color.recharge_movieticket_date));
        } else {
            viewHolder.item_recharge_layout.setBackground(MyShape.setMyshapeStroke(activity, 2, 1,
                    ContextCompat.getColor(activity, R.color.recharge_no_choose_line), 0));
            viewHolder.itemPayGoldDetailArticle.setTextColor(ContextCompat.getColor(activity, R.color.gray5));
            viewHolder.itemPayGoldDetailDate.setTextColor(ContextCompat.getColor(activity, R.color.gray3));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRechargeClick.onRecharge(position);
            }
        });
    }

    @Override
    public ViewHolder onCreateHolder() {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_pay_gold_detail2, null, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_pay_gold_detail_article)
        TextView itemPayGoldDetailArticle;
        @BindView(R.id.item_pay_gold_detail_date)
        TextView itemPayGoldDetailDate;
        @BindView(R.id.item_pay_gold_detail_detail)
        TextView itemPayGoldDetailDetail;
        @BindView(R.id.item_pay_gold_tv)
        TextView itemPayGoldTv;
        @BindView(R.id.item_pay_gold_head_layout)
        LinearLayout itemPayGoldHeadLayout;
        @BindView(R.id.item_recharge_layout)
        RelativeLayout item_recharge_layout;

        ViewHolder(View view) {
            super(view);
        }
    }

    onRechargeClick onRechargeClick;

    public void setOnRechargeClick(onRechargeClick onRechargeClick) {
        this.onRechargeClick = onRechargeClick;
    }

    public interface onRechargeClick {
        void onRecharge(int position);
    }
}
