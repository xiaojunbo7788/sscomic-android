package com.ssreader.novel.ui.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.eventbus.ChapterBuyRefresh;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.adapter.ComicChapterCatalogAdapter;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.comiclookview.SComicRecyclerView;
import com.ssreader.novel.ui.view.screcyclerview.RecycleViewMessageUtil;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ComicinfoMuluFragment extends BaseFragment {

    @BindView(R.id.fragment_comic_info_catalog_top_layout)
    LinearLayout topLayout;
    @BindView(R.id.fragment_comicinfo_mulu_zhuangtai)
    TextView comicNewChapterText;
    @BindView(R.id.fragment_comicinfo_mulu_xu)
    public TextView fragment_comicinfo_mulu_xu;
    @BindView(R.id.fragment_comicinfo_mulu_xu_img)
    public ImageView fragment_comicinfo_mulu_xu_img;
    @BindView(R.id.fragment_comicinfo_mulu_layout)
    public RelativeLayout fragment_comicinfo_mulu_layout;
    @BindView(R.id.fragment_comicinfo_mulu_list)
    public SCRecyclerView fragment_comicinfo_mulu_list;

    @BindView(R.id.fragment_comic_info_noResult)
    NestedScrollView noResultLayout;

    private Comic baseComic;
    private long comic_id;
    private boolean shunxu;

    private ComicChapterCatalogAdapter comicChapterCatalogAdapter;
    private List<ComicChapter> comicChapterCatalogs;
    private ComicInfoActivity.MuluLorded muluLorded;

    private LinearLayout currentLayout, topBottomLayout;
    private ImageView positionImage;
    private TextView positionText;
    private AppBarLayout appBarLayout;

    private boolean orentation, clickChenage;
    private int size;

    public ComicinfoMuluFragment() {

    }

    @SuppressLint("ValidFragment")
    public ComicinfoMuluFragment(ComicInfoActivity.MuluLorded muluLorded, RelativeLayout relativeLayout, AppBarLayout appBarLayout) {
        this.muluLorded = muluLorded;
        positionImage = relativeLayout.findViewById(R.id.fragment_comicinfo_mulu_zhiding_img);
        positionText = relativeLayout.findViewById(R.id.fragment_comicinfo_mulu_zhiding_text);
        currentLayout = relativeLayout.findViewById(R.id.fragment_comicinfo_mulu_dangqian);
        topBottomLayout = relativeLayout.findViewById(R.id.fragment_comicinfo_mulu_zhiding);
        this.appBarLayout = appBarLayout;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_comicinfo_mulu;
    }

    @Override
    public void initView() {
        initSCRecyclerView(fragment_comicinfo_mulu_list, RecyclerView.VERTICAL, 0);
        comicChapterCatalogs = new ArrayList<>();
        fragment_comicinfo_mulu_list.setLoadingMoreEnabled(false);
        fragment_comicinfo_mulu_list.setPullRefreshEnabled(false);
        setNoResultLayout(true);
        fragment_comicinfo_mulu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comicChapterCatalogs.isEmpty()) {
                    return;
                }
                shunxu = !shunxu;
                if (!shunxu) {
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_zhengxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.positive_order);
                } else {
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.reverse_order);
                }
                Collections.reverse(comicChapterCatalogs);
                comicChapterCatalogAdapter.notifyDataSetChanged();
                comicChapterCatalogAdapter.shunxu = shunxu;
            }
        });
        orentation = true;
        setPositionUi(false);
        //添加滑动监听
        fragment_comicinfo_mulu_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!clickChenage) {
                    if (Math.abs(dy) > 8) {
                        if (dy < 0) {
                            setPositionUi(true);
                        } else {
                            // 下滑监听
                            setPositionUi(false);
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
                    setPositionUi(false);
                } else if (LastCompletelyVisibleItemPosition == size) {
                    clickChenage = true;
                    setPositionUi(true);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (activity == null && comic_id == 0) {
            return;
        }
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("comic_id", comic_id);
        String json = readerParams.generateParamsJson();
        httpUtils.sendRequestRequestParams(activity, Api.COMIC_down_option, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JsonParser jsonParser = new JsonParser();
                            JsonArray jsonElements = jsonParser.parse(jsonObject.getString("down_list")).getAsJsonArray();//获取JsonArray对象
                            int i = 0;
                            for (JsonElement jsonElement : jsonElements) {
                                if (!comicChapterCatalogs.isEmpty() && comicChapterCatalogs.size() - 1 >= i) {
                                    ComicChapter comicChapter = gson.fromJson(jsonElement, ComicChapter.class);
                                    comicChapterCatalogs.get(i).is_preview = comicChapter.is_preview;
                                }
                                ++i;
                            }
                            comicChapterCatalogAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    /**
     * 刷新列表
     *
     * @param chapterBuyRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(ChapterBuyRefresh chapterBuyRefresh) {
        if (chapterBuyRefresh.productType != Constant.COMIC_CONSTANT) {
            return;
        }
        if (!chapterBuyRefresh.IsRead) {
            for (long id : chapterBuyRefresh.ids) {
                FLAG:
                for (ComicChapter comicChapter : comicChapterCatalogs) {
                    if (id == comicChapter.chapter_id) {
                        comicChapter.is_preview = 0;
                        break FLAG;
                    }
                }
            }
        } else {
            List<ComicChapter> localComicChapters = ObjectBoxUtils.getComicChapterItemfData(baseComic.comic_id);
            if (localComicChapters != null && !localComicChapters.isEmpty()) {
                for (ComicChapter localChapter : localComicChapters) {
                    FLAG:
                    for (ComicChapter comicChapter : comicChapterCatalogs) {
                        if (localChapter.chapter_id == comicChapter.chapter_id) {
                            comicChapter.IsRead = localChapter.IsRead;
                            comicChapter.is_preview = localChapter.is_preview;
                            comicChapter.current_read_img_order = localChapter.current_read_img_order;
                            break FLAG;
                        }
                    }
                }
            }
            comicChapterCatalogAdapter.setCurrentChapterId(chapterBuyRefresh.chapterId);
        }
        comicChapterCatalogAdapter.notifyDataSetChanged();
        if (chapterBuyRefresh.IsRead) {
            setPositionControl(true, appBarLayout);
        }
    }

    /**
     * 用户下载刷新下载数据
     *
     * @param comic
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnComicDown(Comic comic) {
        if (comic.comic_id == baseComic.comic_id) {
            if (comic.down_chapters != 0) {
                baseComic.down_chapters = comic.down_chapters;
            }
        }
    }

    /**
     * 设置数据
     *
     * @param baseComic
     * @param isHttpData
     */
    public void senddata(Comic baseComic, boolean isHttpData) {
        this.baseComic = baseComic;
        if (isHttpData && baseComic != null) {
            comicNewChapterText.setText(baseComic.getFlag() == null ? "" : baseComic.getFlag());
            comic_id = baseComic.getComic_id();
            comicChapterCatalogAdapter = new ComicChapterCatalogAdapter(comicChapterCatalogs, activity, baseComic, baseComic.getCurrent_chapter_id());
            fragment_comicinfo_mulu_list.setAdapter(comicChapterCatalogAdapter);
            // 获取目录
            baseComic.GetCOMIC_catalog(activity, new ComicinfoMuluFragment.GetCOMIC_catalogList() {
                @Override
                public void GetCOMIC_catalogList(List<ComicChapter> comicChapterList) {
                    if (comicChapterList != null && !comicChapterList.isEmpty()) {
                        comicChapterCatalogs.clear();
                        comicChapterCatalogs.addAll(comicChapterList);
                        if (comicChapterCatalogs != null && !comicChapterCatalogs.isEmpty()) {
                            setNoResultLayout(false);
                            size = comicChapterCatalogs.size();
                            comicChapterCatalogAdapter.notifyDataSetChanged();
                        }
                        muluLorded.getReadChapterItem(comicChapterCatalogs);
                    } else {
                        MyToash.ToashError(activity, R.string.chapterupdateing);
                    }
                }
            });
        } else if (!isHttpData && baseComic != null) {
            // 更新适配器中的comic
            comicChapterCatalogAdapter.baseComic = baseComic;
        }
    }

    /**
     * 列表位置控制
     *
     * @param isCurrent
     * @param appBarLayout
     */
    public void setPositionControl(boolean isCurrent, AppBarLayout appBarLayout) {
        if (comicChapterCatalogAdapter == null || comicChapterCatalogs == null || comicChapterCatalogs.isEmpty()) {
            return;
        }
        if (isCurrent) {
            // 当前
            int index = comicChapterCatalogs.indexOf(new ComicChapter(baseComic.current_chapter_id));
            if (index == -1) {
                return;
            }
            RecycleViewMessageUtil.MoveToPosition(layoutManager, fragment_comicinfo_mulu_list, index + 1);
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (appBarLayout != null) {
                if (index > lastVisibleItemPosition) {
                    scrollToCoordinatorLayout(appBarLayout, false);
                } else if (index < firstVisibleItemPosition - 1) {
                    scrollToCoordinatorLayout(appBarLayout, true);
                }
            }
            if (index == 0) {
                setPositionUi(false);
            } else if (index == size - 1) {
                setPositionUi(true);
            }
        } else {
            // 置顶、置底
            if (orentation) {
                fragment_comicinfo_mulu_list.scrollToPosition(0);
                scrollToCoordinatorLayout(appBarLayout, true);
            } else {
                fragment_comicinfo_mulu_list.scrollToPosition(size);
                scrollToCoordinatorLayout(appBarLayout, false);
            }
            setPositionUi(!orentation);
        }
        clickChenage = true;
    }

    /**
     * 控制AppBarLayout
     *
     * @param appBarLayout
     * @param isTop
     */
    private void scrollToCoordinatorLayout(AppBarLayout appBarLayout, boolean isTop) {
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            if (isTop && appBarLayoutBehavior.getTopAndBottomOffset() < 0) {
                // 快速滑动到顶部
                appBarLayoutBehavior.setTopAndBottomOffset(0);
            } else if (!isTop && appBarLayoutBehavior.getTopAndBottomOffset() >= 0) {
                // 快速滑动实现吸顶效果
                appBarLayoutBehavior.setTopAndBottomOffset(-appBarLayout.getHeight());
            }
        }
    }

    /**
     * 更改UI
     *
     * @param isTop
     */
    private void setPositionUi(boolean isTop) {
        if (isTop != orentation && positionImage != null && positionText != null) {
            if (isTop) {
                positionImage.setImageResource(R.mipmap.comicdetail_gototop);
                positionText.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoding));
            } else {
                positionImage.setImageResource(R.mipmap.comicdetail_gotobottom);
                positionText.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daodi));
            }
            orentation = isTop;
        }
    }

    private void setNoResultLayout(boolean isEmpty) {
        if (isEmpty) {
            topLayout.setVisibility(View.GONE);
            fragment_comicinfo_mulu_list.setVisibility(View.GONE);
            if (currentLayout != null) {
                currentLayout.setVisibility(View.GONE);
            }
            if (topBottomLayout != null) {
                topBottomLayout.setVisibility(View.GONE);
            }
            noResultLayout.setVisibility(View.VISIBLE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            topLayout.setVisibility(View.VISIBLE);
            if (currentLayout != null) {
                currentLayout.setVisibility(View.VISIBLE);
            }
            if (topBottomLayout != null) {
                topBottomLayout.setVisibility(View.VISIBLE);
            }
            if (fragment_comicinfo_mulu_list.getVisibility() == View.GONE) {
                fragment_comicinfo_mulu_list.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface GetCOMIC_catalogList {

        void GetCOMIC_catalogList(List<ComicChapter> comicChapterList);
    }
}
