package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.model.MineUserInfoBean;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.utils.SystemUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ssreader.novel.constant.Constant.USE_QQ;
import static com.ssreader.novel.constant.Constant.USE_WEIXIN;

public class UserInfoAdapter extends BaseRecAdapter<MineModel, UserInfoAdapter.ViewHolder> {

    private avatarCallBack avatarCallBack;

    public void setAvatarCallBack(avatarCallBack avatarCallBack) {
        this.avatarCallBack = avatarCallBack;
    }

    public UserInfoAdapter(Activity activity, List<MineModel> list) {
        super(list, activity);
    }

    @Override
    public void onHolder(ViewHolder holder, MineModel bean, int position) {
        holder.user_info_title.setText(bean.getTitle());
        if (avatarCallBack != null) {
            avatarCallBack.setAvatar(holder.mUserInfoAvatar);
        }
        if (bean.getAction().equals("avatar")) {
            holder.mUserInfoDisplay.setVisibility(View.GONE);
            holder.mUserInfoAvatar.setVisibility(View.VISIBLE);
            MyGlide.GlideImageHeadNoSize(activity, bean.getDesc(), holder.mUserInfoAvatar);
        } else {
            holder.mUserInfoDisplay.setVisibility(View.VISIBLE);
            holder.mUserInfoAvatar.setVisibility(View.GONE);
            holder.mUserInfoDisplay.setText(bean.getDesc());
        }
        if (bean.getTitle_color() != null && !TextUtils.isEmpty(bean.getTitle_color())) {
            holder.user_info_title.setTextColor(Color.parseColor(bean.getTitle_color()));
        }
        if (bean.getDesc_color() != null && !TextUtils.isEmpty(bean.getDesc_color())) {
            holder.mUserInfoDisplay.setTextColor(Color.parseColor(bean.getDesc_color()));
        }
        if (bean.getIs_click() == 1) {
            holder.jiaobiao.setVisibility(View.VISIBLE);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (avatarCallBack != null) {
                        avatarCallBack.onClickItem(bean);
                    }
                }
            });
        } else {
            holder.jiaobiao.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mUserInfoDisplay.getLayoutParams();
            layoutParams.rightMargin = ImageUtil.dp2px(activity, 10);
            holder.mUserInfoDisplay.setLayoutParams(layoutParams);
            holder.mUserInfoDisplay.getLayoutParams();
            holder.jiaobiao.setVisibility(View.GONE);
        }
        if (bean.isBottomLine()) {
            holder.mine_top_line.setVisibility(View.GONE);
            holder.bottom_view.setVisibility(View.VISIBLE);
        } else {
            holder.bottom_view.setVisibility(View.GONE);
            holder.mine_top_line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_mine_user_info));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_mine_user_info_layout)
        LinearLayout linearLayout;
        @BindView(R.id.user_info_avatar)
        ImageView mUserInfoAvatar;
        @BindView(R.id.user_info_display)
        TextView mUserInfoDisplay;
        @BindView(R.id.user_info_title)
        TextView user_info_title;
        @BindView(R.id.user_info_into_image)
        ImageView jiaobiao;
        @BindView(R.id.user_info_line)
        View mine_top_line;
        @BindView(R.id.user_info_bottom_bg)
        View bottom_view;

        public ViewHolder(View view) {
            super(view);
        }
    }

    public interface avatarCallBack {

        void setAvatar(ImageView imageView);

        void onClickItem(MineModel model);
    }
}
