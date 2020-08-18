package com.ssreader.novel.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.AnswerFeedBackBean;
import com.ssreader.novel.model.AnswerFeedBackListBean;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.MyFeedBackAdapter;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FeedBackListActivity extends BaseActivity {

    @BindView(R.id.activity_my_feed_back)
    SCRecyclerView activityMyFeedBack;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout fragment_option_noresult;
    @BindView(R.id.fragment_option_noresult_text)
    TextView fragment_option_noresult_text;

    private List<AnswerFeedBackListBean> list = new ArrayList<>();
    private MyFeedBackAdapter myFeedBackAdapter;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        public_sns_topbar_title_id = R.string.activityMyFeedBack;
        return R.layout.activity_my_feed_back;
    }

    @Override
    public void initView() {
        fragment_option_noresult_text.setText(LanguageUtil.getString(activity, R.string.activityNoFeedBack));
        initSCRecyclerView(activityMyFeedBack, RecyclerView.VERTICAL, 0);
        myFeedBackAdapter = new MyFeedBackAdapter(activity, list);
        activityMyFeedBack.setAdapter(myFeedBackAdapter,true, ContextCompat.getColor(activity,R.color.graybg));
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        httpUtils.sendRequestRequestParams(activity,Api.AnswerFaceBcakList, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            AnswerFeedBackBean answerFeedBackBean = gson.fromJson(json, AnswerFeedBackBean.class);
            if (answerFeedBackBean.current_page <= answerFeedBackBean.total_count&&!answerFeedBackBean.list.isEmpty()) {
                if (current_page == 1) {
                    this.list.clear();
                    activityMyFeedBack.setLoadingMoreEnabled(true);
                }
                this.list.addAll(answerFeedBackBean.list);
            }
            if (answerFeedBackBean.current_page >= answerFeedBackBean.total_page) {
                activityMyFeedBack.setLoadingMoreEnabled(false);
            }
            myFeedBackAdapter.notifyDataSetChanged();
            if (list.isEmpty()) {
                activityMyFeedBack.setVisibility(View.GONE);
                fragment_option_noresult.setVisibility(View.VISIBLE);
            }
        } else {
            fragment_option_noresult.setVisibility(View.VISIBLE);
            activityMyFeedBack.setVisibility(View.GONE);
        }
    }

}
