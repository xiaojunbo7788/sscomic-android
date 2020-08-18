package com.ssreader.novel.ui.read.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.ssreader.novel.R;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;

import com.ssreader.novel.ui.bwad.TTAdShow;
import com.ssreader.novel.ui.bwad.ViewToBitmapUtil;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.ui.read.animation.CoverPageAnim;
import com.ssreader.novel.ui.read.animation.HorizonPageAnim;
import com.ssreader.novel.ui.read.animation.NonePageAnim;
import com.ssreader.novel.ui.read.animation.PageAnimation;
import com.ssreader.novel.ui.read.animation.ScrollPageAnim;
import com.ssreader.novel.ui.read.animation.SimulationPageAnim;
import com.ssreader.novel.ui.read.animation.SlidePageAnim;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.cache.BitmapCache;

import org.jetbrains.annotations.Nullable;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.getIsReadBottomAd;
import static com.ssreader.novel.constant.Constant.getIsReadCenterAd;
import static com.ssreader.novel.ui.bwad.WebAdshow.ClickWebAd;
import static com.ssreader.novel.ui.read.page.PageLoader.STATUS_EMPTY;

/**
 * 绘制页面显示内容的类
 */
public class PageView extends View {

    // 当前View的宽、高
    private int mViewWidth = 0, mViewHeight = 0, S10, S30, S35, S50, S65;
    // 上下文
    private ReadActivity mActivity;
    // 首次点击位置
    private int mStartX = 0, mStartY = 0;
    // 是否移动
    private boolean isMove = false;
    // 初始化参数
    private int mBgColor = 0xFFCEC29C;
    private PageMode mPageMode = PageMode.SIMULATION;
    // 是否允许点击
    private boolean canTouch = true;
    // 唤醒菜单的区域
    private RectF mCenterRect = null;
    // 唤醒菜单的区域
    private RectF mCenterRectWebAd = null;
    private boolean isPrepare;
    // 动画类
    public PageAnimation mPageAnim;
    // 点击监听
    private TouchListener mTouchListener;
    // 内容加载器
    public PageLoader mPageLoader;
    // 广告
    public BaseAd baseAd;

    private FrameLayout ADview;
    public BitmapCache bitmapCache;
    private TTAdShow ttAdShow;

    public void setADview(FrameLayout ADview) {
        this.ADview = ADview;
        if (mPageAnim != null) {
            mPageAnim.ADview = ADview;
            mPageAnim.mPageLoader = mPageLoader;
        }
        if (ttAdShow == null) {
            ttAdShow = new TTAdShow();
        }
        if (baseAd != null && baseAd.ad_type != 1) {
            ttAdShow.bindAdListener(mPageLoader, mActivity, ADview, baseAd, null);
        }
    }

    // 动画监听类
    private PageAnimation.OnPageChangeListener mPageAnimListener = new PageAnimation.OnPageChangeListener() {
        @Override
        public boolean hasPrev(boolean isJudgeHasPre) {
            return PageView.this.hasPrevPage(isJudgeHasPre);
        }

        @Override
        public boolean hasNext(boolean isAuto) {
            return PageView.this.hasNextPage(isAuto);
        }

        @Override
        public void pageCancel(boolean isNext) {
            mTouchListener.cancel(isNext);
            mPageLoader.pageCancel(isNext);
        }
    };

    public PageView(Context context) {
        this(context, null);
    }

    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        S10 = ImageUtil.dp2px(context, 10);
        S30 = S10 * 3;
        S35 = (int) (S10 * 3.5f);
        S50 = S10 * 5;
        S65 = (int) (S10 * 6.5f);
        if (getIsReadCenterAd(context)) {
            int mHeight = ScreenSizeUtils.getInstance(context).getScreenHeight();
            int mWidth = ScreenSizeUtils.getInstance(context).getScreenWidth();
            int AD_WIDTH = (mWidth - S30);
            int AD_H = (int) ((float) AD_WIDTH / 1.2f);
            int AD_TOP = (mHeight - AD_H) / 2;
            int AD_MAGIN = (int) (S10 * 1.5f);
            mCenterRectWebAd = new RectF(AD_MAGIN, AD_TOP, AD_MAGIN + AD_WIDTH, AD_TOP + AD_H);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initScreenSize(w, h, false);
    }

