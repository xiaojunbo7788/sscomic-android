package com.ssreader.novel.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.PayGoldDetail;
import com.ssreader.novel.model.RewardHistoryBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.RewardHistoryAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RewardHistoryFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView publicRecycleview;
    List<PayGoldDetail> list;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout fragmentReadhistoryPop;
    @BindView(R.id.fragment_option_noresult_text)
    TextView fragmentHistoryNoResult;
    @BindView(R.id.fragment_option_noresult_try)
    TextView fragmentHistoryGoLogin;

    private RewardHistoryAdapter rewardHistoryAdapter;

    public RewardHistoryFragment() {

    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_readhistory;
    }

    @Override
    public void initView() {
        fragmentHistoryGoLogin.setText(LanguageUtil.getString(activity, R.string.app_login_now));
        fragmentHistoryGoLogin.setBackground(MyShape.setMyshapeStroke(activity, ImageUtil.dp2px(activity, 1),
                1, ContextCompat.getColor(activity, R.color.maincolor)));
        list = new ArrayList<>();
        initSCRecyclerView(publicRecycleview, RecyclerView.VERTICAL, 0);
        rewardHistoryAdapter = new RewardHistoryAdapter(list, activity);
        publicRecycleview.setAdapter(rewardHistoryAdapter, true);
        setNoResult(true);
    }

    @OnClick({R.id.fragment_option_noresult_try})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_option_noresult_try:
                LoginUtils.goToLogin(activity);
                break;
        }
    }

    @Override
    public void initData() {
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("page_num", current_page + "");
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.REWARD_GIFT_LOG, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        RewardHistoryBean rewardHistoryBean = gson.fromJson(json, RewardHistoryBean.class);
        if (rewardHistoryBean.current_page <= rewardHistoryBean.total_page && !rewardHistoryBean.list.isEmpty()) {
            if (current_page == 1) {
                list.clear();
                publicRecycleview.setLoadingMoreEnabled(true);
            }
            list.addAll(rewardHistoryBean.list);
        }
        if (rewardHistoryBean.current_page >= rewardHistoryBean.total_page) {
            publicRecycleview.setLoadingMoreEnabled(false);
        } else {
            if (current_page == 1) {
                current_page = 2;
                initData();
            }
        }
        rewardHistoryAdapter.notifyDataSetChanged();
        if (list.isEmpty()) {
            setNoResult(true);
        } else {
            setNoResult(false);
        }
    }

    /**
     * 设置没有内容时的UI
     *
     * @param isShow
     */
    private void setNoResult(boolean isShow) {
        if (isShow) {
            publicRecycleview.setVisibility(View.GONE);
            fragmentReadhistoryPop.setVisibility(View.VISIBLE);
            if (!UserUtils.isLogin(activity)) {
                fragmentHistoryGoLogin.setVisibility(View.VISIBLE);
                fragmentHistoryNoResult.setText(LanguageUtil.getString(activity, R.string.app_reward_no_login_history));
            } else {
                fragmentHistoryGoLogin.setVisibility(View.GONE);
                fragmentHistoryNoResult.setText(LanguageUtil.getString(activity, R.string.app_reward_no_history));
            }
        } else {
            fragmentReadhistoryPop.setVisibility(View.GONE);
            publicRecycleview.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine r) {
        if (UserUtils.isLogin(activity)) {
            setNoResult(false);
            current_page = 1;
            initData();
        }
    }
}
