package com.ssreader.novel.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.ssreader.novel.R;

/**
 * 高度自适应，防止出现滚动条
 */
public class AdaptionGridViewNoMargin extends GridView {

    public AdaptionGridViewNoMargin(Context context) {
        this(context, null);
    }

    public AdaptionGridViewNoMargin(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdaptionGridViewNoMargin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSelector(getResources().getDrawable(R.drawable.selector_listview_item));
        setFocusable(false);
    }

    //    不出现滚动条
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}