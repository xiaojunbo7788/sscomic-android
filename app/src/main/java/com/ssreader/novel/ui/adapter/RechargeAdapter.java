package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.RotateTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by  on 2018/8/12.
 */
public class RechargeAdapter extends BaseListAdapter<PayBeen.ItemsBean> {
    ViewHolder viewHolder;

    public RechargeAdapter(Activity activity, List<PayBeen.ItemsBean> list) {
        super(activity, list);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_recharge_new;
    }

    @Override
    public View getOwnView(int position, PayBeen.ItemsBean been, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder(convertView);
        if (been.choose) {
            if(position==0){
                viewHolder.item_recharge_layout.setBackground(MyShape.setMyshape(activity,ImageUtil.dp2px(activity,10),ImageUtil.dp2px(activity,10),0,0,R.color.fenzong));
            }else if(position==mList.size()-1){
                viewHolder.item_recharge_layout.setBackground(MyShape.setMyshape(activity,0,0,ImageUtil.dp2px(activity,10),ImageUtil.dp2px(activity,10),R.color.fenzong));
            }else{
                viewHolder.item_recharge_layout.setBackground(MyShape.setMyshapeMineStroke(activity,0,21));
            }
        } else {
            if(position==0){
                viewHolder.item_recharge_layout.setBackground(MyShape.setMyshape(activity,ImageUtil.dp2px(activity,10),ImageUtil.dp2px(activity,10),0,0,R.color.lightgraybg));
            }else if(position==mList.size()-1){
                viewHolder.item_recharge_layout.setBackground(MyShape.setMyshape(activity,0,0,ImageUtil.dp2px(activity,10),ImageUtil.dp2px(activity,10),R.color.lightgraybg));
            }else {
                viewHolder.item_recharge_layout.setBackground(MyShape.setMyshapeMineStroke(activity,0,22));
            }
        }
        viewHolder.item_recharge_price.setText(mList.get(position).getTitle());
        viewHolder.item_recharge_title.setText(mList.get(position).getNote());
        if(mList.get(position).getTag()!=null&&!mList.get(position).getTag().isEmpty()){
            viewHolder.item_acquire_pay_title_flag.setText(mList.get(position).getTag().get(0).getTab());
        }else{
            viewHolder.item_acquire_pay_title_flag.setVisibility(View.GONE);
        }
        viewHolder.item_acquire_pay_title_flag.setBackground(MyShape.setMyshapeMineStroke(activity,20,19));
        SpannableString spannableString = new SpannableString(mList.get(position).getFat_price());
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.7f);
        spannableString.setSpan(relativeSizeSpan,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewHolder.item_recharge_money.setText(spannableString);
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.item_recharge_money)
        TextView item_recharge_money;
        @BindView(R.id.item_recharge_price)
        TextView item_recharge_price;
        @BindView(R.id.item_recharge_title)
        TextView item_recharge_title;
        @BindView(R.id.item_recharge_note)
        TextView item_recharge_note;
        @BindView(R.id.item_recharge_flag)
        RotateTextView item_recharge_flag;
        @BindView(R.id.item_recharge_layout)
        RelativeLayout item_recharge_layout;
        @BindView(R.id.item_acquire_pay_title_flag)
        TextView item_acquire_pay_title_flag;

        @BindView(R.id.item_recharge_bg_layout)
                RelativeLayout item_recharge_bg_layout;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
