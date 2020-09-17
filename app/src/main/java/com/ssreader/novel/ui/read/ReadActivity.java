package com.ssreader.novel.ui.read;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.eventbus.RefreshBookInfoStatusBar;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.ui.activity.CommentActivity;
import com.ssreader.novel.ui.bwad.AdHttp;
import com.ssreader.novel.ui.bwad.AdReadCachePool;
import com.ssreader.novel.ui.bwad.TTAdShow;
import com.ssreader.novel.ui.dialog.BookDownDialogFragment;
import com.ssreader.novel.ui.read.page.PageMode;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.UserUtils;
import com.ssreader.novel.utils.cache.BitmapCache;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AudioProgressRefresh;
import com.ssreader.novel.eventbus.AudioServiceRefresh;
import com.ssreader.novel.eventbus.BookBottomTabRefresh;
import com.ssreader.novel.eventbus.RefreshPageFactoryChapter;
import com.ssreader.novel.eventbus.RefreshBookInfo;
import com.ssreader.novel.eventbus.RefreshBookSelf;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.eventbus.AdStatusRefresh;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.ui.activity.BookCatalogMarkActivity;
import com.ssreader.novel.ui.activity.BookEndRecommendActivity;
import com.ssreader.novel.ui.audio.AudioAiActivity;
import com.ssreader.novel.ui.dialog.BookReadDialogFragment;
import com.ssreader.novel.ui.dialog.PublicDialog;
import com.ssreader.novel.ui.dialog.PublicPurchaseDialog;
import com.ssreader.novel.ui.read.dialog.AutoProgress;
import com.ssreader.novel.ui.read.dialog.AutoSettingDialog;
import com.ssreader.novel.ui.read.dialog.BrightnessDialog;
import com.ssreader.novel.ui.read.dialog.ReadHeadMoreDialog;
import com.ssreader.novel.ui.read.dialog.SettingDialog;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.read.util.BrightnessUtil;
import com.ssreader.novel.ui.audio.view.AudioProgressLayout;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.read.manager.ReadSettingManager;
import com.ssreader.novel.ui.read.page.PageView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.*;
import static com.ssreader.novel.ui.bwad.AdHttp.adClick;

public class ReadActivity extends BaseReadActivity {

    @BindView(R.id.read_PageView)
    public PageView mPvPage;
    @BindView(R.id.activity_read_top_menu)
    View activity_read_top_menu;
    @BindView(R.id.bookpop_bottom)
    View bookpop_bottom;

    @BindView(R.id.book_read_bottom_comment)
    LinearLayout commentLayout;
    @BindView(R.id.activity_read_bottom_view)
    RelativeLayout activity_read_bottom_view;
    @BindView(R.id.activity_read_audio_layout)
    AudioProgressLayout audioProgressLayout;
    @BindView(R.id.activity_read_change_day_night)
    ImageView activity_read_change_day_night;

    @BindViews({R.id.toolbar_into_reward, R.id.toolbar_into_audio, R.id.toolbar_into_down, R.id.toolbar_into_more})
    List<RelativeLayout> readToolBarLayouts;
    @BindView(R.id.activity_read_firstread)
    ImageView activity_read_firstread;
    @BindView(R.id.auto_read_progress_bar)
    ProgressBar auto_read_progress_bar;

    @BindView(R.id.list_ad_view_layout)
    public FrameLayout insert_todayone2;
    @BindView(R.id.activity_read_buttom_ad_layout)
    public FrameLayout activity_read_buttom_ad_layout;

    // 作家的话
    @BindView(R.id.book_first_author_note_layout)
    View authorFistNoteLayout;
    @BindView(R.id.public_book_bottom_reward_layout)
    View rewardLayout;
    @BindView(R.id.public_book_author_words_layout)
    View authorWordsLayout;
    // 付费布局
    @BindView(R.id.activity_read_purchase_layout)
    View purchaseLayout;

    @BindView(R.id.book_read_try_layout)
    public View bookReadTryLayout;
    @BindView(R.id.book_read_load_image)
    public ImageView bookReadLoad;

