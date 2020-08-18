package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AcquireBaoyuePayAdapter extends BaseListAdapter<PayBeen.ItemsBean> {

    public AcquireBaoyuePayAdapter(Activity activity, List list) {
        super(activity, list);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_acquire_pay;
    }

    @Override
    public View getOwnView(int position, PayBeen.ItemsBean been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);

        viewHolder.mItemAcquirePayTitle.setText(been.getTitle());
        if (mList.get(position).getTag() != null && mList.get(position).getTag().size() > 0) {
            viewHolder.mItemAcquirePayTitleTag.setVisibility(View.VISIBLE);
            viewHolder.mItemAcquirePayTitleTag.setText(been.getTag().get(0).getTab());
            viewHolder.mItemAcquirePayTitleTag.setBackground(MyShape.setMyshapeMineStroke(activity, 20, 19));
        } else {
            viewHolder.mItemAcquirePayTitleTag.setVisibility(View.GONE);
        }

        viewHolder.mItemAcquirePayNote.setText(been.getNote());
        SpannableString spannableString = new SpannableString(been.getFat_price());
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.7f);

        spannableString.setSpan(relativeSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewHolder.mItemAcquirePayPrice.setText(spannableString);

        if (!been.choose) {
            if (position == 0) {
                viewHolder.mItemAcquirePayChooseBg.setBackground(MyShape.setMyshape(activity, ImageUtil.dp2px(activity, 10), ImageUtil.dp2px(activity, 10), 0, 0, R.color.lightgraybg));
            } else if (position == mList.size() - 1) {
                viewHolder.mItemAcquirePayChooseBg.setBackground(MyShape.setMyshape(activity, 0, 0, ImageUtil.dp2px(activity, 10), ImageUtil.dp2px(activity, 10), R.color.lightgraybg));
            } else {
                viewHolder.mItemAcquirePayChooseBg.setBackground(MyShape.setMyshapeMineStroke(activity, 0, 22));
            }
        } else {
            if (position == 0) {
                viewHolder.mItemAcquirePayChooseBg.setBackground(MyShape.setMyshape(activity, ImageUtil.dp2px(activity, 10), ImageUtil.dp2px(activity, 10), 0, 0, R.color.fenzong));
            } else if (position == mList.size() - 1) {
                viewHolder.mItemAcquirePayChooseBg.setBackground(MyShape.setMyshape(activity, 0, 0, ImageUtil.dp2px(activity, 10), ImageUtil.dp2px(activity, 10), R.color.fenzong));
            } else {
                viewHolder.mItemAcquirePayChooseBg.setBackground(MyShape.setMyshapeMineStroke(activity, 0, 21));
            }
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item_acquire_pay_title)
        TextView mItemAcquirePayTitle;
        @BindView(R.id.item_acquire_pay_title_tag)
        TextView mItemAcquirePayTitleTag;
        @BindView(R.id.item_acquire_pay_note)
        TextView mItemAcquirePayNote;
        @BindView(R.id.item_acquire_pay_price)
        TextView mItemAcquirePayPrice;

        @BindView(R.id.item_acquire_pay_choose_bg)
        RelativeLayout mItemAcquirePayChooseBg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
