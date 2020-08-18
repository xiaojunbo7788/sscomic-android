package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.AcquirePrivilegeItem;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AcquireBaoyuePrivilegeAdapter extends BaseListAdapter<AcquirePrivilegeItem> {

    public AcquireBaoyuePrivilegeAdapter(Activity activity, List list) {
        super(activity, list);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_acquire_privilege;
    }

    @Override
    public View getOwnView(int position, AcquirePrivilegeItem been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.mItemAcquirePrivilegeTitle.setText(been.getLabel());
        //MyGlide.GlideImageNoSize(activity,been.getIcon(),viewHolder.mItemAcquirePrivilegeImg);
        viewHolder.mItemAcquirePrivilegeImg.setImageResource(been.getLocalIcon());
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.item_acquire_privilege_img)
        ImageView mItemAcquirePrivilegeImg;
        @BindView(R.id.item_acquire_privilege_title)
        TextView mItemAcquirePrivilegeTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
