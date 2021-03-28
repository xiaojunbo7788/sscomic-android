package com.ssreader.novel.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.eventbus.StoreHotWords;
import com.ssreader.novel.model.BannerBottomItem;
import com.ssreader.novel.model.BannerNoticeBean;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.model.BookComicStoare;
import com.ssreader.novel.model.PublicIntent;
import com.ssreader.novel.net.MainHttpTask;
import com.ssreader.novel.ui.adapter.BannerBottomItemAdapter;
import com.ssreader.novel.ui.adapter.PublicMainAdapter;
import com.ssreader.novel.ui.adapter.TextAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.view.AdaptionGridViewNoMargin;
import com.ssreader.novel.ui.view.banner.ConvenientBanner;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.haowen.textbanner.TextBanner;
import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;

public class StoreFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView mPublicRecycleview;

    private ViewHolder viewHolder;
    private List<BannerBottomItem> bannerBottomItems;
    private List<BaseLabelBean> list;
    private BannerBottomItemAdapter bottomItemAdapter;
    private PublicMainAdapter publicMainAdapter;
    TextBanner noticeView;

    private List<BannerNoticeBean>hotWordArray = new ArrayList<>();

    private int productType;
    // 性别 1 = 男，  2 = 女
    private int channel_id = 1;
    private Public_main_fragment.OnChangeSex changeSex;

    TextAdapter simpleAdapter;

    public StoreFragment() {

    }

    public StoreFragment(int productType, int channel_id) {
        this.productType = productType;
        this.channel_id = channel_id;
    }

    /**
     * 切换男女
     * @param channel_id
     * @param changeSex
     */
    public void setChannel_id(int channel_id, Public_main_fragment.OnChangeSex changeSex) {
        this.channel_id = channel_id;
        this.changeSex = changeSex;
        initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            productType = savedInstanceState.getInt("productType");
            channel_id = savedInstanceState.getInt("channel_id");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("productType", productType);
        outState.putInt("channel_id", channel_id);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int initContentView() {
        return R.layout.public_recycleview;
    }

    @Override
    public void initView() {
        mPublicRecycleview.setLoadingMoreEnabled(false);
        initSCRecyclerView(mPublicRecycleview, RecyclerView.VERTICAL, 0);
        mPublicRecycleview.setStoreScrollStatusInterface(true, productType);
        mPublicRecycleview.setItemViewCacheSize(200);
        mPublicRecycleview.setHasFixedSize(true);
        mPublicRecycleview.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        bannerBottomItems = new ArrayList<>();
        View view = LayoutInflater.from(activity).inflate(R.layout.head_book_store_item, null);
        noticeView = view.findViewById(R.id.textBanner);
        simpleAdapter = new TextAdapter(getContext(),R.layout.item_text_banner_simple, hotWordArray);
        noticeView.setAdapter(simpleAdapter);

        viewHolder = new ViewHolder(view);

        ViewGroup.LayoutParams layoutParams = viewHolder.mStoreBannerFrameLayout.getLayoutParams();
        int width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        int height = (width - ImageUtil.dp2px(activity, 20)) * 3 / 5;
        if (NotchHeight != 0) {
            height += ImageUtil.dp2px(activity, 58) + NotchHeight;
        } else {
            height += ImageUtil.dp2px(activity, 78);
        }
        layoutParams.height = height;
        layoutParams.width = width;
        viewHolder.mStoreBannerFrameLayout.setLayoutParams(layoutParams);

        // 首页tab
        bottomItemAdapter = new BannerBottomItemAdapter(activity, bannerBottomItems, productType);
        viewHolder.mStoreEntranceGridMale.setAdapter(bottomItemAdapter);
        initStoreTabListener();
        mPublicRecycleview.addHeaderView(view);
        publicMainAdapter = new PublicMainAdapter(list, productType, activity, false, false);
        mPublicRecycleview.setAdapter(publicMainAdapter);
    }

    private void initStoreTabListener() {
        viewHolder.mStoreEntranceGridMale.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (bannerBottomItems != null && !bannerBottomItems.isEmpty() && bannerBottomItems.size() > position) {
                    // 判断条目
                    bannerBottomItems.get(position).intentOption(activity, productType);
                }
            }
        });
    }

    @Override
    public void initData() {
        String Option = "";
        if (productType == BOOK_CONSTANT) {
            Option = channel_id == 1 ? "StoreBookMan" : "StoreBookWoMan";
        } else if (productType == COMIC_CONSTANT) {
            Option = channel_id == 1 ? "StoreComicMan" : "StoreComicWoMan";
        } else if (productType == AUDIO_CONSTANT) {
            Option = channel_id == 1 ? "StoreAudioMan" : "StoreAudioWoMan";
        }
        MainHttpTask.getInstance().getResultString(activity, http_flag != 0, Option, new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                http_flag = 1;
                responseListener.onResponse(result);
            }
        });
    }

    @Override
    public void initInfo(String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        list.clear();
        if (productType == BOOK_CONSTANT) {
            BookComicStoare bookStoare = gson.fromJson(json, BookComicStoare.class);
            if (bookStoare.getBanner() != null && !bookStoare.getBanner().isEmpty()) {
                ConvenientBanner.initBanner(activity, 2, bookStoare.getBanner(), viewHolder.mStoreBannerMale, productType);
            } else {
                List<PublicIntent> list = new ArrayList();
                PublicIntent publicIntent = new PublicIntent();
                publicIntent.action = 9;
                list.add(publicIntent);
                ConvenientBanner.initBanner(activity, 2, list, viewHolder.mStoreBannerMale, productType);
            }
            if (bookStoare.getMenus_tabs() != null && !bookStoare.getMenus_tabs().isEmpty()) {
                bannerBottomItems.clear();
                bannerBottomItems.addAll(bookStoare.getMenus_tabs());
                if (viewHolder.mStoreEntranceGridMale.getNumColumns() != bannerBottomItems.size()) {
                    viewHolder.mStoreEntranceGridMale.setNumColumns(bannerBottomItems.size());
                }
                if ((bookStoare.getAnnouncement() != null)) {
                    for (BannerNoticeBean noticeBean:bookStoare.getAnnouncement()) {
                        hotWordArray.add(noticeBean);
                    }
                }
                simpleAdapter.notifyDataChange();

                bottomItemAdapter.notifyDataSetChanged();
            }
            List<BaseLabelBean> label = bookStoare.getLabel();
            list.addAll(label);
            publicMainAdapter.notifyDataSetChanged();
            if (bookStoare.getHot_word() != null && !bookStoare.getHot_word().isEmpty()) {
                EventBus.getDefault().post(new StoreHotWords(productType, bookStoare.getHot_word(), channel_id));
            }
        } else if (productType == COMIC_CONSTANT) {
            BookComicStoare comicStoare = gson.fromJson(json, BookComicStoare.class);
            if (comicStoare.getBanner() != null && !comicStoare.getBanner().isEmpty()) {
                viewHolder.mStoreBannerMale.invalidate();
                ConvenientBanner.initBanner(activity, 2, comicStoare.getBanner(), viewHolder.mStoreBannerMale, productType);
            } else {
                List<PublicIntent> list = new ArrayList();
                PublicIntent publicIntent = new PublicIntent();
                publicIntent.action = 9;
                list.add(publicIntent);
                ConvenientBanner.initBanner(activity, 2, list, viewHolder.mStoreBannerMale, productType);
            }
            if (comicStoare.getMenus_tabs() != null && !comicStoare.getMenus_tabs().isEmpty()) {
                bannerBottomItems.clear();
                bannerBottomItems.addAll(comicStoare.getMenus_tabs());
                if (viewHolder.mStoreEntranceGridMale.getNumColumns() != bannerBottomItems.size()) {
                    viewHolder.mStoreEntranceGridMale.setNumColumns(bannerBottomItems.size());
                }
                if ((comicStoare.getAnnouncement() != null)) {
                    for (BannerNoticeBean noticeBean:comicStoare.getAnnouncement()) {
                        hotWordArray.add(noticeBean);
                    }
                }
                simpleAdapter.notifyDataChange();
                bottomItemAdapter.notifyDataSetChanged();
            }
            list.addAll(comicStoare.getLabel());
            publicMainAdapter.notifyDataSetChanged();
            if (comicStoare.getHot_word() != null && !comicStoare.getHot_word().isEmpty()) {
                EventBus.getDefault().post(new StoreHotWords(productType, comicStoare.getHot_word(), channel_id));
            }
        } else if (productType == AUDIO_CONSTANT) {
            BookComicStoare audioStoare = gson.fromJson(json, BookComicStoare.class);
            if (audioStoare.getBanner() != null && !audioStoare.getBanner().isEmpty()) {
                viewHolder.mStoreBannerMale.invalidate();
                ConvenientBanner.initBanner(activity, 2, audioStoare.getBanner(), viewHolder.mStoreBannerMale, productType);
            } else {
                //没有数据显示占位图
                List<PublicIntent> list = new ArrayList();
                PublicIntent publicIntent = new PublicIntent();
                publicIntent.action = 9;
                list.add(publicIntent);
                ConvenientBanner.initBanner(activity, 2, list, viewHolder.mStoreBannerMale, productType);
            }
            if (audioStoare.getMenus_tabs() != null && !audioStoare.getMenus_tabs().isEmpty()) {
                bannerBottomItems.clear();
                bannerBottomItems.addAll(audioStoare.getMenus_tabs());
                if (viewHolder.mStoreEntranceGridMale.getNumColumns() != bannerBottomItems.size()) {
                    viewHolder.mStoreEntranceGridMale.setNumColumns(bannerBottomItems.size());
                }
                bottomItemAdapter.notifyDataSetChanged();
            }
            list.addAll(audioStoare.getLabel());
            publicMainAdapter.notifyDataSetChanged();
            if (audioStoare.getHot_word() != null && !audioStoare.getHot_word().isEmpty()) {
                EventBus.getDefault().post(new StoreHotWords(productType, audioStoare.getHot_word(), channel_id));
            }
        }
        if (changeSex != null) {
            changeSex.success();
            changeSex = null;
        }
    }

    public class ViewHolder {

        @BindView(R.id.store_banner_male_FrameLayout)
        FrameLayout mStoreBannerFrameLayout;
        @BindView(R.id.store_banner_male)
        ConvenientBanner mStoreBannerMale;
        @BindView(R.id.store_banner_line)
        ImageView mStoreBannerLine;
        @BindView(R.id.store_entrance_grid_male)
        AdaptionGridViewNoMargin mStoreEntranceGridMale;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
