package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

import butterknife.BindView;

public class RechargeChannelAdapter extends BaseRecAdapter<PayBeen.ItemsBean.PalChannelBean, RechargeChannelAdapter.ViewHolder> {

    public List<PayBeen.ItemsBean.PalChannelBean> list;

    public RechargeChannelAdapter(List<PayBeen.ItemsBean.PalChannelBean> list, Activity activity) {
        super(list, activity);
        this.list = list;
    }

    @Override
    public void onHolder(RechargeChannelAdapter.ViewHolder viewHolder, PayBeen.ItemsBean.PalChannelBean bean, int position) {
        MyGlide.GlideImageHeadNoSize(activity,bean.getIcon(),viewHolder.weixinPayIcon);
        viewHolder.weixinPaytypeTextview.setText(bean.getTitle());
        if (bean.choose) {
            viewHolder.weixinPaytypeImg.setImageResource(R.mipmap.pay_channel_success);
        }else {
            viewHolder.weixinPaytypeImg.setImageResource(R.mipmap.pay_channel_failure);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRechargeClick.onRecharge(position);
            }
        });
    }

    @Override
    public RechargeChannelAdapter.ViewHolder onCreateHolder() {
        return new RechargeChannelAdapter.ViewHolder(getViewByRes(R.layout.item_alipay_pay));
    }

    public class ViewHolder extends BaseRecViewHolder {
        @BindView(R.id.weixin_pay_icon)
        ImageView weixinPayIcon;
        @BindView(R.id.weixin_paytype_textview)
        TextView weixinPaytypeTextview;
        @BindView(R.id.weixin_paytype_img)
        ImageView weixinPaytypeImg;
        @BindView(R.id.weixin_pay_layout)
        RelativeLayout weixinPayLayout;

        ViewHolder(View view) {
            super(view);
        }
    }
    public onRechargeClick onRechargeClick;

    public void setOnRechargeClick(onRechargeClick onRechargeClick) {
        this.onRechargeClick = onRechargeClick;
    }

    public interface onRechargeClick{
        void onRecharge(int position);
    }
}
