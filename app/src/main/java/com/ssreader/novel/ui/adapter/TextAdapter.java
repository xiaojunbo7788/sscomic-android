package com.ssreader.novel.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import me.haowen.textbanner.adapter.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import com.ssreader.novel.R;
import com.ssreader.novel.model.BannerNoticeBean;
import com.ssreader.novel.ui.activity.WebViewActivity;

import java.util.List;

/**
 * 简单的数据适配器（TextView）
 */
public class TextAdapter extends BaseAdapter<BannerNoticeBean> {

    @LayoutRes
    private int mLayoutResId;

    /**
     * 简单的TextBanner适配器（只含有一个TextView）
     *
     * @param context     上下文
     * @param layoutResId 布局资源ID
     * @param data        字符串列表数据源
     */
    public TextAdapter(Context context, @LayoutRes int layoutResId, List<BannerNoticeBean> data) {
        super(context, data);
        this.mLayoutResId = layoutResId;
    }

    @Override
    public View onCreateView(@NonNull ViewGroup parent) {
        return mInflater.inflate(mLayoutResId, parent, false);
    }

    @Override
    public void onBindViewData(@NonNull View convertView, int position) {
        LinearLayout linearLayout = ((LinearLayout) convertView);
        TextView textView = linearLayout.findViewById(R.id.text_text1);
        BannerNoticeBean noticeBean = mData.get(position);
        textView.setText(noticeBean.getTitle());
        TextView rightBtn = linearLayout.findViewById(R.id.right_btn);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (noticeBean.getOpen_type().equals("1")) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, WebViewActivity.class);
                    intent.putExtra("url", noticeBean.getLink_url());
                    mContext.startActivity(intent);
                } else {
                    final Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(noticeBean.getLink_url()));
                    // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
                    // 官方解释 : Name of the component implementing an activity that can display the intent
                    mContext.startActivity(intent);
                }

            }
        });
    }

//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }
}