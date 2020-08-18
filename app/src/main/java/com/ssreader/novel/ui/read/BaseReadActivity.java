package com.ssreader.novel.ui.read;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.ReadContinue;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.base.BaseInterface;
import com.ssreader.novel.ui.read.manager.ReadSettingManager;
import com.ssreader.novel.ui.read.page.PageMode;
import com.ssreader.novel.utils.cache.BitmapCache;
import com.ssreader.novel.eventbus.BookEndRecommendRefresh;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.ui.bwad.ViewToBitmapUtil;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.MyTarget;
import com.ssreader.novel.ui.read.page.PageLoader;
import com.ssreader.novel.ui.read.page.PageView;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

import static com.ssreader.novel.constant.Constant.GETNotchHeight;
import static com.ssreader.novel.ui.utils.StatusBarUtil.setFullScreen;

public abstract class BaseReadActivity extends FragmentActivity implements BaseInterface {

    public BitmapCache bitmapCache;
    public ReadActivity activity;
    public int mScreenHeight;
    public Book baseBook;
    public BookChapter chapter;
    public long mBookId,ClickTime;
    public int isNotchEnable;
    public boolean isNightMode = false;
    public PageLoader mPageLoader;
    public FrameLayout frameLayoutCenter, frameLayoutButton;
    //页面宽
    protected int mWidth, AD_TOP;
    //页面高
    public int mHeight, AD_H;
    public PageView PvPage;
    protected PageMode initPageMode;
    // 当前是否已经打开了章节末尾推荐页
    public boolean isShowBookEnd = false;
    protected ReadSettingManager config;

    // 广告数据类
    protected BaseAd baseAdCenter, baseAdButton;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initActivity();
            bitmapCache = BitmapCache.getInstance();
            mHeight = ScreenSizeUtils.getInstance(activity).getScreenHeight();
            mWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth();
            setFullScreen(activity, 1);
            chapter = ChapterManager.getInstance().mCurrentChapter;
            baseBook = ChapterManager.getInstance().baseBook;
            if (baseBook.is_read == 0) {
                baseBook.is_read = 1;
                baseBook.isRecommend = false;
                ObjectBoxUtils.addData(baseBook, Book.class);
                EventBus.getDefault().post(new RefreshShelfCurrent(Constant.BOOK_CONSTANT, baseBook));
            }
            config = ReadSettingManager.getInstance();
            initPageMode = config.getPageMode();
            mBookId = baseBook.book_id;
            EventBus.getDefault().register(this);
            mScreenHeight = ScreenSizeUtils.getInstance(activity).getScreenHeight();
            isNotchEnable = GETNotchHeight(activity);
            setContentView(initContentView());
            ButterKnife.bind(activity);
            initView();
            // 保持屏幕常亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            EventBus.getDefault().post(new ReadContinue(baseBook));
            // 获取封面bitmap
            if (bitmapCache.getBitmapFromCache(String.valueOf(baseBook.getBook_id())) != null) {
                initData();
            } else {
                if (InternetUtils.internet(activity) && !TextUtils.isEmpty(baseBook.cover)) {
                    int bookCoverWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth() * 2 / 5;
                    Glide.with(this).asBitmap().load(baseBook.cover)
                            .override(bookCoverWidth, bookCoverWidth * 4 / 3)
                            .error(R.mipmap.icon_comic_def)
                            .into(new MyTarget(activity, new MyTarget.GetFirstReadImage() {
                                @Override
                                public void getFirstReadImage(Bitmap bitmap) {
                                    bitmapCache.addBitmapToCache(String.valueOf(baseBook.getBook_id()), bitmap);
                                    initData();
                                }
                            }));
                } else {
                    initData();
                }
            }
        } catch (Throwable e) {
        }
    }

    protected abstract void initActivity();

    @Override
    protected void onResume() {
        super.onResume();
        setReceiver();
        if (mPageLoader != null) {
            mPageLoader.updateTime();
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            mPageLoader.updateBattery(battery);
        }
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        if (mPageLoader != null) {
            mPageLoader.saveRecord();
        }
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    /**
     * 用于在末尾章节页时刷新界面
     *
     * @param recommendRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEndRecommend(BookEndRecommendRefresh recommendRefresh) {
        if (recommendRefresh.isFinish()) {
            if (mPageLoader != null) {
                mPageLoader.closeBook();
                mPageLoader = null;
            }
            finish();
        } else {
            isShowBookEnd = false;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (mPageLoader != null) {
                mPageLoader.closeBook();
                mPageLoader = null;
            }
            try {
                if (baseAdButton != null && baseAdButton.ad_type != 1) {
                    handler.removeMessages(3);
                }
                if (baseAdCenter != null && baseAdCenter.ad_type != 1) {
                    handler.removeMessages(0);
                }
                handler.removeMessages(2);
            } catch (Exception e) {
            }
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        } catch (Throwable e) {
        }
    }

    private void setReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
    }

    /**
     * 接收电池信息、时间的广播
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPageLoader != null) {
                if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int level = intent.getIntExtra("level", 0);
                    mPageLoader.updateBattery(level);
                }
                // 监听分钟的变化
                else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    mPageLoader.updateTime();
                }
            }
        }
    };

    public void getBaseAdCenter(int flag) {
        if (flag == 0 && baseAdCenter != null) {
            baseAdCenter.setAd(activity, frameLayoutCenter, flag, getttAdShowBitamp);
        } else if (baseAdButton != null) {
            baseAdButton.setAd(activity, frameLayoutButton, flag);
            if (baseAdButton.ad_type != 1) {
                handler.sendEmptyMessageDelayed(3, 30000);
            }
        }
    }

    private BaseAd.GetttAdShowBitamp getttAdShowBitamp = new BaseAd.GetttAdShowBitamp() {
        @Override
        public void getttAdShowBitamp(View view, int img) {
            if (view != null) {
                frameLayoutCenter.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmapAD = ViewToBitmapUtil.convertViewToBitmap(frameLayoutCenter, AD_TOP, AD_H);
                        bitmapCache.addBitmapToCache("baseAdCenter",bitmapAD);
                    }
                });
            } else {
                if (mPageLoader != null && !mPageLoader.isAdLord) {
                    mPageLoader.isAdLord = true;
                    adDataInitComplete();
                }
            }
        }
    };

    protected abstract void adDataInitComplete();

    public void CloseAdEnd() {
        if (mPageLoader == null || activity == null || activity.isFinishing()) {
            return;
        }
        int CLOSE_READ_AD_TIME = ShareUitls.getInt(activity, "CLOSE_READ_AD_TIME", 0);
        CLOSE_READ_AD_TIME += 5;
        int CLOSE_READ_AD_TIME_END = ShareUitls.getInt(activity, "CLOSE_READ_AD_TIME_END", 0);
        if (CLOSE_READ_AD_TIME >= CLOSE_READ_AD_TIME_END) {
            mPageLoader.isCloseAd = false;
        } else {
            mPageLoader.isCloseAd = true;
            ShareUitls.putInt(activity, "CLOSE_READ_AD_TIME", CLOSE_READ_AD_TIME);
            handler.sendEmptyMessageDelayed(2, 5000);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                case 3:
                    getBaseAdCenter(msg.what);
                    break;
                case 2://用于免广告时间判断
                    CloseAdEnd();
                    break;
            }
        }
    };

    @Override
    public void initInfo(String json) {

    }

    @Override
    public void errorInfo(String json) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        BWNApplication.applicationContext.setActivity(activity);
    }
}
