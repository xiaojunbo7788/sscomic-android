package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseReadHistory;
import com.ssreader.novel.model.MyLikeItemBean;
import com.ssreader.novel.model.PublicPage;
import com.ssreader.novel.model.ReadHistory;
import com.ssreader.novel.model.RewardHistoryBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.adapter.ReadHistoryBookAdapter;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyLikeComicListFragment extends BaseFragment {

    private MyLikeItemBean itemBean;
    private int type;
    @BindView(R.id.public_recycleview)
    SCRecyclerView mSCRecyclerView;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout mFragmentPop;
    @BindView(R.id.fragment_option_noresult_text)
    TextView mFragmentText;

    private PublicPage publicPage;
    private List<BaseReadHistory> optionBeenList;
    private ReadHistoryBookAdapter optionAdapter;
    private String channelId;

    public MyLikeComicListFragment(MyLikeItemBean itemBean,int type,String channelId) {
        this.type = type;
        this.itemBean = itemBean;
        this.channelId = channelId;
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_like_comic;
    }

    @Override
    public void initView() {
        initSCRecyclerView(mSCRecyclerView, RecyclerView.VERTICAL, 0);
        optionBeenList = new ArrayList<>();
        optionAdapter = new ReadHistoryBookAdapter(activity, optionBeenList, getPosition, Constant.COMIC_CONSTANT);
        optionAdapter.setLike(true);
        mSCRecyclerView.setAdapter(optionAdapter, true);
        setNoResult(true);
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("page_num", current_page + "");
        String url = "";
        String tab = "";
        if (type == 1) {
            url = Api.MyLikeAuthorList;
            tab = itemBean.getAuthor();
            readerParams.putExtraParams("author", tab);
        } else if (type == 2) {
            url = Api.MyLikeOriginalList;
            tab = itemBean.getOriginal();
            readerParams.putExtraParams("original", tab);
        } else if (type == 3) {
            url = Api.MyLikeSiniciList;
            tab = itemBean.getSinici();
            readerParams.putExtraParams("sinici", tab);
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        readerParams.putExtraParams("channel_id",this.channelId);
        HttpUtils.getInstance().sendRequestRequestParams(activity,url, readerParams.generateParamsJson(),responseListener);
    }

    /**
     * 点击事件
     */
    ReadHistoryFragment.GetPosition getPosition = new ReadHistoryFragment.GetPosition() {
        @Override
        public void getPosition(int falg, BaseReadHistory readHistoryBook, int position, int productType) {
            Intent intent = new Intent(activity, ComicInfoActivity.class);
            intent.putExtra("comic_id", readHistoryBook.getComic_id());
            startActivity(intent);
        }
    };


    /**
     * 设置没有内容时的UI
     *
     * @param isShow
     */
    private void setNoResult(boolean isShow) {
        if (isShow) {
            mSCRecyclerView.setVisibility(View.GONE);
            mFragmentPop.setVisibility(View.VISIBLE);
            mFragmentText.setText("暂无数据");
        } else {
            mFragmentPop.setVisibility(View.GONE);
            mSCRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initInfo(String json) {
        ReadHistory optionItem = gson.fromJson(json, ReadHistory.class);
        publicPage = optionItem;
        if (optionItem.list != null) {
            if (publicPage.current_page <= publicPage.total_page && !optionItem.list.isEmpty()) {
                if (current_page == 1) {
                    optionAdapter.NoLinePosition = -1;
                    mSCRecyclerView.setLoadingMoreEnabled(true);
                    optionBeenList.clear();
                }
                optionBeenList.addAll(optionItem.list);
            }
        }
        if (!optionBeenList.isEmpty()) {
            if (publicPage.current_page >= publicPage.total_page) {
                optionAdapter.NoLinePosition = optionBeenList.size() - 1;
                mSCRecyclerView.setLoadingMoreEnabled(false);
            }
            setNoResult(false);
            optionAdapter.notifyDataSetChanged();
        } else {
            setNoResult(true);
        }
    }
}
