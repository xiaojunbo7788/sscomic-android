package com.ssreader.novel.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.ui.adapter.ComicChapterCatalogAdapter;
import com.ssreader.novel.ui.fragment.ComicinfoMuluFragment;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.comiclookview.SComicRecyclerView;
import com.ssreader.novel.ui.view.screcyclerview.RecycleViewMessageUtil;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ComicinfoMuluActivity extends BaseActivity {

    @BindView(R.id.fragment_comicinfo_mulu_list)
    SCRecyclerView fragment_comicinfo_mulu_list;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout noResultLayout;
    @BindView(R.id.fragment_option_noresult_text)
    TextView noResultText;

    @BindView(R.id.fragment_comicinfo_mulu_xu)
    TextView fragment_comicinfo_mulu_xu;
    @BindView(R.id.fragment_comicinfo_mulu_xu_img)
    ImageView fragment_comicinfo_mulu_xu_img;
    @BindView(R.id.fragment_comicinfo_mulu_layout)
    RelativeLayout fragmentComicinfoMuluLayout;
    @BindView(R.id.fragment_comicinfo_mulu_dangqian)
    LinearLayout fragmentComicinfoMuluDangqian;
    @BindView(R.id.fragment_comicinfo_mulu_zhiding_img)
    ImageView fragment_comicinfo_mulu_zhiding_img;
    @BindView(R.id.fragment_comicinfo_mulu_zhiding_text)
    TextView fragment_comicinfo_mulu_zhiding_text;
    @BindView(R.id.fragment_comicinfo_mulu_zhiding)
    LinearLayout fragmentComicinfoMuluZhiding;
    @BindView(R.id.fragment_comicinfo_mulu_dangqian_layout)
    RelativeLayout fragmentComicinfoMuluDangqianLayout;

    private boolean shunxu, clickChenage;
    private long currentChapter_id;
    private ComicChapterCatalogAdapter comicChapterCatalogAdapter;
    private List<ComicChapter> comicChapterCatalogs;
    private boolean orentation;
    private int size;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.BookInfoActivity_mulu;
        return R.layout.activity_comicinfo_mulu;
    }

    @Override
    public void initView() {
        noResultText.setText(LanguageUtil.getString(activity, R.string.app_no_catalog_bean));
        fragmentComicinfoMuluLayout.setVisibility(View.VISIBLE);
        initSCRecyclerView(fragment_comicinfo_mulu_list, RecyclerView.VERTICAL, 0);
        fragment_comicinfo_mulu_list.setLoadingMoreEnabled(false);
        fragment_comicinfo_mulu_list.setPullRefreshEnabled(false);
        comicChapterCatalogs = new ArrayList<>();
        setNoResultLayout(true);
        currentChapter_id = formIntent.getLongExtra("currentChapter_id", 0);
        Comic baseComic = (Comic) formIntent.getSerializableExtra("baseComic");
        List<ComicChapter> comicChapterList = ObjectBoxUtils.getComicChapterItemfData(baseComic.comic_id);
        if (!comicChapterList.isEmpty()) {
            size=comicChapterList.size();
            comicChapterCatalogs.addAll(comicChapterList);
            if (!comicChapterCatalogs.isEmpty()) {
                setNoResultLayout(false);
                comicChapterCatalogAdapter = new ComicChapterCatalogAdapter(comicChapterCatalogs, activity, null, currentChapter_id);
                fragment_comicinfo_mulu_list.setAdapter(comicChapterCatalogAdapter);
                // 跳转到指定位置
                jumpPosition();
            }
        } else {
            MyToash.ToashError(activity, R.string.chapterupdateing);
        }
       /* baseComic.GetCOMIC_catalog(activity, new ComicinfoMuluFragment.GetCOMIC_catalogList() {
            @Override
            public void GetCOMIC_catalogList(List<ComicChapter> comicChapterList) {
                if (comicChapterList != null && !comicChapterList.isEmpty()) {
                    comicChapterCatalogs.addAll(comicChapterList);
                    if (!comicChapterCatalogs.isEmpty()) {
                        setNoResultLayout(false);
                        comicChapterCatalogAdapter = new ComicChapterCatalogAdapter(comicChapterCatalogs, activity, null, currentChapter_id);
                        fragment_comicinfo_mulu_list.setAdapter(comicChapterCatalogAdapter);
                        // 跳转到指定位置
                        jumpPosition();
                    }
                } else {
                    MyToash.ToashError(activity, R.string.chapterupdateing);
                }
            }
        });*/
        isOrentation(false);
        initListener();
    }

    /**
     * 添加滑动监听
     */
    private void initListener() {
        fragment_comicinfo_mulu_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!clickChenage) {
                    if (Math.abs(dy) > 8) {
                        if (dy < 0) {
                            isOrentation(true);
                        } else {
                            // 下滑监听
                            isOrentation(false);
                        }
                    }
                }
                clickChenage = false;
            }
        });

        fragment_comicinfo_mulu_list.setScrollViewListener(new SComicRecyclerView.ScrollViewListener() {
            @Override
            public void onScroll(int FirstCompletelyVisibleItemPosition, int f, int LastCompletelyVisibleItemPosition, int l) {
                if (FirstCompletelyVisibleItemPosition == 0) {
                    clickChenage = true;
                    isOrentation(false);
                } else if (LastCompletelyVisibleItemPosition == size) {
                    clickChenage = true;
                    isOrentation(true);
                }
            }
        });
    }

    @OnClick({R.id.fragment_comicinfo_mulu_dangqian, R.id.fragment_comicinfo_mulu_zhiding, R.id.fragment_comicinfo_mulu_layout})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.fragment_comicinfo_mulu_layout:
                if (comicChapterCatalogs.isEmpty()) {
                    return;
                }
                shunxu = !shunxu;
                if (!shunxu) {
                    isOrentation(true);
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_zhengxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.positive_order);
                } else {
                    isOrentation(false);
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.reverse_order);

                }
                Collections.reverse(comicChapterCatalogs);
                setNoResultLayout(false);
                comicChapterCatalogAdapter.notifyDataSetChanged();
                break;
            case R.id.fragment_comicinfo_mulu_dangqian:
                if (comicChapterCatalogs.isEmpty()) {
                    return;
                }
                // 跳转到指定位置
                jumpPosition();
                break;
            case R.id.fragment_comicinfo_mulu_zhiding:
                if (comicChapterCatalogs.isEmpty()) {
                    return;
                }
                if (orentation) {
                    fragment_comicinfo_mulu_list.scrollToPosition(0);
                    isOrentation(false);
                } else {
                    fragment_comicinfo_mulu_list.scrollToPosition(comicChapterCatalogs.size());
                    isOrentation(true);
                }
                break;
        }
    }

    public void isOrentation(boolean orentation) {
        if (orentation) {
            this.orentation = true;
            fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gototop);
            fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoding));
        } else {
            this.orentation = false;
            fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gotobottom);
            fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daodi));
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }

    private void jumpPosition() {
        int position = comicChapterCatalogs.indexOf(new ComicChapter(currentChapter_id));
        if (position != -1) {
            position = (Math.min(position + 1, comicChapterCatalogs.size()));
            RecycleViewMessageUtil.MoveToPosition(layoutManager, fragment_comicinfo_mulu_list, position);
            // 判断是否到底到顶
            if (layoutManager.findFirstVisibleItemPosition() == 1) {
                isOrentation(false);
            } else if (layoutManager.findLastVisibleItemPosition() == comicChapterCatalogs.size()) {
                isOrentation(true);
            }
        }
    }

    private void setNoResultLayout(boolean isEmpty) {
        if (isEmpty) {
            fragment_comicinfo_mulu_list.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            fragment_comicinfo_mulu_list.setVisibility(View.VISIBLE);
        }
    }
}
