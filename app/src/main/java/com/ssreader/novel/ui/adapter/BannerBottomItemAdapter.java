package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BannerBottomItem;
import com.ssreader.novel.ui.utils.ImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerBottomItemAdapter extends BaseListAdapter<BannerBottomItem> {

    private int productType;

    public BannerBottomItemAdapter(Activity activity, List<BannerBottomItem> list, int productType) {
        super(activity, list);
        this.productType = productType;
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_banner_bottom;
    }

    @Override
    public View getOwnView(int position, BannerBottomItem been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        ViewGroup.LayoutParams layoutParams = viewHolder.mItemStoreBannerBottomImg.getLayoutParams();
        if (productType == Constant.BOOK_CONSTANT || productType == Constant.AUDIO_CONSTANT) {
            layoutParams.width = layoutParams.height = ImageUtil.dp2px(activity, 40);
        }
        viewHolder.mItemStoreBannerBottomImg.setLayoutParams(layoutParams);
        Glide.with(activity).load(been.getIcon()).into(viewHolder.mItemStoreBannerBottomImg);
        viewHolder.mItemStoreBannerBottomText.setText(been.getTitle());
        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.item_store_banner_bottom_img)
        ImageView mItemStoreBannerBottomImg;
        @BindView(R.id.item_store_banner_bottom_text)
        TextView mItemStoreBannerBottomText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