    public void initScreenSize(int w, int h, boolean isRefreshAd) {
        if (w != 0) {
            mViewWidth = w;
        }
        if (h != 0) {
            mViewHeight = h;
        }
        if (getIsReadBottomAd(getContext())) {
            mViewHeight -= Constant.getReadBottomHeight(getContext());
        }
        // 设置中间区域范围
        mCenterRect = new RectF(mViewWidth / 5, mViewHeight / 3,
                mViewWidth * 4 / 5, mViewHeight * 2 / 3);
        isPrepare = true;
        if (isRefreshAd) {
            mPageLoader = null;
        }
        if (mPageLoader != null && !isRefreshAd) {
            mPageLoader.prepareDisplay(w, mViewHeight);
        }
    }

    /**
     * 设置翻页的模式
     *
     * @param pageMode
     */
    void setPageMode(PageMode pageMode) {
        mPageMode = pageMode;
        //视图未初始化的时候，禁止调用
        if (mViewWidth == 0 || mViewHeight == 0) return;
        switch (mPageMode) {
            case COVER:
                mPageAnim = new CoverPageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
                break;
            case SLIDE:
                mPageAnim = new SlidePageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
                break;
            case NONE:
                mPageAnim = new NonePageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
                break;
            case SCROLL:
                mPageAnim = new ScrollPageAnim(mViewWidth, mViewHeight, 0,
                        0, this, mPageAnimListener);
                break;
            default:
                mPageAnim = new SimulationPageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
        }
        if (ADview != null) {
            mPageAnim.ADview = ADview;
        }
    }

    public Bitmap getNextBitmap() {
        if (mPageAnim == null) return null;
        return mPageAnim.getNextBitmap();
    }

    public Bitmap getBgBitmap() {
        if (mPageAnim == null) return null;
        return mPageAnim.getBgBitmap();
    }

    public void setBgColor(int color) {
        mBgColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawColor(mBgColor);
        //绘制动画
        if (mPageAnim != null) {
            mPageAnim.draw(canvas);
        }
    }

