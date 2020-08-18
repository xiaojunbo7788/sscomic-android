package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;

import java.util.List;

/**
 * 搜索页上方的热词的adapter
 */
public class HotWordsAdapter extends BaseListAdapter<String> {

    Activity activity;

    public HotWordsAdapter(Activity activity, List<String> strings) {
        super(activity, strings);
        this.activity = activity;
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_hot_words;
    }

    @Override
    public View getOwnView(int position, String been, View convertView, ViewGroup parent) {
        LinearLayout container = convertView.findViewById(R.id.item_hot_word_container);
        TextView index = container.findViewById(R.id.item_hot_word_index);
        index.setText((position + 1) + "");
        if (position == 0) {
            container.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 3), ContextCompat.getColor(activity, R.color.red)));
        } else if (position == 1) {
            container.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 3), "#EE8437"));
        } else if (position == 2) {
            container.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 3), "#EDAD48"));
        } else {
            container.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 3), "#CBCCCC"));
        }
        TextView content = convertView.findViewById(R.id.item_hot_word_content);
        content.setText(mList.get(position));
        return convertView;
    }
}
