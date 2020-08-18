package com.ssreader.novel.ui.fragment;

import android.annotation.SuppressLint;
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
import com.ssreader.novel.model.PublicPage;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.GoldRecordAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.MONTHTICKETHISTORY;

public class GoldRecordFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView recyclerView;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout noResultLayout;
    @BindView(R.id.fragment_option_noresult_text)
    TextView noResult;
    @BindView(R.id.fragment_option_noresult_try)
    TextView goLogin;

    @OnClick({R.id.fragment_option_noresult_try})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_option_noresult_try:
                LoginUtils.goToLogin(activity);
                break;
        }
    }

    public String rule_url;
    private String unit;
    private List<PayGoldDetail> optionBeenList;
    private GoldRecordAdapter optionAdapter;
    public int OPTION;

    public GoldRecordFragment() {

    }

    public GoldRecordFragment(int OPTION) {
        this.OPTION = OPTION;
    }

    @SuppressLint("ValidFragment")
    public GoldRecordFragment(String unit) {
        this.unit = unit;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_readhistory;
    }

    @Override
    public void initView() {
        initSCRecyclerView(recyclerView, RecyclerView.VERTICAL, 0);
        goLogin.setText(LanguageUtil.getString(activity, R.string.app_login_now));
        goLogin.setBackground(MyShape.setMyshapeStroke(activity, ImageUtil.dp2px(activity, 1),
                1, ContextCompat.getColor(activity, R.color.maincolor)));
        optionBeenList = new ArrayList<>();
        optionAdapter = new GoldRecordAdapter(optionBeenList, activity, OPTION);
        recyclerView.setAdapter(optionAdapter, true);
        setNoResult(true);
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        if (OPTION != MONTHTICKETHISTORY) {
            http_URL = Api.mPayGoldDetailUrl;
            readerParams.putExtraParams("unit", unit);
        } else {
            http_URL = Api.REWARD_TICKET_LOG;
        }
        readerParams.putExtraParams("page_size", 20);
        readerParams.putExtraParams("page_num", current_page);
        HttpUtils.getInstance().sendRequestRequestParams(activity,http_URL, readerParams.generateParamsJson(),responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (json != null && !json.isEmpty()) {
            OptionItem optionItem = gson.fromJson(json, OptionItem.class);
            if (optionItem.current_page <= optionItem.total_page && !optionItem.list.isEmpty()) {
                if (current_page == 1) {
                    optionBeenList.clear();
                    optionAdapter.NoLinePosition=-1;
                    recyclerView.setLoadingMoreEnabled(true);
                }
                optionBeenList.addAll(optionItem.list);
            }
            if (optionItem.current_page >= optionItem.total_page) {
                optionAdapter.NoLinePosition=optionBeenList.size()-1;
                recyclerView.setLoadingMoreEnabled(false);
            }
            optionAdapter.notifyDataSetChanged();
            if (optionBeenList.isEmpty()) {
                setNoResult(true);
            } else {
                setNoResult(false);
            }
            if (optionItem.ticket_rule != null) {
                rule_url = optionItem.ticket_rule;
            }
        }
    }

    /**
     * 设置没有内容时的UI
     *
     * @param isShow
     */
    private void setNoResult(boolean isShow) {
        if (isShow) {
            recyclerView.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
            if (!UserUtils.isLogin(activity)) {
                goLogin.setVisibility(View.VISIBLE);
                if (OPTION == MONTHTICKETHISTORY){
                    noResult.setText(LanguageUtil.getString(activity, R.string.app_monthly_web_need_login));
                }else {
                    noResult.setText(LanguageUtil.getString(activity, R.string.app_liushuijilu_no_login));
                }
            } else {
                goLogin.setVisibility(View.GONE);
                if (OPTION == MONTHTICKETHISTORY){
                    noResult.setText(LanguageUtil.getString(activity, R.string.app_monthly_pass_history_no_data));
                }else {
                    noResult.setText(LanguageUtil.getString(activity, R.string.app_liushuijilu_no_result));
                }
            }
        } else {
            noResultLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public class OptionItem extends PublicPage {
        public List<PayGoldDetail> list;
        public String ticket_rule;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity)) {
            setNoResult(false);
            current_page = 1;
            initData();
        }
    }
}
