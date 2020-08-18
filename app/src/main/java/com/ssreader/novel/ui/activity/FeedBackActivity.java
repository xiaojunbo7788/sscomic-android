package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.FeedBackAnswerBean;
import com.ssreader.novel.model.FeedBackAnswerNameBean;
import com.ssreader.novel.ui.adapter.FeedBackAnswerAdapter;
import com.ssreader.novel.ui.adapter.FeedBackAnswerListAdapter;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.ui.utils.LoginUtils.goToLogin;

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_right_tv)
    TextView mPublicSnsTopbarRightTv;
    @BindView(R.id.activity_yijian_feedback_layout)
    RelativeLayout activity_yijian_feedback_layout;
    @BindView(R.id.activity_my_feed_back_layout)
    RelativeLayout activity_my_feed_back_layout;
    @BindView(R.id.activity_feed_back_view)
    ListView activity_feed_back_view;

    private List<FeedBackAnswerNameBean> list;
    private FeedBackAnswerAdapter feedBackAnswerAdapter;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.FeedBackActivity_title;
        return R.layout.activity_feed_back;
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        feedBackAnswerAdapter = new FeedBackAnswerAdapter(activity, list);
        activity_feed_back_view.setAdapter(feedBackAnswerAdapter);
        feedBackAnswerAdapter.setOnScrollListener(new FeedBackAnswerListAdapter.OnScrollListener() {
            @Override
            public void onScroll() {
                // 最后一个item时，将布局滑动到最底部
                activity_feed_back_view.setSelection(activity_feed_back_view.getBottom());
            }
        });
    }

    @OnClick({R.id.activity_yijian_feedback_layout, R.id.activity_my_feed_back_layout})
    public void onClick(View v) {
        if (goToLogin(activity)) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.activity_my_feed_back_layout:
                    //我的反馈
                    if(goToLogin(activity)){
                        intent.setClass(activity, FeedBackListActivity.class);
                    }
                    break;
                case R.id.activity_yijian_feedback_layout:
                    //反馈意见
                    if(goToLogin(activity)){
                        intent.setClass(activity, FeedBakcPostActivity.class);
                    }
                    break;
            }
            startActivity(intent);
        }
    }

    @Override
    public void initData() {
        httpUtils.sendRequestRequestParams(activity,Api.FaceBcakAnswer, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        FeedBackAnswerBean feedBackAnswerBean = gson.fromJson(json, FeedBackAnswerBean.class);
        List<FeedBackAnswerNameBean> help_list = feedBackAnswerBean.getHelp_list();
        list.addAll(help_list);
        feedBackAnswerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FeedBakcPostActivity.isCommentSuccess) {
            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.FeedBackActivity_fankui_success));
            FeedBakcPostActivity.isCommentSuccess = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }
}
