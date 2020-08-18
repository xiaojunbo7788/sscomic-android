package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseTag;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreComicAdapter extends BaseListAdapter {
    private int style;//当前样式标识
    private int WIDTH, HEIGHT, height;

    public StoreComicAdapter(Activity activity, List<BaseBookComic> list, int style, int WIDTH, int HEIGHT) {
        super(activity, list);
        this.style = style;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        height = ImageUtil.dp2px(activity, 55);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_store_comic_style;
    }

    @Override
    public View getOwnView(int position, Object been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(convertView);

        BaseBookComic comic = (BaseBookComic) getItem(position);
        if ((style == 1 || style == 3) && !TextUtils.isEmpty(comic.horizontal_cover)) {
            MyGlide.GlideImage(activity, comic.horizontal_cover, viewHolder.liem_store_comic_style1_img, WIDTH, HEIGHT);
        } else {
            MyGlide.GlideImage(activity, comic.vertical_cover, viewHolder.liem_store_comic_style1_img, WIDTH, HEIGHT);
        }
        if (!TextUtils.isEmpty(comic.flag)) {
            viewHolder.liem_store_comic_style1_flag.setVisibility(View.VISIBLE);
            viewHolder.liem_store_comic_style1_flag.setText(comic.flag);
        } else {
            viewHolder.liem_store_comic_style1_flag.setVisibility(View.GONE);
        }
        viewHolder.liem_store_comic_style1_name.setText(comic.name);
        if (comic.description != null) {
            viewHolder.liem_store_comic_style1_description.setText(comic.description);
        } else if (comic.tag != null && !comic.tag.isEmpty()) {
            String str = "";
            for (BaseTag tag : comic.tag) {
                str += tag.getTab() + "  ";
            }
            viewHolder.liem_store_comic_style1_description.setText(str);
        } else {
            viewHolder.liem_store_comic_style1_description.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.liem_store_comic_style1_layout.getLayoutParams();
        layoutParams.height = HEIGHT + height;
        viewHolder.liem_store_comic_style1_layout.setLayoutParams(layoutParams);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.liem_store_comic_style1_layout)
        LinearLayout liem_store_comic_style1_layout;
        @BindView(R.id.liem_store_comic_style1_img)
        ImageView liem_store_comic_style1_img;

        @BindView(R.id.liem_store_comic_style1_flag)
        TextView liem_store_comic_style1_flag;
        @BindView(R.id.liem_store_comic_style1_name)
        TextView liem_store_comic_style1_name;
        @BindView(R.id.liem_store_comic_style1_description)
        TextView liem_store_comic_style1_description;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
