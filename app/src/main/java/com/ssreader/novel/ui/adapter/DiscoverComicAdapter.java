package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseTag;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;

public class DiscoverComicAdapter extends BaseRecAdapter<BaseBookComic, DiscoverComicAdapter.ViewHolder> {

    private int DP10;

    public DiscoverComicAdapter(List<BaseBookComic> list, Activity activity) {
        super(list, activity);
        DP10 = ImageUtil.dp2px(activity, 10);
    }

    @Override
    public void onHolder(ViewHolder viewHolder, BaseBookComic comic, int position) {
        int width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 20);
        int height;
        if (comic.ad_type == 0) {
            height = width * 4 / 7;
            viewHolder.list_ad_view_layout.setVisibility(View.GONE);
            viewHolder.item_discovery_comic_img.setVisibility(View.VISIBLE);
            viewHolder.item_discovery_comic_title.setVisibility(View.VISIBLE);
            viewHolder.item_discovery_comic_flag.setVisibility(View.VISIBLE);
            viewHolder.item_discovery_comic_tag.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams layoutParams1 = viewHolder.list_ad_view_noAD.getLayoutParams();
            layoutParams1.width = width;
            viewHolder.list_ad_view_noAD.setLayoutParams(layoutParams1);

            ViewGroup.LayoutParams layoutParams2 = viewHolder.item_discovery_comic_img.getLayoutParams();
            layoutParams2.width = width;
            layoutParams2.height = height;
            viewHolder.item_discovery_comic_img.setLayoutParams(layoutParams2);

            MyGlide.GlideImageRoundedCornersNoSize(8, activity, comic.cover, viewHolder.item_discovery_comic_img);
            viewHolder.item_discovery_comic_title.setText(comic.title);
            viewHolder.item_discovery_comic_flag.setText(comic.flag);
            viewHolder.item_discovery_comic_tag.removeAllViews();
            if (comic.tag != null) {
                for (BaseTag tag : comic.tag) {
                    TextView textView = new TextView(activity);
                    textView.setText(tag.getTab());
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                    textView.setLines(1);
                    textView.setPadding(10, 5, 10, 5);
                    textView.setTextColor(Color.parseColor(tag.getColor()));//resources.getColor(R.color.comic_info_tag_text)
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(10);
                    drawable.setColor(Color.parseColor("#1A" + tag.getColor().substring(1)));
                    textView.setBackground(drawable);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = DP10;
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    viewHolder.item_discovery_comic_tag.addView(textView, layoutParams);
                }
            }
        } else {
            viewHolder.item_discovery_comic_img.setVisibility(View.GONE);
            viewHolder.item_discovery_comic_flag.setVisibility(View.GONE);
            viewHolder.item_discovery_comic_tag.setVisibility(View.GONE);
            if (comic.ad_type == 1) {
                if (!TextUtils.isEmpty(comic.ad_title)) {
                    viewHolder.item_discovery_comic_title.setVisibility(View.VISIBLE);
                    viewHolder.item_discovery_comic_title.setText(comic.ad_title);
                } else {
                    viewHolder.item_discovery_comic_title.setVisibility(View.GONE);
                }
            } else {
                viewHolder.item_discovery_comic_title.setVisibility(View.GONE);
            }
            viewHolder.list_ad_view_layout.setVisibility(View.VISIBLE);
            comic.setAd(activity, viewHolder.list_ad_view_layout, 5);

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ComicInfoActivity.class);
                intent.putExtra("comic_id", comic.comic_id);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_discovery_comci, true));
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_discovery_comic_img)
        public ImageView item_discovery_comic_img;
        @BindView(R.id.item_discovery_comic_title)
        public TextView item_discovery_comic_title;
        @BindView(R.id.item_discovery_comic_flag)
        public TextView item_discovery_comic_flag;
        @BindView(R.id.item_discovery_comic_tag)
        public LinearLayout item_discovery_comic_tag;
        @BindView(R.id.list_ad_view_layout)
        FrameLayout list_ad_view_layout;
        @BindView(R.id.list_ad_view_img)
        ImageView list_ad_view_img;
        @BindView(R.id.discovery_comic_layout)
        LinearLayout list_ad_view_noAD;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
