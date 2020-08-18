package com.ssreader.novel.ui.view.banner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ssreader.novel.R;

import com.ssreader.novel.model.PublicIntent;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.view.banner.holder.Holder;
import com.ssreader.novel.utils.ScreenSizeUtils;

import static com.ssreader.novel.constant.Constant.GETNotchHeight;

/**
 * 轮播图Holder
 */
public class HomeBannerHolderViewComic implements Holder {

    private ImageView item_store_entrance_comic_bg, item_store_entrance_comic_img;
    View item_store_entrance_comic_bgVIEW;
    Activity activity;
    int width, width2, height;

    @Override
    public View createView(Context context, int size) {
        activity = (Activity) context;
        View contentView = LayoutInflater.from(activity).inflate(R.layout.item_store_entrance_comic, null, false);
        item_store_entrance_comic_img = contentView.findViewById(R.id.item_store_entrance_comic_img);
        item_store_entrance_comic_bg = contentView.findViewById(R.id.item_store_entrance_comic_bg);
        item_store_entrance_comic_bgVIEW = contentView.findViewById(R.id.item_store_entrance_comic_bgVIEW);

        width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        width2 = width - ImageUtil.dp2px(activity, 20);
        height = width2 * 3 / 5;


        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item_store_entrance_comic_img.getLayoutParams();
        layoutParams.height = height;
        item_store_entrance_comic_img.setLayoutParams(layoutParams);
        return contentView;
    }

    @Override
    public void UpdateUI(Context context, int position, Object data) {
        if (data instanceof PublicIntent) {
            PublicIntent date = (PublicIntent) data;//"#4D"+date.getColor().substring(1)
            try {
                int[] colors = {0xF2FFFFFF, Color.parseColor("#E6"+date.getColor().substring(1))};
                GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
                item_store_entrance_comic_bgVIEW.setBackground(g);
            } catch (Exception e) {
            }
            MyGlide.GlideImage(activity, date.getImage(), item_store_entrance_comic_bg);
            MyGlide.GlideImageRoundedCornersNoSize(6, activity, date.getImage(), item_store_entrance_comic_img);
        }
    }
}
