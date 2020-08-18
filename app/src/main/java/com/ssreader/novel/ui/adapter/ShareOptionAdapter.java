package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.BannerBottomItem;

import java.util.List;

import butterknife.BindView;

public class ShareOptionAdapter extends BaseRecAdapter<BannerBottomItem, ShareOptionAdapter.ViewHolder> {

    private OnShareOptionListener onShareOptionListener;
    private int itemWidth;

    public ShareOptionAdapter(List<BannerBottomItem> list, Activity activity) {
        super(list, activity);
    }

    public void setOnShareOptionListener(OnShareOptionListener onShareOptionListener) {
        this.onShareOptionListener = onShareOptionListener;
    }

    @Override
    public void onHolder(ViewHolder holder, BannerBottomItem bean, int position) {
        if (bean != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.linearLayout.getLayoutParams();
            params.width = itemWidth;
            holder.linearLayout.setLayoutParams(params);
            holder.textView.setText(bean.getTitle());
            holder.imageView.setImageResource(bean.getShare_icon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShareOptionListener != null) {
                        onShareOptionListener.onClick(bean.getAction());
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_dialog_share));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_share_layout)
        LinearLayout linearLayout;
        @BindView(R.id.item_share_image)
        ImageView imageView;
        @BindView(R.id.item_share_text)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setWidth(int width) {
        itemWidth = width;
    }

    public interface OnShareOptionListener {

        void onClick(String action);
    }
}
