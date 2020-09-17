package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.MyLikeItemBean;
import com.ssreader.novel.ui.utils.MyGlide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyLikeAdapter extends BaseRecAdapter<MyLikeItemBean,MyLikeAdapter.ViewHolder> {

    private MyLikeAdapterListener iMyLikeAdapterListener;

    public void setiMyLikeAdapterListener(MyLikeAdapterListener iMyLikeAdapterListener) {
        this.iMyLikeAdapterListener = iMyLikeAdapterListener;
    }

    private int type;
    public MyLikeAdapter(Activity activity, List<MyLikeItemBean> list,int type) {
        super(list, activity);
        this.type = type;
    }

    @Override
    public void onHolder(MyLikeAdapter.ViewHolder holder, MyLikeItemBean bean, int position) {
        MyGlide.GlideImageNoSize(activity, bean.getIcon(), holder.imageView);
        switch (type) {
            case 1:
                holder.titleView.setText(bean.getAuthor());
                break;
            case 2:
                holder.titleView.setText(bean.getOriginal());
                break;
            case 3:
                holder.titleView.setText(bean.getSinici());
                break;

        }
        holder.countView.setText("合计"+bean.getCount()+"篇");
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iMyLikeAdapterListener != null) {
                    iMyLikeAdapterListener.onClickItem(bean);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new MyLikeAdapter.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_my_like, null));
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_like)
        LinearLayout rootView;

        @BindView(R.id.like_img)
        ImageView imageView;
        @BindView(R.id.like_title)
        TextView titleView;
        @BindView(R.id.like_count)
        TextView countView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyLikeAdapterListener {
        void onClickItem(MyLikeItemBean bean);
    }
}
