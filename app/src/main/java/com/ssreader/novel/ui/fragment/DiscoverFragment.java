package com.ssreader.novel.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.DiscoverExpierTimeEnd;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.model.BookComicStoare;
import com.ssreader.novel.model.DiscoverComicData;
import com.ssreader.novel.model.PublicIntent;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.MainHttpTask;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.DiscoverComicAdapter;
import com.ssreader.novel.ui.adapter.PublicMainAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.banner.ConvenientBanner;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;

/**
 * 发现界面
 */
public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView mPublicRecycleview;

    private ViewHolder viewHolder;
    private int productType;
    private List<BaseLabelBean> list;
    private PublicMainAdapter coverAdatper;
    private List<BaseBookComic> comicList;
    private DiscoverComicAdapter discoverComicAdapter;
    private List<PublicIntent> banner;
    private int top_height;

    public DiscoverFragment() {

    }

    public DiscoverFragment(int productType, int top_height) {
        this.productType = productType;
        if (top_height != 0) {
            this.top_height = top_height;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            productType = savedInstanceState.getInt("productType");
            if (savedInstanceState.getInt("top_heigth") != 0) {
                top_height = savedInstanceState.getInt("top_height");
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("productType", productType);
        outState.putInt("top_height", top_height);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int initContentView() {
        USE_EventBus=true;
        return R.layout.public_recycleview;
    }

    @Override
    public void initView() {
        if (top_height != 0) {
            mPublicRecycleview.setPadding(0, top_height, 0, 0);
        }
        list = new ArrayList<>();
        comicList = new ArrayList<>();
        initSCRecyclerView(mPublicRecycleview, RecyclerView.VERTICAL, 0);
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_haed_discover_banner, null);
        viewHolder = new ViewHolder(view);
        mPublicRecycleview.addHeaderView(view);

        ViewGroup.LayoutParams layoutParams = viewHolder.mFragmentDiscoveryBannerMale.getLayoutParams();
        layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        layoutParams.height = (layoutParams.width - ImageUtil.dp2px(activity, 20)) / 4;
        viewHolder.mFragmentDiscoveryBannerMale.setLayoutParams(layoutParams);

        if (productType == BOOK_CONSTANT || productType == AUDIO_CONSTANT) {
            // 小说
            mPublicRecycleview.setLoadingMoreEnabled(false);
            coverAdatper = new PublicMainAdapter(list, productType, activity, false, false);
            mPublicRecycleview.setAdapter(coverAdatper);
        } else {
            // 漫画
            discoverComicAdapter = new DiscoverComicAdapter(comicList, activity);
            mPublicRecycleview.setAdapter(discoverComicAdapter, false);
        }
    }
    /**
     * 登录回调
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(DiscoverExpierTimeEnd refreshMine) {
        if (productType == BOOK_CONSTANT) {
            current_page=1;
            initData();
        }
    }

    @Override
    public void initData() {
        if (current_page == 1) {
            String Option = "";
            if (productType == BOOK_CONSTANT) {
                Option = "DiscoverBook";
            } else if (productType == COMIC_CONSTANT) {
                Option = "DiscoverComic";
            } else if (productType == AUDIO_CONSTANT) {
                Option = "DiscoverAudio";
            }
            MainHttpTask.getInstance().getResultString(activity, http_flag != 0, Option, new MainHttpTask.GetHttpData() {
                @Override
                public void getHttpData(String result) {
                    http_flag = 1;
                    responseListener.onResponse(result);
                }
            });
        } else {
            readerParams = new ReaderParams(activity);
            readerParams.putExtraParams("page_num", current_page + "");
            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.COMIC_featured, readerParams.generateParamsJson(),responseListener);
        }
    }

    @Override
    public void initInfo(String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        if (productType == BOOK_CONSTANT || productType == AUDIO_CONSTANT) {
            BookComicStoare bookComicStoare = gson.fromJson(json, BookComicStoare.class);
            banner = bookComicStoare.getBanner();
            ConvenientBanner.initBanner(activity, 3, banner, viewHolder.mFragmentDiscoveryBannerMale, productType);
            List<BaseLabelBean> label = bookComicStoare.getLabel();
            if (!list.isEmpty()) {
                list.clear();
            }
            if (label != null) {
                list.addAll(label);
                coverAdatper.notifyDataSetChanged();
            }
        } else if (productType == COMIC_CONSTANT) {
            DiscoverComicData comicStoare = gson.fromJson(json, DiscoverComicData.class);
            List<PublicIntent> banner = comicStoare.getBanner();
            ConvenientBanner.initBanner(activity, 3, banner, viewHolder.mFragmentDiscoveryBannerMale, productType);
            if (comicStoare.item_list != null) {
                if (current_page <= comicStoare.item_list.total_count && !comicStoare.item_list.list.isEmpty()) {
                    if (current_page == 1) {
                        mPublicRecycleview.setLoadingMoreEnabled(true);
                        comicList.clear();
                        comicList.addAll(comicStoare.item_list.list);
                    } else {
                        comicList.addAll(comicStoare.item_list.list);
                    }
                } else {
                    if (comicStoare.item_list.current_page >= comicStoare.item_list.total_page && current_page != 1) {
                        mPublicRecycleview.setLoadingMoreEnabled(false);
                    }
                }
                discoverComicAdapter.notifyDataSetChanged();
            }
        }
    }

    public class ViewHolder {

        @BindView(R.id.fragment_discovery_banner_male)
        ConvenientBanner mFragmentDiscoveryBannerMale;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