    private Boolean isShow = false;
    private BrightnessDialog mBrightDialog;
    private SettingDialog mSettingDialog;
    private AutoSettingDialog mAutoSettingDialog;
    // 购买弹窗
    private PublicPurchaseDialog purchaseDialog;
    // 动画
    private AnimationDrawable animationDrawable;

    public PageView getBookPage() {
        return mPvPage;
    }

    @Override
    protected void initActivity() {
        activity = this;
    }

    @Override
    public int initContentView() {
        return R.layout.activity_read;
    }

    @Override
    public void initView() {
        if (ShareUitls.getString(ReadActivity.this, "FirstRead", "yes").equals("yes")) {
            ShareUitls.putString(ReadActivity.this, "FirstRead", "no");
            activity_read_firstread.setVisibility(View.VISIBLE);
            activity_read_firstread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_read_firstread.setVisibility(View.GONE);
                }
            });
        }
        if (Constant.getMonthlyTicket(activity) == 1 || Constant.getRewardSwitch(activity) == 1) {
            readToolBarLayouts.get(0).setVisibility(View.VISIBLE);
        } else {
            readToolBarLayouts.get(0).setVisibility(View.GONE);
        }
        activity_read_top_menu.setBackground(MyShape.setMyshape(0, ContextCompat.getColor(activity, R.color.white)));
        bookpop_bottom.setBackground(MyShape.setMyshape(0, ContextCompat.getColor(activity, R.color.white)));
        if (isNotchEnable == 0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) activity_read_top_menu.getLayoutParams();
            layoutParams.height = ImageUtil.dp2px(this, 70);
            activity_read_top_menu.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void initData() {
        mSettingDialog = new SettingDialog(this, baseBook);
        mBrightDialog = new BrightnessDialog(this);
        mAutoSettingDialog = new AutoSettingDialog(this);
        mAutoSettingDialog.setSettingDialog(mSettingDialog);
        isNightMode = ReadSettingManager.getInstance().isNightMode();
        if (!isNightMode) {
            BrightnessUtil.setBrightness(this, config.getBrightness());
        }
        initDayOrNight();
        //壳子SD本地书籍
        if (mBookId > LOCAL_BOOKID) {
            commentLayout.setVisibility(View.GONE);
            readToolBarLayouts.get(0).setVisibility(View.GONE);
            readToolBarLayouts.get(1).setVisibility(View.GONE);
            readToolBarLayouts.get(2).setVisibility(View.GONE);
            readToolBarLayouts.get(3).setVisibility(View.GONE);
        } else {
            if (!Constant.USE_AUDIO_AI) {
                readToolBarLayouts.get(1).setVisibility(View.GONE);
            } else {
                readToolBarLayouts.get(1).setVisibility(View.VISIBLE);
            }
        }
        // 获取页面加载器
        mPageLoader = mPvPage.getPageLoader(activity, baseBook, getIntent().getLongExtra("mark_id", 0));
        // 广告
        adInit(true);
        // 打开界面
        adDataInitComplete();
        initListener();
    }

    @Override
    protected void adDataInitComplete() {
        mPageLoader.bitmapCache = bitmapCache;
        mPvPage.bitmapCache = bitmapCache;
        authorFistNoteLayout.post(new Runnable() {
            @Override
            public void run() {
                if (BitmapCache.getInstance().getBitmapFromCache("purchase") == null) {
                    mPageLoader.initPurchaseLayout();
                }
                if (initPageMode != PageMode.SCROLL) {
                    mPageLoader.refreshChapterList();
                }
            }
        });
    }

    /**
     * 加载广告
     *
     * @param isFirstOpenBook
     */
    public void adInit(boolean isFirstOpenBook) {
        // 底部广告
        if (getIsReadBottomAd(activity)) {
            frameLayoutButton = activity_read_buttom_ad_layout;
            activity_read_buttom_ad_layout.setVisibility(View.VISIBLE);
            if ((baseAdButton = AdReadCachePool.getInstance().getReadBaseAd(3)) != null) {
                getBaseAdCenter(3);
                if (baseAdButton.ad_type != 1) {
                    handler.sendEmptyMessageDelayed(3, 30000);
                }
            }
            ViewGroup.LayoutParams layoutParams = frameLayoutButton.getLayoutParams();
            layoutParams.width = mWidth;
            layoutParams.height = Constant.getReadBottomHeight(activity);
            frameLayoutButton.setLayoutParams(layoutParams);
        } else {
            // 去掉广告
            activity_read_buttom_ad_layout.setVisibility(View.GONE);
            baseAdButton = null;
        }
        // 章节中部广告
        if (getIsReadCenterAd(activity)) {
            int AD_WIDTH = (mWidth - ImageUtil.dp2px(activity, 30));
            AD_H = (int) ((float) AD_WIDTH / 1.2f);
            AD_TOP = (mHeight - AD_H) / 2;
            mPageLoader.initAD(frameLayoutCenter, AD_H, AD_TOP);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) insert_todayone2.getLayoutParams();
            layoutParams.topMargin = AD_TOP;
            layoutParams.width = AD_WIDTH;
            layoutParams.height = AD_H;
            insert_todayone2.setLayoutParams(layoutParams);
            frameLayoutCenter = insert_todayone2;
            PvPage = mPvPage;

            if ((baseAdCenter = AdReadCachePool.getInstance().getReadBaseAd(0)) != null) {
                if (baseAdCenter.ad_type != 1) {
                    int S5 = ImageUtil.dp2px(activity, 5);
                    frameLayoutCenter.setPadding(S5, S5, S5, S5);
                    frameLayoutCenter.setBackground(MyShape.setMyshape(S5, Color.WHITE));
                }
                mPageLoader.baseAd = baseAdCenter;
                PvPage.baseAd = baseAdCenter;
                getBaseAdCenter(0);
                if (getIsUseVideoAd(activity)) {
                    CloseAdEnd();
                }
                mPvPage.setADview(insert_todayone2);
            }

        } else {
            insert_todayone2.setVisibility(View.GONE);
            baseAdCenter = null;
        }
        // 刷新广告
        if (!isFirstOpenBook) {
            mPvPage.initScreenSize(0, ScreenSizeUtils.getInstance(activity).getScreenHeight(), true);
            mPageLoader = mPvPage.getPageLoader(activity, baseBook, 0);
            if (initPageMode != PageMode.SCROLL) {
                // 刷新数据
                mPageLoader.refreshChapterList();
            }
        }
    }

    protected void initListener() {
        mPvPage.setTouchListener(ReadActivity.this, new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return !hideReadSetting();
            }

            @Override
            public void center(boolean isCancleDialog) {
                if (mAutoSettingDialog != null) {
                    if (AutoProgress.getInstance().isStarted()) {
                        if (!mAutoSettingDialog.isShowing()) {
                            AutoProgress.getInstance().pause();
                            mAutoSettingDialog.show();
                        }
                        return;
                    }
                }
                if (isCancleDialog) {
                    showReadSetting();
                }
            }

            @Override
            public void prePage() {
                if (AutoProgress.getInstance().isStarted()) {
                    AutoProgress.getInstance().restart();
                }
            }

            @Override
            public void nextPage(boolean isHasNext, boolean isAuto) {
                if (AutoProgress.getInstance().isStarted()) {
                    AutoProgress.getInstance().restart();
                }
                if (!isHasNext && !isShowBookEnd) {
                    if (baseBook.book_id < LOCAL_BOOKID) {
                        isShowBookEnd = true;
                        if (mPageLoader != null) {
                            mPageLoader.saveRecord();
                        }
                        // 打开末尾推荐页
                        Intent intent = new Intent();
                        intent.setClass(activity, BookEndRecommendActivity.class);
                        intent.putExtra("book", baseBook);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_right_left_open, R.anim.activity_stay);
                    }
                }
            }

            @Override
            public void purchase(int type) {
                if (mPageLoader == null || mPageLoader.bookChapter == null) {
                    return;
                }
                if (type == 1) {
                    // 单次购买
                    ChapterManager.getInstance().purchaseSingleChapter(activity, Api.mChapterBuy, mBookId,
                            mPageLoader.bookChapter.chapter_id + "", -1, new ChapterManager.OnPurchaseListener() {
                                @Override
                                public void purchaseSuc(long[] chapter_ids) {
                                    if (chapter_ids != null && chapter_ids.length > 0) {
                                        BookChapter bookChapter = ChapterManager.getInstance().getChapter(chapter_ids[0]);
                                        bookChapter.is_preview = 0;
                                        bookChapter.chapter_path = null;
                                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.ReadActivity_buysuccess));
                                        ObjectBoxUtils.addData(bookChapter, BookChapter.class);
                                        ChapterManager.getInstance().openCurrentChapter(false,
                                                bookChapter, mPageLoader);
                                    }
                                }
                            });
                } else if (type == 2) {
                    // 批量购买
                    purchaseDialog = new PublicPurchaseDialog(activity, Constant.BOOK_CONSTANT,
                            false, null, true);
                    purchaseDialog.initData(mBookId, mPageLoader.bookChapter.chapter_id + "",
                            false, null);
                    purchaseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ImmersionBar.with(activity).hideBar(BarHide.FLAG_HIDE_BAR).init();
                        }
                    });
                    purchaseDialog.show();
                }
            }

            @Override
            public void lookVideo() {
                if (LoginUtils.goToLogin(activity)) {
                    onClickWatchVideo();
                }
            }

            @Override
            public void cancel(boolean isNext) {

            }

            @Override
            public void onReward(BookChapter bookChapter) {
                if (baseBook != null && Constant.getRewardSwitch(activity) == 1) {
                    if (mPageLoader != null) {
                        mPageLoader.saveRecord();
                    }
                    openBookReadDialog(true);
                }
            }

            @Override
            public void onTicket(BookChapter bookChapter) {
                if (baseBook != null && Constant.getMonthlyTicket(activity) == 1) {
                    if (mPageLoader != null) {
                        mPageLoader.saveRecord();
                    }
                    openBookReadDialog(false);
                }
            }

            @Override
            public void onComment(BookChapter bookChapter) {
                intentComment(bookChapter);
            }
        });

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        audioProgressLayout.setLayoutClickListener(new AudioProgressLayout.OnProgressLayoutClickListener() {
            @Override
            public void onIntoAudio() {
                AudioManager.getInstance(activity).IntoAudioActivity(activity, false);
            }

            @Override
            public void onPlayer() {
                // 设置听书
                if (!AudioManager.getInstance(activity).isIsPlayerError() && !AudioManager.getInstance(activity).isPreview()) {
                    // 只有播放不失败时才能点击
                    if (AudioManager.isSound) {
                        if (AudioManager.getInstance(activity).currentPlayAudio != null && AudioManager.getInstance(activity).audioCurrentChapter != null &&
                                AudioManager.getInstance(activity).audioChapterList != null) {
                            AudioManager.getInstance(activity).setAudioPlayerStatus((AudioManager.getInstance(activity).isPlayer != 1),
                                    AudioManager.getInstance(activity).currentPlayAudio,
                                    AudioManager.getInstance(activity).audioCurrentChapter.getChapter_id(), null);
                        }
                    } else {
                        if (AudioManager.getInstance(activity).currentPlayBook != null && AudioManager.getInstance(activity).bookCurrentChapter != null &&
                                AudioManager.getInstance(activity).bookChapterList != null) {
                            AudioManager.getInstance(activity).setBookPlayerStatus((AudioManager.getInstance(activity).isPlayer != 1),
                                    AudioManager.getInstance(activity).currentPlayBook,
                                    AudioManager.getInstance(activity).bookCurrentChapter.getChapter_id(), null);
                        }
                    }
                } else {
                    AudioManager.getInstance(activity).IntoAudioActivity(activity, true);
                }
                if (AudioManager.getInstance(activity).isPlayer != 1 && audioProgressLayout.isPlayer()) {
                    audioProgressLayout.setPlayer(false);
                }
            }

            @Override
            public void onCancel() {
                audioProgressLayout.setVisibility(View.GONE);
                EventBus.getDefault().post(new AudioServiceRefresh(false, true));
            }
        });
        if (mBrightDialog != null) {
            mBrightDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ImmersionBar.with(activity).hideBar(BarHide.FLAG_HIDE_BAR).init();
                }
            });
        }
    }

    /**
     * 用于刷新广告状态（开启、关闭）
     *
     * @param adStatusRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(AdStatusRefresh adStatusRefresh) {
        if (!adStatusRefresh.StartAutoBuy) {
            if (mPageLoader != null) {
                mPageLoader.saveRecord();
            }
            adInit(false);
        } else {
            mPageLoader.lordNextData(true,false);
        }
    }

    /**
     * 打开指定章节
     *
     * @param factoryChapter
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshPageFactoryChapter factoryChapter) {
        if (factoryChapter.activity == null) {
            ChapterManager.getInstance().openCurrentChapter(factoryChapter, mPageLoader);
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
        }
    }

    /**
     * 有声播放
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgress(AudioProgressRefresh refresh) {
        if (audioProgressLayout != null && audioProgressLayout.getVisibility() == View.VISIBLE) {
            if (refresh.isPlayer) {
                audioProgressLayout.setPlayer(true);
                if (refresh.isSound) {
                    audioProgressLayout.setProgress(refresh.progress * 100 / refresh.duration);
                } else {
                    audioProgressLayout.setProgress(0);
                }
            } else {
                audioProgressLayout.setPlayer(false);
                if (refresh.isSound) {
                    if (refresh.progress == 100) {
                        audioProgressLayout.setProgress(0);
                    }
                } else {
                    audioProgressLayout.setProgress(0);
                }
            }
        }
    }

    /**
     * 用于刷新小说阅读器底部内容
     *
     * @param bottomTabRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bottomTabRefresh(BookBottomTabRefresh bottomTabRefresh) {
        if (mPvPage != null && mPageLoader != null && mPageLoader.bookChapter != null) {
            switch (bottomTabRefresh.getType()) {
                case 1:
                    mPageLoader.bookChapter.reward_num = bottomTabRefresh.getContent();
                    if (!ChapterManager.getInstance().mChapterList.isEmpty()) {
                        for (BookChapter bookChapter : ChapterManager.getInstance().mChapterList) {
                            bookChapter.reward_num = bottomTabRefresh.getContent();
                        }
                    }
                    break;
                case 2:
                    mPageLoader.bookChapter.ticket_num = bottomTabRefresh.getContent();
                    if (!ChapterManager.getInstance().mChapterList.isEmpty()) {
                        for (BookChapter bookChapter : ChapterManager.getInstance().mChapterList) {
                            bookChapter.ticket_num = bottomTabRefresh.getContent();
                        }
                    }
                    break;
                case 3:
                    if (bottomTabRefresh.getChapterId() == mPageLoader.bookChapter.chapter_id) {
                        mPageLoader.bookChapter.comment_num = bottomTabRefresh.getContent();
                        ObjectBoxUtils.addData(mPageLoader.bookChapter, BookChapter.class);
                    }
                    break;
            }
            if (mPageLoader.mCurPage.isAuthorPage) {
                mPageLoader.initRewardLayout(mPageLoader.bookChapter);
                mPvPage.drawCurPage(false);
            }
        }
    }

    /**
     * 登录回调
     *
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity)) {
            if (purchaseDialog != null && purchaseDialog.isShowing()) {
                purchaseDialog.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (audioProgressLayout != null && AudioManager.getInstance(activity) != null) {
            if (AudioManager.getInstance(activity).isShowAudioStatusLayout()) {
                if (isShow) {
                    audioProgressLayout.setVisibility(View.VISIBLE);
                } else {
                    audioProgressLayout.setVisibility(View.GONE);
                }
                if (AudioManager.getInstance(activity).isPlayer == 1) {
                    audioProgressLayout.setPlayer(true);
                } else {
                    audioProgressLayout.setPlayer(false);
                }
                if (!TextUtils.isEmpty(AudioManager.getInstance(activity).bookCover)) {
                    audioProgressLayout.setImageUrl(AudioManager.getInstance(activity).bookCover);
                }
            } else {
                audioProgressLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (back()) return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean back() {
        if (mAutoSettingDialog != null) {
            if (AutoProgress.getInstance().isStarted()) {
                if (!mAutoSettingDialog.isShowing()) {
                    AutoProgress.getInstance().pause();
                    mAutoSettingDialog.show();
                    return true;
                }
            }
        }
        if (isShow) {
            hideReadSetting();
            return true;
        }
        if (mSettingDialog != null) {
            if (mSettingDialog.isShowing()) {
                mSettingDialog.hide();
                return true;
            }
        }
        if (mBrightDialog != null) {
            if (mBrightDialog.isShowing()) {
                mBrightDialog.hide();
                return true;
            }
        }
        BankActivity();
        return true;
    }

    private void BankActivity() {
        if (baseBook.book_id < LOCAL_BOOKID) {
            if (mPageLoader != null) {
                mPageLoader.saveRecord();
            }
            EventBus.getDefault().post(new RefreshBookInfo(baseBook, true));
            if (baseBook.is_collect == 1) {
                finish();
            } else {
                // 是否收藏
                isNeedToShelf(activity, baseBook);
            }
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().post(new RefreshBookInfoStatusBar());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!AutoProgress.getInstance().isStop()) {
            AutoProgress.getInstance().stop();
        }
        if (mSettingDialog != null && mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
        }
    }

    /**
     * 隐藏菜单。沉浸式阅读
     */
    public void initDayOrNight() {
        activity_read_change_day_night.setImageResource(!isNightMode ? R.mipmap.night_mode : R.mipmap.light_mode);
    }

    /**
     * 改变显示模式
     */
    public void changeDayOrNight(boolean toLight) {
        if (!toLight) {
            isNightMode = !isNightMode;
            initDayOrNight();
            config.setNightMode(isNightMode);
            mPageLoader.setNightMode(isNightMode);
        } else if (isNightMode) {
            isNightMode = false;
            initDayOrNight();
            config.setNightMode(isNightMode);
            mPageLoader.isNightMode = false;
        }
    }

    /**
     * 设置菜单
     */
    private void showReadSetting() {
        if (!isShow) {
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).statusBarDarkFont(true).init();
            Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.menu_ins);
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.menu_in);
            activity_read_bottom_view.startAnimation(topAnim);
            activity_read_top_menu.startAnimation(bottomAnim);
            activity_read_bottom_view.setVisibility(View.VISIBLE);
            activity_read_top_menu.setVisibility(View.VISIBLE);
            if (audioProgressLayout != null && AudioManager.getInstance(activity) != null) {
                if (AudioManager.getInstance(activity).isShowAudioStatusLayout()) {
                    audioProgressLayout.setVisibility(View.VISIBLE);
                } else {
                    audioProgressLayout.setVisibility(View.GONE);
                }
            }
            isShow = true;
        }
    }

    /**
     * @return 隐藏菜单
     */
    private boolean hideReadSetting() {
        if (isShow) {
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
            Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.menu_outs);
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.menu_out);
            if (activity_read_bottom_view.getVisibility() == View.VISIBLE) {
                activity_read_bottom_view.startAnimation(topAnim);
            }
            if (activity_read_top_menu.getVisibility() == View.VISIBLE) {
                activity_read_top_menu.startAnimation(bottomAnim);
            }
            activity_read_bottom_view.setVisibility(View.GONE);
            activity_read_top_menu.setVisibility(View.GONE);
            audioProgressLayout.setVisibility(View.GONE);
            isShow = false;
            return true;
        }
        return false;
    }

    @OnClick({R.id.toolbar_into_reward, R.id.toolbar_into_audio, R.id.toolbar_into_down, R.id.toolbar_into_more,
            R.id.book_read_bottom_brightness, R.id.activity_read_top_back_view, R.id.book_read_bottom_directory,
            R.id.book_read_bottom_comment, R.id.book_read_bottom_setting, R.id.bookpop_bottom,
            R.id.activity_read_top_menu, R.id.activity_read_bottom_view, R.id.activity_read_change_day_night})
    public void onClick(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.toolbar_into_reward:
                    hideReadSetting();
                    if (Constant.getMonthlyTicket(activity) == 1 || Constant.getRewardSwitch(activity) == 1) {
                        if (mPageLoader != null) {
                            mPageLoader.saveRecord();
                        }
                        openBookReadDialog(true);
                    }
                    break;
                case R.id.toolbar_into_audio:
                    if (!InternetUtils.internet(activity)) {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_no_support_online_player));
                        return;
                    }
                    if (baseBook != null) {
                        if (ChapterManager.getInstance().getCurrentChapter() != null) {
                            baseBook.setCurrent_listen_chapter_id(
                                    ChapterManager.getInstance().getCurrentChapter().getChapter_id());
                        }
                        if (mPageLoader != null) {
                            mPageLoader.saveRecord();
                        }
                        // 打开服务
                        AudioManager.openService(activity);
                        // 打开ai界面
                        Intent audioIntent = new Intent();
                        audioIntent.putExtra("book", baseBook);
                        audioIntent.setClass(this, AudioAiActivity.class);
                        startActivity(audioIntent);
                        overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
                    }
                    break;
                case R.id.toolbar_into_down:
                    if (mPageLoader != null) {
                        mPageLoader.saveRecord();
                    }
                    BookDownDialogFragment.getDownBookChapters(ReadActivity.this, baseBook, mPageLoader.bookChapter, new BookDownDialogFragment.OnGetDownOptions() {
                        @Override
                        public void onOptions(List<Downoption> downOptions) {
                            BookDownDialogFragment bookDownDialogFragment = new BookDownDialogFragment(activity, baseBook, mPageLoader.bookChapter, downOptions);
                            bookDownDialogFragment.show(getSupportFragmentManager(), "BookDownDialogFragment");
                        }
                    });
                    if (isShow) {
                        hideReadSetting();
                    }
                    break;
                case R.id.toolbar_into_more:
                    hideReadSetting();
                    ReadHeadMoreDialog readHeadMoreDialog = new ReadHeadMoreDialog(activity, baseBook, mPageLoader,
                            new ReadHeadMoreDialog.OnDialogDismissListener() {
                                @Override
                                public void onDismiss() {
                                    ImmersionBar.with(activity).hideBar(BarHide.FLAG_HIDE_BAR).init();
                                }
                            });
                    readHeadMoreDialog.show(getSupportFragmentManager(), "ReadHeadMoreDialog");
                    break;
                case R.id.book_read_bottom_directory:
                    // 打开目录界面
                    if (mPageLoader != null) {
                        mPageLoader.saveRecord();
                    }

                    hideReadSetting();
                    Intent intent = new Intent(this, BookCatalogMarkActivity.class);
                    intent.putExtra("book", baseBook);
                    intent.putExtra("isFromBookRead", true);
                    startActivity(intent);
                    break;
                case R.id.activity_read_change_day_night:
                    changeDayOrNight(false);
                    break;
                case R.id.book_read_bottom_comment:
                    hideReadSetting();
                    // 打开评论页面
                    if (mPageLoader != null) {
                        intentComment(mPageLoader.bookChapter);
                    } else {
                        intentComment(null);
                    }
                    break;
                case R.id.book_read_bottom_setting:
                    mSettingDialog = new SettingDialog(this, baseBook);
                    mSettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ImmersionBar.with(activity).hideBar(BarHide.FLAG_HIDE_BAR).init();
                        }
                    });
                    mSettingDialog.setProgressBar(auto_read_progress_bar);
                    mSettingDialog.show();
                    hideReadSetting();
                    break;
                case R.id.bookpop_bottom:
                case R.id.activity_read_bottom_view:
                case R.id.activity_read_top_menu:
                    break;
                case R.id.activity_read_top_back_view:
                    BankActivity();
                    break;
                case R.id.book_read_bottom_brightness:
                    hideReadSetting();
                    if (mBrightDialog != null) {
                        mBrightDialog.show();
                    }
                    break;
            }
        }
    }

    /**
     * 打开打赏、月票弹窗
     *
     * @param isFirst
     */
    private void openBookReadDialog(boolean isFirst) {
        BookReadDialogFragment bookReadDialogFragment = new BookReadDialogFragment(activity, baseBook.book_id, isFirst);
        bookReadDialogFragment.show(getSupportFragmentManager(), "BookReadDialogFragment");
    }

    /**
     * 打开评论界面
     */
    private void intentComment(BookChapter bookChapter) {
        Intent intentComment = new Intent(activity, CommentActivity.class);
        intentComment.putExtra("current_id", mBookId);
        if (bookChapter != null) {
            intentComment.putExtra("chapter_id", bookChapter.chapter_id);
            MyToash.Log("read_chapter_id", bookChapter.chapter_id + "");
        }
        intentComment.putExtra("productType", Constant.BOOK_CONSTANT);
        startActivity(intentComment);
    }

    private void isNeedToShelf(ReadActivity activity, Book baseBook) {
        PublicDialog.publicDialogVoid(activity, "",
                LanguageUtil.getString(activity, R.string.ReadActivity_isaddBookself),
                LanguageUtil.getString(activity, R.string.ReadActivity_isaddBookselfno),
                LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujia), new PublicDialog.OnPublicListener() {
                    @Override
                    public void onClickConfirm(boolean isConfirm) {
                        if (isConfirm) {
                            baseBook.is_collect = 1;
                            ObjectBoxUtils.addData(baseBook, Book.class);
                            EventBus.getDefault().post(new RefreshShelf(Constant.BOOK_CONSTANT, new RefreshBookSelf(baseBook, 1)));
                            EventBus.getDefault().post(new RefreshBookInfo(baseBook, true));
                        }
                        activity.finish();
                    }
                });
    }

    /**
     * 显示加载动画
     */
    public void startBookReadLoad() {
        if (animationDrawable == null) {
            animationDrawable = (AnimationDrawable) bookReadLoad.getDrawable();
        }
        if (bookReadLoad.getVisibility() == View.GONE) {
            bookReadLoad.setVisibility(View.VISIBLE);
        }
        if (!animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    /**
     * 停止动画
     */
    public void stopBookReadLoad() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        if (bookReadLoad.getVisibility() == View.VISIBLE) {
            bookReadLoad.setVisibility(View.GONE);
        }
    }

    public View getRewardLayout() {
        return rewardLayout;
    }

    public View getAuthorFistNoteLayout() {
        return authorFistNoteLayout;
    }

    public View getAuthorWordsLayout() {
        return authorWordsLayout;
    }

    public View getPurchaseLayout() {
        return purchaseLayout;
    }

    /******************************************************************************************/

    /**
     * 观看视频
     */
    private void onClickWatchVideo() {
        startBookReadLoad();
        TTAdShow.loadJiliAd(activity, new TTAdShow.OnRewardVerify() {
            @Override
            public void OnRewardVerify() {
                adClick(activity, new BaseAd(ShareUitls.getString(activity, "USE_AD_VIDEO_Advert_id", "")), 0, new AdHttp.AdClick() {
                    @Override
                    public void adClick(String result) {
                        startBookReadLoad();
                        try {
                            int CLOSE_READ_AD_TIME = new JSONObject(result).getInt("time");//服务器当前时间
                            if (CLOSE_READ_AD_TIME != 0 && ShareUitls.getInt(activity, "USE_AD_VIDEO_TIME", 0) > 0) {
                                ShareUitls.putInt(activity, "CLOSE_READ_AD_TIME", CLOSE_READ_AD_TIME);//
                                ShareUitls.putInt(activity, "CLOSE_READ_AD_TIME_END", CLOSE_READ_AD_TIME + ShareUitls.getInt(activity, "USE_AD_VIDEO_TIME", 0));//免广告结束时间
                                handler.sendEmptyMessageDelayed(2, 5000);
                                frameLayoutCenter.setVisibility(View.INVISIBLE);
                                mPageLoader.isCloseAd = true;
                                mPageLoader.saveCurrentChapterPos(true);
                                mPageLoader.skipToChapter(mPageLoader.bookChapter.chapter_id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
}
