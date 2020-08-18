package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.ui.utils.MyToash;

import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.AgainTime;

public class MineListAdapter extends BaseRecAdapter<MineModel, MineListAdapter.ViewHolder> {

    private OnClickMineItemListener onClickMineItemListener;
    private boolean isAboutUs;
    private long ClickTime;

    public MineListAdapter(boolean isLocal, Activity activity, List<MineModel> list) {
        this(activity, list);
    }

    public MineListAdapter(Activity activity, List<MineModel> list) {
        super(list, activity);
    }

    public MineListAdapter(Activity activity, List<MineModel> list, boolean isAboutUs) {
        super(list, activity);
        this.isAboutUs = isAboutUs;
    }

    public void setOnClickMineItemListener(OnClickMineItemListener onClickMineItemListener) {
        this.onClickMineItemListener = onClickMineItemListener;
    }

    @Override
    public void onHolder(ViewHolder holder, MineModel bean, int position) {
        if (bean != null) {
            if (!isAboutUs) {
                holder.icon.setVisibility(View.VISIBLE);
                if (bean.getIcon() != null && !TextUtils.isEmpty(bean.getIcon())) {
                    Glide.with(activity).load(bean.getIcon()).into(holder.icon);
                }
                if (bean.getIconResources() != 0) {
                    holder.icon.setImageResource(bean.getIconResources());
                }
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            holder.title.setText(bean.getTitle());
            if (bean.getTitle_color() != null && !TextUtils.isEmpty(bean.getTitle_color())) {
                holder.title.setTextColor(Color.parseColor(bean.getTitle_color()));
            }
            if (bean.getDesc() != null && !bean.getAction().equals("url")) {
                holder.desc.setText(bean.getDesc());
            }
            if (bean.getDesc_color() != null && !TextUtils.isEmpty(bean.getDesc_color())) {
                holder.desc.setTextColor(Color.parseColor(bean.getDesc_color()));
            }
            if (position == list.size() - 1) {
                holder.bottomLine.setVisibility(View.GONE);
                holder.bottomBg.setVisibility(View.GONE);
            } else {
                if (bean.isBottomLine()) {
                    holder.bottomLine.setVisibility(View.GONE);
                    holder.bottomBg.setVisibility(View.VISIBLE);
                } else {
                    holder.bottomBg.setVisibility(View.GONE);
                    holder.bottomLine.setVisibility(View.VISIBLE);
                }
            }
            if (bean.getIs_click() == 1) {
                holder.intoImage.setVisibility(View.VISIBLE);
            } else {
                holder.intoImage.setVisibility(View.GONE);
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long ClickTimeNew = System.currentTimeMillis();
                    if (ClickTimeNew - ClickTime > AgainTime) {
                        ClickTime = ClickTimeNew;
                        if (!isAboutUs) {
                            bean.mineOption(activity);
                        } else {
                            onClickMineItemListener.onClickItem(bean);
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_minelist));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.mine_top_layout)
        LinearLayout linearLayout;
        @BindView(R.id.item_mine_bottom_line)
        View bottomLine;
        @BindView(R.id.item_mine_bottom_bg)
        View bottomBg;
        @BindView(R.id.item_mine_icon)
        ImageView icon;
        @BindView(R.id.item_mine_title)
        TextView title;
        @BindView(R.id.item_mine_desc)
        TextView desc;
        @BindView(R.id.item_mine_into_image)
        ImageView intoImage;

        public ViewHolder(View view) {
            super(view);
        }
    }

    public interface OnClickMineItemListener {

        void onClickItem(MineModel mineModel);
    }
}
