package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshComicShelf;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.model.BaseTag;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.model.ComicInfo;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.eventbus.CommentRefresh;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.MyFragmentPagerAdapter;
import com.ssreader.novel.ui.fragment.ComicinfoCommentFragment;
import com.ssreader.novel.ui.fragment.ComicinfoMuluFragment;
import com.ssreader.novel.ui.utils.AndroidWorkaround;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.PaletteHelper;
import com.ssreader.novel.ui.view.StarLayout;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.ui.utils.MyShape.setMyshapeComicBg;
import static com.ssreader.novel.ui.utils.StatusBarUtil.setStatusTextColor;

public class ComicInfoActivity extends BaseActivity {

    @BindView(R.id.fragment_comicinfo_viewpage)
    public ViewPager fragment_comicinfo_viewpage;
    @BindView(R.id.comic_info_XTabLayout_layout)
    RelativeLayout selectionXTabBgLayout;
    @BindView(R.id.comic_info_XTabLayout)
    SmartTabLayout public_selection_XTabLayout;

    @BindView(R.id.activity_comic_info_topBar_layout)
    LinearLayout topBarLayout;
    @BindView(R.id.activity_comic_info_topbar_downlayout)
    public RelativeLayout activity_comic_info_topbar_downlayout;
    @BindView(R.id.activity_comic_info_topbar_sharelayout)
    public RelativeLayout activity_comic_info_topbar_sharelayout;

    @BindView(R.id.fragment_comic_info_current_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.fragment_comicinfo_current_goonread)
    public TextView fragment_comicinfo_current_goonread;
    @BindView(R.id.fragment_comicinfo_current_chaptername)
    public TextView fragment_comicinfo_current_chaptername;

    @BindView(R.id.activity_comic_info_top_bookname)
    public TextView activity_comic_info_top_bookname;
    @BindView(R.id.activity_book_info_content_cover)
    public ImageView activity_book_info_content_cover;
    @BindView(R.id.activity_book_info_content_name)
    public TextView activity_book_info_content_name;
    @BindView(R.id.activity_book_info_content_star)
    public StarLayout mStarLayout;
    @BindView(R.id.activity_book_info_content_total_hot)
    public TextView activity_book_info_content_total_hot;
    @BindView(R.id.activity_book_info_content_shoucang)
    public TextView activity_book_info_content_shoucang;
    @BindView(R.id.activity_book_info_content_shoucannum)
    public TextView activity_book_info_content_shoucannum;
    @BindView(R.id.activity_book_info_content_tag)
    public TagFlowLayout activity_book_info_content_tag;
    @BindView(R.id.activity_book_info_content_cover_bg)
    public ImageView activity_book_info_content_cover_bg;
    @BindView(R.id.activity_comic_info_view)
    View activity_comic_info_view;
    @BindView(R.id.activity_comic_info_AppBarLayout)
    public AppBarLayout activity_comic_info_AppBarLayout;
    @BindView(R.id.activity_comic_info_CollapsingToolbarLayout)
    public CollapsingToolbarLayout activity_comic_info_CollapsingToolbarLayout;
    @BindView(R.id.activity_comic_info_comment_layout)
    public LinearLayout activity_comic_info_comment_layout;
    @BindView(R.id.activity_comic_info_topbar)
    public View activity_comic_info_topbar;

    @BindView(R.id.fragment_comicinfo_mulu_dangqian_layout)
    RelativeLayout positionControlLayout;

    private List<Fragment> fragmentList;
    private List<String> tabList;
    private List<TextView> textViewList;
    private List<Comment> bookInfoComment;
    private List<ComicChapter> comicChapter;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private ComicinfoCommentFragment comicFragment;
    private ComicinfoMuluFragment muluFragment;

