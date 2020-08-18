package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AcquirePrivilegeItem;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

import butterknife.BindView;

public class RechargePrivilegeAdapter extends BaseRecAdapter<AcquirePrivilegeItem, RechargePrivilegeAdapter.ViewHolder> {

    public List<AcquirePrivilegeItem> list;

    public RechargePrivilegeAdapter(List<AcquirePrivilegeItem> list, Activity activity) {
        super(list, activity);
        this.list = list;
    }

    @Override
    public void onHolder(RechargePrivilegeAdapter.ViewHolder viewHolder, AcquirePrivilegeItem bean, int position) {
        viewHolder.itemAcquirePrivilegeTitle.setText(bean.label);
        MyGlide.GlideImageNoSize(activity, bean.icon, viewHolder.itemAcquirePrivilegeImg);
    }

    @Override
    public RechargePrivilegeAdapter.ViewHolder onCreateHolder() {
        return new RechargePrivilegeAdapter.ViewHolder(getViewByRes(R.layout.item_acquire_privilege));
    }

    class ViewHolder extends BaseRecViewHolder {
        @BindView(R.id.item_acquire_privilege_img)
        ImageView itemAcquirePrivilegeImg;
        @BindView(R.id.item_acquire_privilege_title)
        TextView itemAcquirePrivilegeTitle;

        ViewHolder(View view) {
            super(view);
        }
    }
}
