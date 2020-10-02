package com.ssreader.novel.ui.activity.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ssreader.novel.R;
import com.ssreader.novel.model.ShareBean;
import com.ssreader.novel.ui.utils.GlideCacheUtil;
import com.ssreader.novel.ui.utils.MyGlide;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InviteAdapter extends BaseQuickAdapter<ShareBean.InviteUserItem, BaseViewHolder> {

    public InviteAdapter(int layoutResId, @Nullable List<ShareBean.InviteUserItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ShareBean.InviteUserItem inviteUserItem) {
        TextView nameView = holder.getView(R.id.user_name);
        ImageView imageView = holder.getView(R.id.user_head);
        TextView timeView = holder.getView(R.id.user_time);
        nameView.setText(inviteUserItem.getNickname());
        timeView.setText(inviteUserItem.getCreated_at());
        MyGlide.GlideImageHeadNoSize((Activity) getContext(),inviteUserItem.getUserHead(),imageView);
    }
}
