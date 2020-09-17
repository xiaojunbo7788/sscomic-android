package com.ssreader.novel.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ssreader.novel.R;

import javax.annotation.Nullable;


/**
 * Created by yongqianggeng on 2019/1/25.
 */

public class StarLayout extends LinearLayout implements View.OnClickListener {

    private ImageView start1;
    private ImageView start2;
    private ImageView start3;
    private ImageView start4;
    private ImageView start5;

    private StarLayoutLisenter mStarLayoutLisenter;

    public StarLayout(Context context) {
        super(context);
    }

    public StarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.star_layout,this);
        start1 = findViewById(R.id.start_view_1);
        start2 = findViewById(R.id.start_view_2);
        start3 = findViewById(R.id.start_view_3);
        start4 = findViewById(R.id.start_view_4);
        start5 = findViewById(R.id.start_view_5);

        start1.setImageResource(R.mipmap.s_start);
        start2.setImageResource(R.mipmap.us_start);
        start3.setImageResource(R.mipmap.us_start);
        start4.setImageResource(R.mipmap.us_start);
        start5.setImageResource(R.mipmap.us_start);

        start1.setOnClickListener(this);
        start2.setOnClickListener(this);
        start3.setOnClickListener(this);
        start4.setOnClickListener(this);
        start5.setOnClickListener(this);

    }

    public void setmStarLayoutLisenter(StarLayoutLisenter mStarLayoutLisenter) {
        this.mStarLayoutLisenter = mStarLayoutLisenter;
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.start_view_1) {
//            if (mStarLayoutLisenter != null) {
//                mStarLayoutLisenter.onClickAtIndex(1);
//            }
//            start1.setImageResource(R.mipmap.s_start);
//            start2.setImageResource(R.mipmap.us_start);
//            start3.setImageResource(R.mipmap.us_start);
//            start4.setImageResource(R.mipmap.us_start);
//            start5.setImageResource(R.mipmap.us_start);
//        } else if (v.getId() == R.id.start_view_2) {
//            if (mStarLayoutLisenter != null) {
//                mStarLayoutLisenter.onClickAtIndex(2);
//            }
//            start1.setImageResource(R.mipmap.xuanzhong_s);
//            start2.setImageResource(R.mipmap.xuanzhong_s);
//            start3.setImageResource(R.mipmap.weixuanzhong_s);
//            start4.setImageResource(R.mipmap.weixuanzhong_s);
//            start5.setImageResource(R.mipmap.weixuanzhong_s);
//        } else if (v.getId() == R.id.start_view_3) {
//            if (mStarLayoutLisenter != null) {
//                mStarLayoutLisenter.onClickAtIndex(3);
//            }
//            start1.setImageResource(R.mipmap.xuanzhong_s);
//            start2.setImageResource(R.mipmap.xuanzhong_s);
//            start3.setImageResource(R.mipmap.xuanzhong_s);
//            start4.setImageResource(R.mipmap.weixuanzhong_s);
//            start5.setImageResource(R.mipmap.weixuanzhong_s);
//        } else if (v.getId() == R.id.start_view_4) {
//            if (mStarLayoutLisenter != null) {
//                mStarLayoutLisenter.onClickAtIndex(4);
//            }
//            start1.setImageResource(R.mipmap.xuanzhong_s);
//            start2.setImageResource(R.mipmap.xuanzhong_s);
//            start3.setImageResource(R.mipmap.xuanzhong_s);
//            start4.setImageResource(R.mipmap.xuanzhong_s);
//            start5.setImageResource(R.mipmap.weixuanzhong_s);
//        } else if (v.getId() == R.id.start_view_5) {
//
//            if (mStarLayoutLisenter != null) {
//                mStarLayoutLisenter.onClickAtIndex(5);
//            }
//            start1.setImageResource(R.mipmap.xuanzhong_s);
//            start2.setImageResource(R.mipmap.xuanzhong_s);
//            start3.setImageResource(R.mipmap.xuanzhong_s);
//            start4.setImageResource(R.mipmap.xuanzhong_s);
//            start5.setImageResource(R.mipmap.xuanzhong_s);
//        }
    }

    public interface StarLayoutLisenter {
        void onClickAtIndex(int index);
    }
}
