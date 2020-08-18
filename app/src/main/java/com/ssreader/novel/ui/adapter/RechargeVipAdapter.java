package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;

public class RechargeVipAdapter extends BaseRecAdapter<PayBeen.ItemsBean, RechargeVipAdapter.ViewHolder> {

    public List<PayBeen.ItemsBean> list;

    public RechargeVipAdapter(List<PayBeen.ItemsBean> list, Activity activity) {
        super(list, activity);
        this.list = list;
    }

    @Override
    public void onHolder(RechargeVipAdapter.ViewHolder viewHolder, PayBeen.ItemsBean bean, int position) {
        viewHolder.payItemDate.setText(bean.title);
        Spannable span = new SpannableString(bean.getFat_price());
        span.setSpan(new AbsoluteSizeSpan(ImageUtil.dp2px(activity, 15)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.payItemTitle.setText(span);
        if (bean.sub_title != null&&!bean.sub_title.isEmpty()) {
            viewHolder.payItemDesc.setText(bean.sub_title);
        }
        if (bean.getTag() != null && !bean.getTag().isEmpty()) {
            viewHolder.item_pay_head_tv.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 8), 0, ImageUtil.dp2px(activity, 8), 0, ContextCompat.getColor(activity, R.color.recharge_lable)));
            viewHolder.item_pay_head_tv.setText(bean.getTag().get(0).getTab());
        } else {
            viewHolder.item_pay_head_layout.setVisibility(View.GONE);
        }

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.itemRechargeLayout.getLayoutParams();
        layoutParams.width = (ScreenSizeUtils.getInstance(activity).getScreenWidth() / 3) - ImageUtil.dp2px(activity, 20);
        layoutParams.height = layoutParams.width * 120 / 105;
        viewHolder.itemRechargeLayout.setLayoutParams(layoutParams);

        if (bean.choose) {
            viewHolder.itemRechargeLayout.setBackground(MyShape.setMyshapeStroke(activity, 4, 1, ContextCompat.getColor(activity, R.color.recharge_line), ContextCompat.getColor(activity, R.color.recharge_bg)));
            viewHolder.payItemDate.setTextColor(ContextCompat.getColor(activity, R.color.recharge_movieticket_article));
            viewHolder.payItemDesc.setTextColor(ContextCompat.getColor(activity, R.color.recharge_movieticket_date));
        } else {
            viewHolder.itemRechargeLayout.setBackground(MyShape.setMyshapeStroke(activity, 4, 1, ContextCompat.getColor(activity, R.color.lightgray), 0));
            viewHolder.payItemDate.setTextColor(ContextCompat.getColor(activity, R.color.gray5));
            viewHolder.payItemDesc.setTextColor(ContextCompat.getColor(activity, R.color.gray));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRechargeClick.onRecharge(position);
            }
        });
    }

    @Override
    public RechargeVipAdapter.ViewHolder onCreateHolder() {
        return new RechargeVipAdapter.ViewHolder(getViewByRes(R.layout.item_recharge_vip));
    }

    class ViewHolder extends BaseRecViewHolder {
        @BindView(R.id.pay_item_date)
        TextView payItemDate;
        @BindView(R.id.pay_item_title)
        TextView payItemTitle;
        @BindView(R.id.pay_item_desc)
        TextView payItemDesc;
        @BindView(R.id.item_recharge_layout)
        LinearLayout itemRechargeLayout;
        @BindView(R.id.item_pay_head_tv)
        TextView item_pay_head_tv;
        @BindView(R.id.item_pay_head_layout)
        LinearLayout item_pay_head_layout;

        ViewHolder(View view) {
            super(view);
        }
    }

    public onRechargeClick onRechargeClick;

    public void setOnRechargeClick(RechargeVipAdapter.onRechargeClick onRechargeClick) {
        this.onRechargeClick = onRechargeClick;
    }

    public interface onRechargeClick {
        void onRecharge(int position);
    }
}