    private Comic baseComic, baseComicLocal;
    private BaseBookComic comic;
    private BaseLabelBean stroreComicLable;
    private long comic_id;
    private BaseAd baseAd;
    private int activity_comic_info_topbarD;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        FULL_CCREEN = true;
        return R.layout.activity_comic_info;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStatusTextColor(false, this);
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) { //适配华为手机虚拟键遮挡tab的问题
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));//需要在setContentView()方法后面执行
        }
        if (!Constant.USE_SHARE) {
            activity_comic_info_topbar_sharelayout.setVisibility(View.INVISIBLE);
        }
        activity_comic_info_view.setBackground(setMyshapeComicBg(activity));

        activity_book_info_content_shoucang.setBackground(MyShape.setMyCustomizeShape(activity, 12, R.color.maincolor));
        muluFragment = new <Fragment>ComicinfoMuluFragment(muluLorded, positionControlLayout, activity_comic_info_AppBarLayout);
        comicFragment = new <Fragment>ComicinfoCommentFragment();
        fragmentList = new ArrayList<>();
        textViewList = new ArrayList<>();
        fragmentList.add(comicFragment);
        fragmentList.add(muluFragment);
        tabList = new ArrayList<>();
        tabList.add(LanguageUtil.getString(activity, R.string.fragment_comic_info_xiangqing));
        tabList.add(LanguageUtil.getString(activity, R.string.BookInfoActivity_mulu));
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        fragment_comicinfo_viewpage.setAdapter(myFragmentPagerAdapter);
        bookInfoComment = new ArrayList<>();
        activity_comic_info_AppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int onScrolled_y) {
                int REFRESH_HEIGHT = appBarLayout.getTotalScrollRange();
                onScrolled_y *= -1;
                final float ratio = (float) Math.min(Math.max(onScrolled_y, 0), REFRESH_HEIGHT) / REFRESH_HEIGHT;
                activity_comic_info_topbar.setAlpha(ratio);
                activity_comic_info_top_bookname.setAlpha(ratio);
            }
        });

        fragment_comicinfo_viewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabList.get(position);
            }
        });
        public_selection_XTabLayout.setViewPager(fragment_comicinfo_viewpage);
        textViewList.add(public_selection_XTabLayout.getTabAt(0).findViewById(R.id.item_tablayout_text));
        textViewList.add(public_selection_XTabLayout.getTabAt(1).findViewById(R.id.item_tablayout_text));
        textViewList.get(0).setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
        fragment_comicinfo_viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textViewList.get(position).setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                textViewList.get(1 - position).setTextColor(ContextCompat.getColor(activity, R.color.black));
                switch (position) {
                    case 0:
                        positionControlLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        positionControlLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        comic_id = formIntent.getLongExtra("comic_id", 0);
        if (comic_id == 0) {
            baseComic = (Comic) formIntent.getSerializableExtra("baseComic");
            if (baseComic != null) {
                comic_id = baseComic.getComic_id();
            }
        }
        if (comic_id == 0) {
            return;
        }
        if (baseComic == null) {
            baseComic = new Comic();
            baseComic.setComic_id(comic_id);
        }
        baseComicLocal = ObjectBoxUtils.getComic(comic_id);
        if (baseComicLocal != null) {
            baseComic.is_read = baseComicLocal.is_read;
            baseComic.is_collect = baseComicLocal.is_collect;
            baseComic.setCurrent_chapter_id(baseComicLocal.getCurrent_chapter_id());
            baseComic.setCurrent_display_order(baseComicLocal.getCurrent_display_order());
            baseComic.setCurrent_chapter_name(baseComicLocal.getCurrent_chapter_name());
            baseComic.setChapter_text(baseComicLocal.getChapter_text());
            baseComic.setDown_chapters(baseComicLocal.getDown_chapters());
        }
        setCollect(baseComic.is_collect == 1);
        if (baseComic.is_read == 1) {
            fragment_comicinfo_current_goonread.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_goon_read));
        }
        setNoNetLayout();
    }

    @OnClick(value = {R.id.fragment_comicinfo_current_goonread, R.id.titlebar_back,
            R.id.activity_comic_info_topbar_sharelayout, R.id.activity_comic_info_topbar_downlayout,
            R.id.fragment_comicinfo_mulu_dangqian, R.id.fragment_comicinfo_mulu_zhiding
            , R.id.activity_book_info_content_shoucang})
    public void getEvent(View view) {
        int view_id = view.getId();
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            if (view_id == R.id.titlebar_back) {
                if (!BWNApplication.applicationContext.isMainActivityStartUp()) {
                    startActivity(new Intent(activity, MainActivity.class));
                }
                finish();
            } else if (baseComic != null && baseComic.name != null) {
                Intent intent = new Intent();
                if (view_id == R.id.fragment_comicinfo_current_goonread || view_id == R.id.activity_comic_info_topbar_downlayout ||
                        view_id == R.id.fragment_comicinfo_mulu_dangqian || view_id == R.id.fragment_comicinfo_mulu_zhiding) {
                    if (comicChapter != null && !comicChapter.isEmpty()) {
                        switch (view_id) {
                            case R.id.fragment_comicinfo_current_goonread:
                                fragment_comicinfo_current_goonread.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_goon_read));
                                ObjectBoxUtils.addData(baseComic, Comic.class);
                                intent.setClass(activity, ComicLookActivity.class);
                                intent.putExtra("baseComic", baseComic);
                                startActivity(intent);
                                break;
                            case R.id.activity_comic_info_topbar_downlayout:
                                ObjectBoxUtils.addData(baseComic, Comic.class);
                                intent.setClass(activity, ComicDownActivity.class);
                                intent.putExtra("baseComic", baseComic);
                                startActivity(intent);
                                break;
                            case R.id.fragment_comicinfo_mulu_dangqian:
                                muluFragment.setPositionControl(true, activity_comic_info_AppBarLayout);
                                break;
                            case R.id.fragment_comicinfo_mulu_zhiding:
                                muluFragment.setPositionControl(false, activity_comic_info_AppBarLayout);
                                break;
                        }
                    } else {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, InternetUtils.internet(activity) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
                    }
                } else
                    switch (view_id) {
                        case R.id.activity_comic_info_topbar_sharelayout:
                            new MyShare(activity)
                                    .setFlag(Constant.COMIC_CONSTANT)
                                    .setId(baseComic.getComic_id())
                                    .Share();
                            break;
                        case R.id.activity_book_info_content_shoucang:
                            if (baseComic.is_collect == 0) {
                                baseComic.is_collect = 1;
                                ObjectBoxUtils.addData(baseComic, Comic.class);
                                EventBus.getDefault().post(new RefreshShelf(Constant.COMIC_CONSTANT, new RefreshComicShelf(baseComic, 1)));
                                MyToash.ToashSuccess(activity, LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
                                setCollect(baseComic.is_collect == 1);
                            }
                            break;
                    }
            } else {
                if (view.getId() != R.id.fragment_comicinfo_mulu_dangqian && view.getId() != R.id.fragment_comicinfo_mulu_zhiding) {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, InternetUtils.internet(activity) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
                }
            }
        }
    }

    @Override
    public void initData() {
        if (comic_id != 0) {
            readerParams = new ReaderParams(activity);
            readerParams.putExtraParams("comic_id", comic_id);
            httpUtils.sendRequestRequestParams(activity, Api.COMIC_info, readerParams.generateParamsJson(), responseListener);
        }
    }

    @Override
    public void initInfo(String json) {
        ComicInfo comicInfo = gson.fromJson(json, ComicInfo.class);
        comicFragment.setComicInfo(comicInfo.comic);
        bookInfoComment = comicInfo.comment;
        if (http_flag == 0) {
            comic = comicInfo.comic;
            if (comicInfo.label != null && !comicInfo.label.isEmpty()) {
                stroreComicLable = comicInfo.label.get(0);
            }
            if (comicInfo.advert != null) {
                baseAd = comicInfo.advert;
            }
            handdata(comicInfo.comic.total_comment);
        } else {
            comicFragment.setComment(bookInfoComment, comicInfo.comic.total_comment);
        }
    }

    private void setNoNetLayout() {
        if (!InternetUtils.internet(activity)) {
            MyToash.ToashError(activity, R.string.splashactivity_nonet);
            topBarLayout.setVisibility(View.GONE);
            activity_book_info_content_shoucang.setVisibility(View.GONE);
            selectionXTabBgLayout.setVisibility(View.GONE);
            fragment_comicinfo_viewpage.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }
    }

    public void handdata(int total_comment) {
        MyGlide.GlideImageRoundedCorners(6, activity, comic.vertical_cover, activity_book_info_content_cover, ImageUtil.dp2px(activity, 135), ImageUtil.dp2px(activity, 180));
        String bgUrl;
        if (!TextUtils.isEmpty(comic.horizontal_cover)) {
            bgUrl = comic.horizontal_cover;
            baseComic.setHorizontal_cover(comic.horizontal_cover);
        } else {
            bgUrl = comic.vertical_cover;
        }
        MyGlide.GlideImageRoundedGasoMohu(activity, bgUrl, activity_book_info_content_cover_bg);
        activity_book_info_content_cover_bg.setAlpha(0.75f);
        Glide.with(this).asBitmap().load(bgUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                PaletteHelper.setPaletteColor(resource, new PaletteHelper.PaletteHelperColor() {
                    @Override
                    public void getColor(int color) {
                        activity_comic_info_topbarD = color;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity_comic_info_topbar.setBackgroundColor(activity_comic_info_topbarD);
                            }
                        });
                    }
                });
            }
        });

        activity_book_info_content_name.setText(comic.name);
        fragment_comicinfo_current_chaptername.setText(baseComic.getCurrent_chapter_name() == null ? "" : baseComic.getCurrent_chapter_name());
        activity_book_info_content_total_hot.setText(comic.hot_num);
        activity_book_info_content_shoucannum.setText(comic.total_favors);
        activity_comic_info_top_bookname.setText(comic.name);
        int dp6 = ImageUtil.dp2px(activity, 6);
        int dp3 = ImageUtil.dp2px(activity, 3);
        if (comic.tag != null && !comic.tag.isEmpty()) {
            activity_book_info_content_tag.setAdapter(new TagAdapter<BaseTag>(comic.tag) {
                @Override
                public View getView(FlowLayout parent, int position, BaseTag tag) {
                    TextView textView = new TextView(activity);
                    textView.setText(tag.getTab());
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    textView.setLines(1);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(dp6, dp3, dp6, dp3);
                    textView.setTextColor(Color.parseColor(tag.getColor()));
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(20);
                    drawable.setColor(Color.parseColor("#1A" + tag.getColor().substring(1)));
                    textView.setBackground(drawable);
                    return textView;
                }
            });
        }
