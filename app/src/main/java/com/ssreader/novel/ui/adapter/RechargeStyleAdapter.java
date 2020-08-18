package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by  on 2018/8/12.
 */
public class RechargeStyleAdapter extends BaseListAdapter<PayBeen.ItemsBean.PalChannelBean> {

    public RechargeStyleAdapter(Activity activity, List<PayBeen.ItemsBean.PalChannelBean> list) {
        super(activity, list);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_alipay_pay;
    }

    @Override
    public View getOwnView(int position, PayBeen.ItemsBean.PalChannelBean been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        PayBeen.ItemsBean.PalChannelBean rechargeItem = mList.get(position);

        if (rechargeItem.choose) {
            viewHolder.weixin_paytype_img.setImageResource(R.mipmap.pay_selected);
        } else {
            viewHolder.weixin_paytype_img.setImageResource(R.mipmap.pay_unselected);
        }
        MyGlide.GlideImageNoSize(activity, rechargeItem.getIcon(), viewHolder.weixin_pay_icon);
        viewHolder.weixin_paytype_textview.setText(rechargeItem.getTitle());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.weixin_pay_icon)
        ImageView weixin_pay_icon;
        @BindView(R.id.weixin_paytype_textview)
        TextView weixin_paytype_textview;
        @BindView(R.id.weixin_paytype_img)
        ImageView weixin_paytype_img;
        @BindView(R.id.weixin_pay_layout)
        View weixin_pay_layout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
