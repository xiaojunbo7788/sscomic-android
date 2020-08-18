package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.RankItem;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankAdapter extends BaseListAdapter {

    private List<RankItem> list;
    private int type;

    public RankAdapter(Activity activity, List<RankItem> list, int type) {
        super(activity, list);
        this.list = list;
        this.type = type;
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_store_rank_male;
    }

    @Override
    public View getOwnView(int position, Object been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.public_list_line_id.setVisibility((position==NoLinePosition)?View.GONE:View.VISIBLE);

        String cover1 = "cover1", cover2 = "cover2", cover3 = "cover3";
        if (list.get(position).getIcon() != null && !list.get(position).getIcon().isEmpty()) {
            if (list.get(position).getIcon().size() > 0) {
                cover1 = list.get(position).getIcon().get(0);
            }
            if (list.get(position).getIcon().size() > 1) {
                cover2 = list.get(position).getIcon().get(1);
            }
            if (list.get(position).getIcon().size() > 2) {
                cover3 = list.get(position).getIcon().get(2);
            }
        }
        MyGlide.GlideImageNoSize(activity, cover1, viewHolder.mItemStoreRankMaleImg1);
        MyGlide.GlideImageNoSize(activity, cover2, viewHolder.mItemStoreRankMaleImg2);
        MyGlide.GlideImageNoSize(activity, cover3, viewHolder.mItemStoreRankMaleImg3);
        if (type == Constant.AUDIO_CONSTANT) {
            viewHolder.audioMarkImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.audioMarkImage.setVisibility(View.GONE);
        }
        viewHolder.mItemStoreRankMaleText.setText(list.get(position).getTitle());
        viewHolder.mItemStoreRankMaleDescription.setText(list.get(position).getDesc());
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.item_store_rank_male_img_3)
        ImageView mItemStoreRankMaleImg3;
        @BindView(R.id.item_store_rank_male_img_2)
        ImageView mItemStoreRankMaleImg2;
        @BindView(R.id.item_store_rank_male_img_1)
        ImageView mItemStoreRankMaleImg1;
        @BindView(R.id.item_store_rank_male_audio_mark)
        ImageView audioMarkImage;
        @BindView(R.id.item_store_rank_male_text)
        TextView mItemStoreRankMaleText;
        @BindView(R.id.item_store_rank_male_description)
        TextView mItemStoreRankMaleDescription;

        @BindView(R.id.public_list_line_id)
        View public_list_line_id;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
