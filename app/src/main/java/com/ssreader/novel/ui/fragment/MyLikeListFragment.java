package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.MyLikeItemBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.ui.adapter.MyLikeAdapter;
import com.ssreader.novel.ui.read.util.ScreenUtils;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.COLLECTION_CONTENT_LIST;

public class MyLikeListFragment extends BaseFragment {

    private int type;
    @BindView(R.id.public_recycleview)
    SCRecyclerView mFragmentReadhistoryReadhistory;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout mFragmentReadhistoryPop;
    @BindView(R.id.fragment_option_noresult_text)
    TextView mFragmentHistoryText;
    @BindView(R.id.fragment_option_noresult_try)
    TextView mFragmentHistoryGoLogin;

    private MyLikeAdapter myLikeAdapter;

    private List<MyLikeItemBean>optionBeenList = new ArrayList<>();

    public MyLikeListFragment(int type) {
        this.type = type;
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_like_list;
    }

    @Override
    public void initView() {
        mFragmentReadhistoryReadhistory.setPullRefreshEnabled(false);
        mFragmentHistoryGoLogin.setText(LanguageUtil.getString(activity, R.string.app_login_now));
        mFragmentHistoryGoLogin.setBackground(MyShape.setMyshapeStroke(activity, ImageUtil.dp2px(activity, 1),
                1, ContextCompat.getColor(activity, R.color.maincolor)));
        initSCRecyclerView(mFragmentReadhistoryReadhistory, RecyclerView.VERTICAL, 3);
        RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams) mFragmentReadhistoryReadhistory.getLayoutParams();
        layoutParams.setMargins(ScreenUtils.dpToPx(10),0,ScreenUtils.dpToPx(10),0);

        myLikeAdapter = new MyLikeAdapter(activity,optionBeenList,type);
        mFragmentReadhistoryReadhistory.setAdapter(myLikeAdapter, true);
        setNoResult(true);
        myLikeAdapter.setiMyLikeAdapterListener(new MyLikeAdapter.MyLikeAdapterListener() {
            @Override
            public void onClickItem(MyLikeItemBean bean) {
                String title = "";
                switch (type) {
                    case 1:
                        title = bean.getAuthor();
                        break;
                    case 2:
                        title = bean.getOriginal();
                        break;
                    case 3:
                        title = bean.getSinici();
                        break;
                }
                activity.startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("OPTION", COLLECTION_CONTENT_LIST)
                        .putExtra("title", title)
                        .putExtra("type", type)
                        .putExtra("channelId", bean.getFrom_channel())
                        .putExtra("item",bean));
            }
        });
    }

    @Override
    public void initData() {
        SCRecyclerViewRefresh = false;
        SCRecyclerViewLoadMore = false;
        String url = "";
        switch (type) {
            case 1:
                url = Api.MyLikeAuthor;
                break;
            case 2:
                url = Api.MyLikeOriginal;
                break;
            case 3:
                url = Api.MyLikeSinici;
                break;
        }
        ReaderParams readerParams = new ReaderParams(activity);
        HttpUtils.getInstance().sendRequestRequestParams(activity, url, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        try {
            List<MyLikeItemBean> optionItems = gson.fromJson(json, new TypeToken<List<MyLikeItemBean>>() {}.getType());
            if (!optionItems.isEmpty()) {
                mFragmentReadhistoryReadhistory.setLoadingMoreEnabled(false);
                setNoResult(false);
                optionBeenList.addAll(optionItems);
                myLikeAdapter.notifyDataSetChanged();
            } else {
                setNoResult(true);
            }
        } catch (Exception e) {
            setNoResult(true);
        }
    }


    /**
     * 设置没有内容时的UI
     *
     * @param isShow
     */
    private void setNoResult(boolean isShow) {
        if (isShow) {
            mFragmentReadhistoryReadhistory.setVisibility(View.GONE);
            mFragmentReadhistoryPop.setVisibility(View.VISIBLE);
            if (!UserUtils.isLogin(activity)) {
                mFragmentHistoryGoLogin.setVisibility(View.VISIBLE);
                mFragmentHistoryText.setText(LanguageUtil.getString(activity, R.string.app_like_no_login));
            } else {
                mFragmentHistoryGoLogin.setVisibility(View.GONE);
                mFragmentHistoryText.setText(LanguageUtil.getString(activity, R.string.app_like_no_result));
            }
        } else {
            mFragmentReadhistoryPop.setVisibility(View.GONE);
            mFragmentReadhistoryReadhistory.setVisibility(View.VISIBLE);
        }
    }

}
