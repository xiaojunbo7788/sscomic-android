package com.ssreader.novel.ui.localshell.localapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.eventbus.RefreshMineListItem;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.ui.adapter.MineListAdapter;
import com.ssreader.novel.ui.localshell.eventBus.LocalUserInfo;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalMineFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView public_recycleview;

    private List<MineModel> mineModels = new ArrayList<>();
    private MineListAdapter mineListAdapter;
    private ViewHolder viewHolder;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.public_recycleview;
    }

    @Override
    public void initView() {
        initSCRecyclerView(public_recycleview, RecyclerView.VERTICAL, 0);
        public_recycleview.setLoadingMoreEnabled(false);
        public_recycleview.setPullRefreshEnabled(false);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.fragment_local_mine, null);
        viewHolder = new ViewHolder(inflate);
        public_recycleview.addHeaderView(inflate);

        String localPhoto = ShareUitls.getString(activity, "LocalPhoto", "");
        if (localPhoto != null && !localPhoto.isEmpty()) {
            MyGlide.GlideImageHeadNoSize(activity, localPhoto, viewHolder.mineAvatar);
        } else {
            viewHolder.mineAvatar.setImageResource(R.mipmap.icon_def_head);
        }
        String UserName = ShareUitls.getString(activity, "UserName", "");
        String UserDes = ShareUitls.getString(activity, "UserDes", "");
        if (UserName != null && !UserName.isEmpty()) {
            viewHolder.localTitle.setText(UserName);
        }
        if (UserDes != null && !UserDes.isEmpty()) {
            viewHolder.localDescription.setText(UserDes);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) public_recycleview.getLayoutParams();
        layoutParams.topMargin = ImageUtil.dp2px(activity, 20);
        public_recycleview.setLayoutParams(layoutParams);
        mineModels.add(new MineModel(true, R.mipmap.ic_friend, LanguageUtil.getString(activity, R.string.SettingsActivity_market),
                "", "", "", "share_local", 1));
        mineModels.add(new MineModel(false, R.mipmap.ic_set, LanguageUtil.getString(activity, R.string.SettingsActivity_clear),
                "", "", "", "clean_local", 1));
        mineModels.add(new MineModel(false, R.mipmap.ic_lianxi, LanguageUtil.getString(activity, R.string.MineNewFragment_lianxikefu),
                "", "", "", "service_local", 1));
        mineModels.add(new MineModel(false, R.mipmap.ic_fankui, LanguageUtil.getString(activity, R.string.MineNewFragment_fankui),
                "", "", "", "feedback_local", 1));
        mineListAdapter = new MineListAdapter(true,activity, mineModels);
        public_recycleview.setAdapter(mineListAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    class ViewHolder {
        @BindView(R.id.mine_avatar)
        ImageView mineAvatar;
        @BindView(R.id.local_title)
        TextView localTitle;
        @BindView(R.id.local_description)
        TextView localDescription;
        @BindView(R.id.mine_login_LinearLayout)
        LinearLayout mineLoginLinearLayout;

        @OnClick({R.id.mine_login_LinearLayout})
        public void onClick(View view) {
            if (view.getId() == R.id.mine_login_LinearLayout) {
                Intent intent = new Intent(activity, LocalUserInfoActivity.class);
                intent.putExtra("userTitle", localTitle.getText().toString());
                intent.putExtra("userDes", localDescription.getText().toString());
                startActivity(intent);
            }
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshMine(LocalUserInfo userInfo) {
        if (userInfo.name != null && !userInfo.name.isEmpty()) {
            viewHolder.localTitle.setText(userInfo.name);
        } else if (userInfo.des != null && !userInfo.des.isEmpty()) {
            viewHolder.localDescription.setText(userInfo.des);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshMineListItem(RefreshMineListItem refreshMineListItem) {
        ShareUitls.putString(activity, "LocalPhoto", refreshMineListItem.mCutUri);
        MyGlide.GlideImageHeadNoSize(activity, refreshMineListItem.mCutUri, viewHolder.mineAvatar);
    }
}
