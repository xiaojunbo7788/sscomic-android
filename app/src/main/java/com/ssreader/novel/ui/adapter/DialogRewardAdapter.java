package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.GiftListBean;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogRewardAdapter extends BaseListAdapter<GiftListBean> {

    private ViewHolder viewHolder;

    public DialogRewardAdapter(Activity activity, List<GiftListBean> list) {
        super(activity, list);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_dialog_reward;
    }

    @Override
    public View getOwnView(int position, GiftListBean been, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder(convertView);
        MyGlide.GlideImageNoSize(activity,been.icon, viewHolder.itemRewardGiftImg);
        viewHolder.itemRewardGiftTitle.setText(been.title);
        viewHolder.itemRewardGiftPrice.setText(been.gift_price);
        ViewGroup.LayoutParams layoutParams = viewHolder.itemRewardGiftImg.getLayoutParams();
        layoutParams.width = (ScreenSizeUtils.getInstance(activity).getScreenWidth()/4)-35;
        viewHolder.itemRewardGiftImg.setLayoutParams(layoutParams);
        if(!been.flag.isEmpty()){
            viewHolder.item_dialog_reward_flag.setVisibility(View.VISIBLE);
            viewHolder.item_dialog_reward_flag.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity,2),ContextCompat.getColor(activity,R.color.Orgin2)));
            viewHolder.item_dialog_reward_flag.setText(been.flag);
        }else{
            viewHolder.item_dialog_reward_flag.setVisibility(View.GONE);
        }
        if(been.chose){
            viewHolder.itemRewardGiftLayout.setBackground(MyShape.setMyshape(activity, ImageUtil.dp2px(activity,2), ImageUtil.dp2px(activity,2), ImageUtil.dp2px(activity,2), ImageUtil.dp2px(activity,2),1, ContextCompat.getColor(activity,R.color.maincolor),0));
        }else{
            viewHolder.itemRewardGiftLayout.setBackground(null);
        }
        return convertView;
    }


    class ViewHolder extends BaseRecViewHolder {
        @BindView(R.id.item_reward_gift_img)
        ImageView itemRewardGiftImg;
        @BindView(R.id.item_reward_gift_title)
        TextView itemRewardGiftTitle;
        @BindView(R.id.item_reward_gift_price)
        TextView itemRewardGiftPrice;
        @BindView(R.id.item_reward_gift_layout)
        LinearLayout itemRewardGiftLayout;
        @BindView(R.id.item_dialog_reward_flag)
        TextView item_dialog_reward_flag;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