    private long ClickTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPageLoader != null && mPageLoader.mCurPage != null && mPageLoader.mCurPage.pageStyle == 3 && baseAd.ad_type != 1) {
            if (event.getX() > mViewWidth >> 1 && event.getAction() == MotionEvent.ACTION_DOWN && !mPageLoader.mCurPage.pageAdOver) {
                if (!ADview.isShown()) {
                    ADview.setVisibility(VISIBLE);
                }
                return false;
            }
        }
        if (mPageLoader != null && mPageLoader.bookChapter != null && mPageLoader.bookChapter.is_preview == 1) {
            // 预览章节屏蔽急速点击
            long ClickTimeNew = System.currentTimeMillis();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (ClickTimeNew - ClickTime > AgainTime) {
                    ClickTime = ClickTimeNew;
                    Boolean x1 = onTouchHander(event);
                    if (x1 != null) {
                        return x1;
                    }
                } else {
                    return false;
                }
            } else {
                Boolean x1 = onTouchHander(event);
                if (x1 != null) {
                    return x1;
                }
            }
        } else {
            if (!canTouch && event.getAction() != MotionEvent.ACTION_DOWN) {
                return true;
            }
            Boolean x1 = onTouchHander(event);
            if (x1 != null) {
                return x1;
            }
        }
        return true;
    }

    @Nullable
    private Boolean onTouchHander(MotionEvent event) {
        if (ADview != null && ADview.getVisibility() == VISIBLE) {
            ADview.setVisibility(INVISIBLE);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                isMove = false;
                if (mTouchListener != null && mPageAnim != null) {
                    canTouch = mTouchListener.onTouch();
                    mPageAnim.onTouchEvent(event, onAnimationStopped);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 判断是否大于最小滑动值。
                int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (!isMove) {
                    isMove = Math.abs(mStartX - event.getX()) > slop || Math.abs(mStartY - event.getY()) > slop;
                }
                // 如果滑动了，则进行翻页。
                if (isMove) {
                    if (mPageAnim != null && onAnimationStopped != null) {
                        mPageAnim.onTouchEvent(event, onAnimationStopped);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                try {
                    if (ClickHander(x, y)) return true;
                } catch (Throwable e) {
                }
                if (mPageAnim != null && onAnimationStopped != null) {
                    mPageAnim.onTouchEvent(event, onAnimationStopped);
                }
                break;
        }
        return null;
    }

    private boolean ClickHander(int x, int y) {
        if (!isMove) {
            if (mPageLoader.mStatus == STATUS_EMPTY && mPageLoader.RoundRectAgain != null &&
                    mPageLoader.RoundRectAgain.contains(x, y)) {
                if (InternetUtils.internet(mActivity)) {
                    ChapterManager.getInstance().openCurrentChapter(false,
                            mPageLoader.bookChapter, mPageLoader);
                } else {
                    MyToash.ToashError(mActivity, LanguageUtil.getString(mActivity, R.string.splashactivity_nonet));
                }
                return true;
            }
            if (mPageLoader.mCurPage != null) {
                if (mCenterRectWebAd != null) {//使用广告
                    if (mPageLoader.mCurPage.pageStyle == 3) {//当前是广告页
                        if (baseAd != null && mCenterRectWebAd.contains(x, y)) {
                            if (baseAd.ad_type == 1) {
                                ClickWebAd(mActivity, baseAd, 0);
                            } else {
                                if (!ADview.isShown()) {
                                    ADview.setVisibility(VISIBLE);
                                }
                            }
                            return true;
                        }
                        if (mPageLoader.USE_AD_VIDEO) {
                            int[] s = mPageLoader.getLookVideoS();
                            if (mStartX >= s[0] && mStartX <= s[1] && mStartY >= (s[2] - S30 / 6) && mStartY <= (s[3] + S30 / 6)) {
                                mTouchListener.lookVideo();
                                return true;
                            }
                        }
                    }
                }
                if (mPageLoader.bookChapter.is_preview == 1 && mPageLoader.mCurPage.position == mPageLoader.mCurPageList.size() - 1) {
                    // 确认购买
                    if (clickSinglePurchase()) {
                        return true;
                    }
                    // 批量购买
                    if (clickVolumePurchase()) {
                        return true;
                    }
                }
                if (clickTab()) {
                    return true;
                }
                if (mCenterRect.contains(x, y)) {
                    mTouchListener.center(canTouch);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 确认购买
     *
     * @return
     */
    private boolean clickSinglePurchase() {
        if (mStartY < mViewHeight - (S50 + S35 + S10) && mStartY > mViewHeight - (S50 + S35 + S35 + S10)
                && mStartX > S10 * 2 && mStartX < mViewWidth - S10 * 2) {
            mTouchListener.purchase(1);
            return true;
        }
        return false;
    }

    /**
     * 批量购买
     *
     * @return
     */
    private boolean clickVolumePurchase() {
        if (mStartY < mViewHeight - S50 && mStartY > mViewHeight - (S50 + S35)
                && mStartX > S10 * 2 && mStartX < mViewWidth - S10 * 2) {
            mTouchListener.purchase(2);
            return true;
        }
        return false;
    }

    /**
     * @return 是否点击了观看视频广告
     */
    private boolean clickVideoAd() {
        if (mPageLoader != null && mPageLoader.mCurPage != null && mPageLoader.mCurPage.pageStyle == 3 && mPageLoader.USE_AD_VIDEO) {
            int[] s = mPageLoader.getLookVideoS();
            if (mStartX >= s[0] && mStartX <= s[1] && mStartY >= (s[2] - S30 / 6) && mStartY <= (s[3] + S30 / 6)) {
                mTouchListener.lookVideo();
                return true;
            }
        }
        return false;
    }

    /**
     * @return 底部tab
     */
    private boolean clickTab() {
        if (mPageLoader.mCurPage.isAuthorPage
                && !(!mPageLoader.tab1 && !mPageLoader.tab2 && !mPageLoader.tab3)
                && mStartY < mViewHeight - S30
                && mStartY > mViewHeight - S65) {
            if (mPageLoader.tab1 && mPageLoader.tab2 && mPageLoader.tab3) {
                if (mStartX > 0 && mStartX < mViewWidth / 3) {
                    mTouchListener.onReward(mPageLoader.bookChapter);
                } else if (mStartX > mViewWidth / 3 && mStartX < mViewWidth - (mViewWidth / 3)) {
                    mTouchListener.onTicket(mPageLoader.bookChapter);
                } else {
                    mTouchListener.onComment(mPageLoader.bookChapter);
                }
            } else {
                if (mPageLoader.tab1 && mPageLoader.tab2) {
                    if (mStartX > 0 && mStartX < mViewWidth / 2) {
                        mTouchListener.onReward(mPageLoader.bookChapter);
                    } else {
                        mTouchListener.onTicket(mPageLoader.bookChapter);
                    }
                } else if (mPageLoader.tab1 && mPageLoader.tab3) {
                    if (mStartX > 0 && mStartX < mViewWidth / 2) {
                        mTouchListener.onReward(mPageLoader.bookChapter);
                    } else {
                        mTouchListener.onComment(mPageLoader.bookChapter);
                    }
                } else if (mPageLoader.tab1) {
                    if (mStartX > 0 && mStartX < mViewWidth) {
                        mTouchListener.onReward(mPageLoader.bookChapter);
                    }
                } else if (mPageLoader.tab2 && mPageLoader.tab3) {
                    if (mStartX > 0 && mStartX < mViewWidth / 2) {
                        mTouchListener.onTicket(mPageLoader.bookChapter);
                    } else {
                        mTouchListener.onComment(mPageLoader.bookChapter);
                    }
                } else if (mPageLoader.tab2) {
                    if (mStartX > 0 && mStartX < mViewWidth) {
                        mTouchListener.onTicket(mPageLoader.bookChapter);
                    }
                } else {
                    if (mStartX > 0 && mStartX < mViewWidth) {
                        mTouchListener.onComment(mPageLoader.bookChapter);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return 判断是否存在上一页
     */
    private boolean hasPrevPage(boolean isJudgeHasPre) {
        boolean isHasPrevPage = mPageLoader.prev();

        if (mPageLoader.mStatus == PageLoader.STATUS_FINISH) {
            mTouchListener.prePage();
            if (!isHasPrevPage && isJudgeHasPre && !mActivity.isShowBookEnd) {
                MyToash.ToastError(mActivity, LanguageUtil.getString(mActivity, R.string.ReadActivity_startpage), null);
            }
        }
        return isHasPrevPage;
    }

    /**
     * @return 判断是否下一页存在
     */
    private boolean hasNextPage(boolean isAuto) {
        boolean isHasNextPage = mPageLoader.next();
        if (mPageLoader.mStatus == PageLoader.STATUS_FINISH) {
            mTouchListener.nextPage(isHasNextPage, isAuto);
        }
        return isHasNextPage;
    }

    @Override
    public void computeScroll() {
        //进行滑动
        if (mPageAnim != null) {
            mPageAnim.scrollAnim();
        }
        super.computeScroll();
    }

    //如果滑动状态没有停止就取消状态，重新设置Anim的触碰点
    public void abortAnimation() {
        mPageAnim.abortAnim();
    }

    public boolean isRunning() {
        if (mPageAnim == null) {
            return false;
        }
        return mPageAnim.isRunning();
    }

    public boolean isPrepare() {
        return isPrepare;
    }

    public void setTouchListener(ReadActivity activity, TouchListener mTouchListener) {
        this.mActivity = activity;
        this.mTouchListener = mTouchListener;
    }

    public void drawNextPage() {
        if (!isPrepare) {
            return;
        }
        if (mPageAnim instanceof HorizonPageAnim) {
            ((HorizonPageAnim) mPageAnim).changePage();
        }
        mPageLoader.drawPage(getNextBitmap(), false);
    }

    /**
     * 绘制当前页。
     *
     * @param isUpdate
     */
    public void drawCurPage(boolean isUpdate) {
        if (!isPrepare) return;

        if (!isUpdate) {
            if (mPageAnim instanceof ScrollPageAnim) {
                ((ScrollPageAnim) mPageAnim).resetBitmap();
            }
        }
        if (mPageLoader != null) {
            mPageLoader.drawPage(getNextBitmap(), isUpdate);
        }
    }


    /**
     * @param collBook
     * @param markId
     * @return 获取 PageLoader
     */
    public PageLoader getPageLoader(ReadActivity activity, Book collBook, long markId) {
        // 判是否已经存在
        if (mPageLoader != null) {
            return mPageLoader;
        }
        // 根据书籍类型，获取具体的加载器
        mPageLoader = new NetPageLoader(activity, this, collBook);
        mPageLoader.markId = markId;
        // 判断是否 PageView 已经初始化完成
        if (mViewWidth != 0 || mViewHeight != 0) {
            // 初始化 PageLoader 的屏幕大小
            mPageLoader.prepareDisplay(mViewWidth, mViewHeight);
        }
        if (mPageAnim != null) {
            mPageAnim.mPageLoader = mPageLoader;
        }
        return mPageLoader;
    }

    PageAnimation.OnAnimationStopped onAnimationStopped = new PageAnimation.OnAnimationStopped() {
        @Override
        public void Stop(boolean Direction, boolean isCancel) {
            if (!isCancel && mPageLoader != null && mPageLoader.mCurPage != null) {
                if (mPageLoader.mCurPage.pageStyle == 3) {
                    if (baseAd.ad_type == 1) {
                        mPageLoader.mCurPage.pageAdOver = true;
                    } else {
                        mPageLoader.mCurPage.pageAdOver = false;
                        MyToash.setDelayedHandle(3000, new MyToash.DelayedHandle() {
                            @Override
                            public void handle() {
                                mPageLoader.mCurPage.pageAdOver = true;
                            }
                        });
                        ttAdShow.renderAd(mPageLoader, mActivity, ADview, baseAd, new BaseAd.GetttAdShowBitamp() {
                            @Override
                            public void getttAdShowBitamp(View view, int img) {
                                if (img != -1) {
                                    Bitmap bitmapAD = ViewToBitmapUtil.convertViewToBitmap(ADview);
                                    bitmapCache.addBitmapToCache("baseAdCenter", bitmapAD);
                                    mPageLoader.mCurPage.pageAdOver = true;
                                    if (mPageLoader.canvas != null) {
                                        mPageLoader.drawAD(mPageLoader.canvas, bitmapAD, false);
                                    }
                                }
                            }
                        });
                    }
                } else if (mPageLoader.mCurPage.pageAdLord) {
                    ttAdShow.bindAdListener(mPageLoader, mActivity, ADview, baseAd, null);
                }
            } else {
                if (isCancel) {
                    if (mPageLoader != null && mPageLoader.mCurPage != null && mPageLoader.mCurPage.pageStyle == 3) {
                        ADview.setVisibility(VISIBLE);
                    }
                }
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            mPageAnim.abortAnim();
            mPageAnim.clear();
            mPageLoader = null;
            mPageAnim = null;
            if (ttAdShow != null) {
                if (ttAdShow.ttNativeExpressAds != null && !ttAdShow.ttNativeExpressAds.isEmpty()) {
                    for (TTNativeExpressAd ttNativeExpressAd : ttAdShow.ttNativeExpressAds) {
                        ttNativeExpressAd.destroy();
                    }
                    ttAdShow.ttNativeExpressAds.clear();
                }
                ttAdShow.mTTAdNative = null;
                ttAdShow = null;
            }
        } catch (Exception e) {
        }
    }

    public interface TouchListener {

        boolean onTouch();

        void center(boolean isCancleDialog);

        void prePage();

        void nextPage(boolean isHasNext, boolean isAuto);

        void purchase(int type);

        void lookVideo();

        void cancel(boolean isNext);

        void onReward(BookChapter bookChapter);

        void onTicket(BookChapter bookChapter);

        void onComment(BookChapter bookChapter);
    }
}
