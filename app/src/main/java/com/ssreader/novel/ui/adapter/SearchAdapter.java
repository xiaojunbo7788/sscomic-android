package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.utils.ScreenSizeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;

public class SearchAdapter extends BaseListAdapter<BaseBookComic> {

    private int productType;
    private int WIDTH, HEIGHT, H20, H33;

    public SearchAdapter(Activity activity, int productType, List<BaseBookComic> list) {
        super(activity, list);
        this.productType = productType;
        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        H20 = ImageUtil.dp2px(activity, 40);
        H33 = ImageUtil.dp2px(activity, 28);
        WIDTH = (WIDTH - H20) / 3;
        HEIGHT = (int) (((float) WIDTH * 4f / 3f));
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_store_label_male_vertical;
    }

    @Override
    public View getOwnView(int position, BaseBookComic been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        LinearLayout.LayoutParams layoutParamss = (LinearLayout.LayoutParams) viewHolder.mItemStoreLabelMaleVerticalLayout.getLayoutParams();
        layoutParamss.width = WIDTH;
        layoutParamss.height = HEIGHT + H33;
        viewHolder.mItemStoreLabelMaleVerticalLayout.setLayoutParams(layoutParamss);

        ViewGroup.LayoutParams layoutParams = viewHolder.mItemStoreLabelMaleVerticalImg.getLayoutParams();
        layoutParams.width = WIDTH;
        layoutParams.height = HEIGHT;
        viewHolder.mItemStoreLabelMaleVerticalImg.setLayoutParams(layoutParams);
        viewHolder.mItemStoreLabelMaleVerticalImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        MyGlide.GlideImageNoSize(activity, been.cover, viewHolder.mItemStoreLabelMaleVerticalImg);

        if (been.audio_id != 0 && productType == Constant.AUDIO_CONSTANT) {
            viewHolder.itme_store_label_male_horizontal_palyer.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHolder.itme_store_label_male_horizontal_palyer.getLayoutParams();
            params.height = params.width = (int) (HEIGHT * 0.15);
            params.leftMargin = params.bottomMargin = (int) (HEIGHT * 0.05);
            viewHolder.itme_store_label_male_horizontal_palyer.setLayoutParams(params);
        } else {
            viewHolder.itme_store_label_male_horizontal_palyer.setVisibility(View.GONE);
        }

        viewHolder.mItemStoreLabelMaleVerticalText.setText(been.getName());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item_store_label_male_vertical_img)
        ImageView mItemStoreLabelMaleVerticalImg;
        @BindView(R.id.item_store_label_comic_flag_text)
        TextView mItemStoreLabelComicFlagText;
        @BindView(R.id.item_store_label_comic_flag_layout)
        FrameLayout mItemStoreLabelComicFlagLayout;
        @BindView(R.id.shelfitem_img_container)
        ShadowLayout mShelfitemImgContainer;
        @BindView(R.id.item_store_label_male_vertical_text)
        TextView mItemStoreLabelMaleVerticalText;
        @BindView(R.id.item_store_label_male_vertical_text2)
        TextView mItemStoreLabelMaleVerticalText2;
        @BindView(R.id.item_store_label_male_vertical_layout)
        LinearLayout mItemStoreLabelMaleVerticalLayout;
        @BindView(R.id.itme_store_label_male_horizontal_palyer)
        ImageView itme_store_label_male_horizontal_palyer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
