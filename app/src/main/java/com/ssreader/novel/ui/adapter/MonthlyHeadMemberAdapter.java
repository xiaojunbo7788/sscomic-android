package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.model.BaseStoreMemberCenterBean;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

/**
 * 会员中心界面顶部会员权益适配器
 */
public class MonthlyHeadMemberAdapter extends BaseAdapter {

    private Activity activity;
    private List<BaseStoreMemberCenterBean.MemberPrivilege> list;

    public MonthlyHeadMemberAdapter(Activity activity, List<BaseStoreMemberCenterBean.MemberPrivilege> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BaseStoreMemberCenterBean.MemberPrivilege getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_monthly_head_member, null);
        }
        ImageView imageView = convertView.findViewById(R.id.item_monthly_head_member_image);
        TextView textView = convertView.findViewById(R.id.item_monthly_head_member_dec);
        if (list.get(position) != null) {
            if (list.get(position).getIcon() != null) {
                MyGlide.GlideImageHeadNoSize(activity, list.get(position).getIcon(), imageView);
            }
            if (list.get(position).getLabel() != null) {
                textView.setText(list.get(position).getLabel());
            }
        }
        return convertView;
    }
}