//        mStarLayout
//        activity_book_info_content_author.setText(comic.author.replaceAll(",", " "));
        baseComic.author = comic.author.replaceAll(",", " ");
        baseComic.setVertical_cover(comic.vertical_cover);
        baseComic.setName(comic.name);
        baseComic.setDescription(comic.description);
        baseComic.setFlag(comic.flag);
        baseComic.setTotal_chapters(comic.total_chapters);
        // 加载推荐
        comicFragment.senddata(comic, bookInfoComment, stroreComicLable, baseAd, total_comment);
        // 加载目录
        muluFragment.senddata(baseComic, true);
    }

    /**
     * 刷新评论
     *
     * @param refreshBookInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(CommentRefresh refreshBookInfo) {
        if (refreshBookInfo.productType == Constant.COMIC_CONSTANT && baseComic.comic_id == refreshBookInfo.id) {
            http_flag = 1;
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBookSelf(RefreshShelfCurrent s) {
        if (baseComic != null && comicChapter != null) {
            if (s.productType == Constant.COMIC_CONSTANT) {
                if (s.comic.comic_id == comic_id) {
                    setCollect(s.comic.is_collect == 1);
                    baseComic = s.comic;
                    fragment_comicinfo_current_chaptername.setText(s.comic.getCurrent_chapter_name() == null ? "" : baseComic.getCurrent_chapter_name());
                    muluFragment.senddata(baseComic, false);
                }
            }
        }
    }

    /**
     * 设置收藏按钮的样式
     *
     * @param is_collect
     */
    private void setCollect(boolean is_collect) {
        activity_book_info_content_shoucang.setText(LanguageUtil.getString(this,
                is_collect ? R.string.BookInfoActivity_collection : R.string.fragment_comic_info_shoucang));
        if (!is_collect) {
            activity_book_info_content_shoucang.setBackground(MyShape.setMyCustomizeShape(activity, 12, R.color.maincolor));
        } else {
            activity_book_info_content_shoucang.setBackground(MyShape.setMyCustomizeShape(activity, 12, R.color.maincolor_75));
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
                muluFragment.senddata(baseComic, false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!BWNApplication.applicationContext.isMainActivityStartUp()) {
                startActivity(new Intent(ComicInfoActivity.this, MainActivity.class));
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    MuluLorded muluLorded = new MuluLorded() {
        @Override
        public void getReadChapterItem(List<ComicChapter> comicChapter1) {
            if (comicChapter1 != null && !comicChapter1.isEmpty()) {
                comicChapter = comicChapter1;
                baseComic.setTotal_chapters(comicChapter1.size());
                if (baseComic.getCurrent_chapter_name() == null) {
                    fragment_comicinfo_current_chaptername.setText(comicChapter.get(0).chapter_title);
                }
            } else {
                if (comic != null && !TextUtils.isEmpty(comic.name)) {
                    fragment_comicinfo_current_chaptername.setText(comic.name);
                }
            }
        }
    };

    public interface MuluLorded {

        void getReadChapterItem(List<ComicChapter> comicChapter);
    }
}
