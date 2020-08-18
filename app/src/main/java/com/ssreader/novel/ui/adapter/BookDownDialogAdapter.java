package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.LineSpinFadeLoaderIndicator;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class BookDownDialogAdapter extends BaseListAdapter<Downoption> {

    public BookDownDialogAdapter(Activity activity, List<Downoption> list) {
        super(activity, list);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_dialog_downadapter;
    }

    @Override
    public View getOwnView(int index, Downoption been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.loadingIndicatorView.setIndicator(new LineSpinFadeLoaderIndicator());
        if (been.isDowning) {
            viewHolder.loadingIndicatorView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.loadingIndicatorView.setVisibility(View.GONE);
        }
        viewHolder.textViews.get(0).setText(been.label);
        viewHolder.textViews.get(1).setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5), "#FF5722"));
        if (!TextUtils.isEmpty(been.tag)) {
            viewHolder.textViews.get(1).setVisibility(View.VISIBLE);
            viewHolder.textViews.get(1).setText(been.tag);
        } else {
            viewHolder.textViews.get(1).setVisibility(View.GONE);
        }
        if (been.isdown) {
            viewHolder.textViews.get(2).setVisibility(View.VISIBLE);
            viewHolder.textViews.get(0).setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
        } else {
            viewHolder.textViews.get(2).setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {

        @BindViews({R.id.item_dialog_down_adapter_label, R.id.item_dialog_down_adapter_tag, R.id.item_dialog_down_adapter_downed})
        List<TextView> textViews;
        @BindView(R.id.item_dialog_down_adapter_RotationLoadingView)
        AVLoadingIndicatorView loadingIndicatorView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
