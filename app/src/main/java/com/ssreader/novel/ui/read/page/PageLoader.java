package com.ssreader.novel.ui.read.page;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.huawei.android.hms.agent.common.IOUtils;
import com.ssreader.novel.R;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.BookMarkBean;
import com.ssreader.novel.model.GetBookChapterList;
import com.ssreader.novel.model.ReadHistory;
import com.ssreader.novel.ui.bwad.ViewToBitmapUtil;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.read.manager.ReadSettingManager;
import com.ssreader.novel.ui.read.util.CommonUtil;
import com.ssreader.novel.ui.read.util.ScreenUtils;
import com.ssreader.novel.ui.read.util.StringUtils;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.BorderTextView;
import com.ssreader.novel.utils.DateUtils;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.cache.BitmapCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BATTERY_SERVICE;
import static com.ssreader.novel.constant.Constant.LOCAL_BOOKID;
import static com.ssreader.novel.constant.Constant.LordNext;
import static com.ssreader.novel.constant.Constant.getIsReadCenterAd;
import static com.ssreader.novel.constant.Constant.getIsUseVideoAd;
import static com.ssreader.novel.ui.read.page.PageMode.SCROLL;

public abstract class PageLoader {

    // 当前页面的状态
    public static final int STATUS_LOADING = 1;         // 正在加载
    public static final int STATUS_FINISH = 2;          // 加载完成
    public static final int STATUS_ERROR = 3;           // 加载错误 (一般是网络加载情况)
    public static final int STATUS_EMPTY = 4;           // 空数据
    public static final int STATUS_PARING = 5;          // 正在解析 (装载本地数据)
    public static final int STATUS_PARSE_ERROR = 6;     // 本地文件解析错误(暂未被使用)
    public static final int STATUS_CATEGORY_EMPTY = 7;  // 获取到的目录为空
    // 默认的显示参数配置
    private static final int DEFAULT_MARGIN_HEIGHT = 30;
    private static final int DEFAULT_MARGIN_WIDTH = 15;
    private static final int DEFAULT_TIP_SIZE = 10;
    private static final int EXTRA_TITLE_SIZE = 4;
    private float LineSpacing;
    // 章节页面的类型
    private final int PAGE_STYLE_FIRST_PAGE = 1;
    private final int PAGE_STYLE_AUTHOR_PAGE = 2;
    private final int PAGE_STYLE_AD_PAGE = 3;
    // 当前章节列表
    protected List<BookChapter> mChapterList;
    // 书本对象
    protected Book mCollBook;
    // 章节对象
    public BookChapter bookChapter;
    // 监听器
    protected OnPageChangeListener mPageChangeListener;
    // 上下文
    protected ReadActivity mContext;
    // 页面显示类
    private PageView mPageView;
    // 当前显示的页
    public TxtPage mCurPage;
    // 书签id
    public long markId;
    // 上一章的页面列表缓存
    private List<TxtPage> mPrePageList;
    // 当前章节的页面列表
    public List<TxtPage> mCurPageList;
    // 下一章的页面列表缓存
    private List<TxtPage> mNextPageList;

    // 绘制电池的画笔
    private Paint mBatteryPaint;
    // 绘制提示的画笔
    private Paint mTipPaint;
    // 绘制标题的画笔
    private Paint mTitlePaint;
    // 绘制背景颜色的画笔(用来擦除需要重绘的部分)
    private Paint mBgPaint;
    // 绘制小说内容的画笔
    private TextPaint mTextPaint;
    // 阅读器的配置选项
    private ReadSettingManager mSettingManager;
    // 被遮盖的页，或者认为被取消显示的页
    private TxtPage mCancelPage;

    /***********************************    params    ***********************************/

    // 当前的状态
    public int mStatus = STATUS_LOADING;
    // 判断章节列表是否加载完成
    protected boolean isChapterListPrepare;
    // 是否打开过章节
    public boolean isChapterOpen, isFirstOpen = true;
    // 页面的翻页效果模式
    public PageMode mPageMode;
    // 加载器的颜色主题
    private PageStyle mPageStyle;
    //当前是否是夜间模式
    public boolean isNightMode;
    //书籍绘制区域的宽高
    private int mVisibleWidth;
    private int mVisibleHeight;
    //应用的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;
    //加载失败的坐标
    public RectF RoundRectAgain;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;
    //字体的颜色
    private int mTextColor;
    //标题的大小
    private int mTitleSize;
    //字体的大小
    private int mTextSize;
    //行间距
    private int mTextInterval;
    //标题的行间距
    private int mTitleInterval;
    //段落距离(基于行间距的额外距离)
    private int mTextPara;
    private int mTitlePara;
    //电池的百分比
    private int mBatteryLevel = 0;
    //电池宽高
    private int mBatterWidth, mBatterHeight, mBorderWidth;
    //电池外边框
    private RectF rect1 = new RectF();
    //电池内边框
    private RectF rect2 = new RectF();
    //当前页面的背景
    private int mBgColor;

    // 当前章
    public long mCurChapter_id;
    //上一章的记录
    private long mLastChapter_id;

    /*****************************   底部与顶部显示内容   *****************************/

    // 状态栏
    private int isNotchEnable;
    // 底部文字，距离底部高度
    private int percentWidth, percentMarginHeight;
    // 书名
    private String bookName = "";
    // 总章节
    private int totalChapter;
    private DecimalFormat df = new DecimalFormat("#0.0");
    private float ButtonY;

    /*******************************   广告、作家的话、购买   *******************************/

    // 缓存池
    public BitmapCache bitmapCache;
    // 封面
    private Paint bookFirstPaint;
    // 作家的话
    private Paint authorPaint, videoTips;
    // 作家相关的View
    private View authorFistNoteLayout, rewardLayout, authorWordsLayout;
    // 底部tab状态
    public boolean tab1, tab2, tab3;
    // 是否临时关闭广告 看完视频免广告30分钟
    public boolean isCloseAd = false;
    // 广告是否加载完毕
    public boolean isAdLord = false;
    // 广告文字
    private String lookVideoText = "点击看视频免30分钟广告", scrollClickText = "点击/滑动可继续阅读";
    // 广告文字显示宽高
    private int ADVideo_text_WIDTH, TimeWidth, LeftMagin, Title_height, scrollClickWidth;
    // 广告位置
    public int AD_BUTTOM, AD_WIDTH, AD_TOP, AD_H, AD_MAGIN;
    // 视频广告
    private int[] LookVideoS = new int[4];
    // 购买
    private View purchaseLayout;

    /*****************************     init params     *******************************/

    public PageLoader(ReadActivity activity, PageView pageView, Book collBook) {
        mChapterList = ChapterManager.getInstance().mChapterList;
        bookChapter = ChapterManager.getInstance().mCurrentChapter;
        mPageView = pageView;
        mContext = activity;
        mCollBook = collBook;
        totalChapter = mCollBook.total_chapter;
        bookName = collBook.name;
        if (purchaseLayout == null) {
            purchaseLayout = activity.getPurchaseLayout();
            initPurchaseLayout();
        }
        if (authorFistNoteLayout == null) {
            authorFistNoteLayout = activity.getAuthorFistNoteLayout();
            initAuthorNoteLayout();
        }
        if (authorWordsLayout == null) {
            authorWordsLayout = activity.getAuthorWordsLayout();
            initAuthorWordsLayout(bookChapter);
        }
        if (rewardLayout == null) {
            rewardLayout = activity.getRewardLayout();
            initRewardLayout(bookChapter);
        }
        // 初始化数据
        initData();
        // 初始化画笔
        initPaint();
        // 初始化PageView
        initPageView();
        // 初始化书籍
        prepareBook();
        // 设置宽高
        initBgPSize(activity, collBook);
    }

    private void initBgPSize(ReadActivity activity, Book collBook) {
        isNotchEnable = activity.isNotchEnable;
        LeftMagin = ImageUtil.dp2px(activity, 10);
        percentMarginHeight = ScreenUtils.dpToPx(6);

        percentWidth = (int) mTipPaint.measureText("100.99%");
        TimeWidth = (int) mTipPaint.measureText("00:00");

        Title_height = isNotchEnable + LeftMagin * 2;
        mBorderWidth = ImageUtil.dp2px(activity, 1);
        mBatterWidth = LeftMagin * 2 - mBorderWidth;
        mBatterHeight = LeftMagin;
    }

    private void initData() {
        // 获取配置管理器
        mSettingManager = ReadSettingManager.getInstance();
        // 获取配置参数
        mPageMode = mSettingManager.getPageMode();
        mPageStyle = mSettingManager.getPageStyle();
        // 初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT);
        LineSpacing = mSettingManager.getLineSpacingMode();
        // 配置文字有关的参数
        setUpTextParams(mSettingManager.getTextSize());
        // 获取电量
        BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
        mBatteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public void initAD(FrameLayout frameLayoutCenter, int AD_H, int AD_TOP) {
        this.frameLayoutAd = frameLayoutCenter;
        this.AD_H = AD_H;
        this.AD_BUTTOM = AD_TOP + AD_H;
        int AD_MAGIN = ImageUtil.dp2px(mContext, 15);
        this.AD_WIDTH = (ScreenSizeUtils.getInstance(mContext).getScreenWidth() - AD_MAGIN * 2);
        this.AD_MAGIN = AD_MAGIN;
        this.AD_TOP = AD_TOP;
        // 设置是否显示视屏广告入口
        USE_AD_VIDEO = getIsUseVideoAd(mContext);
        lookVideoText = ShareUitls.getString(mContext, "USE_AD_VIDEO_TEXT", "");
        if (TextUtils.isEmpty(lookVideoText)) {
            USE_AD_VIDEO = false;
        }
        if (USE_AD_VIDEO) {
            lookVideoText += ">";
            videoTips = new Paint();
            videoTips.setAntiAlias(true);
            videoTips.setDither(true);
            videoTips.setTextSize(CommonUtil.sp2px(mContext, 15));
            ADVideo_text_WIDTH = (int) (videoTips.measureText(lookVideoText));//;
            videoTips.setTextSize(CommonUtil.sp2px(mContext, 20));
            scrollClickWidth = (int) (videoTips.measureText(scrollClickText));//;
        }
    }

    /**
     * 作用：设置与文字相关的参数
     *
     * @param textSize
     */
    private void setUpTextParams(int textSize) {
        // 文字大小
        mTextSize = textSize;
        mTitleSize = mTextSize + ScreenUtils.spToPx(EXTRA_TITLE_SIZE);
        setUpLineSpacingParams(LineSpacing);
    }

    private void setUpLineSpacingParams(float LineSpacing) {
        this.LineSpacing = LineSpacing;
        // 行间距(大小为字体的一半)
        mTextInterval = (int) (LineSpacing * mTextSize / 2);
        mTitleInterval = (int) (LineSpacing * mTitleSize / 2);
        // 段落间距(大小为字体的高度)
        mTextPara = (int) (LineSpacing * mTextSize);
        mTitlePara = (int) (LineSpacing * mTitleSize);
    }

    private void initPaint() {
        // 绘制提示的画笔
        mTipPaint = new Paint();
        mTipPaint.setColor(mTextColor);
        mTipPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mTipPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE)); // Tip默认的字体大小
        mTipPaint.setAntiAlias(true);
        mTipPaint.setSubpixelText(true);

        // 绘制页面内容的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        // 绘制标题的画笔
        mTitlePaint = new TextPaint();
        mTitlePaint.setColor(mTextColor);
        mTitlePaint.setTextSize(mTitleSize);
        mTitlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitlePaint.setAntiAlias(true);

        // 封面文字画笔
        bookFirstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bookFirstPaint.setFakeBoldText(true);
        bookFirstPaint.setTextAlign(Paint.Align.LEFT);
        bookFirstPaint.setColor(mTextColor);

        // 绘制作家的话的画笔
        authorPaint = new Paint();
        authorPaint.setAntiAlias(true);
        authorPaint.setDither(true);

        // 绘制背景的画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);

        // 绘制电池的画笔
        mBatteryPaint = new Paint();
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setDither(true);

        // 初始化页面样式
        setNightMode(mSettingManager.isNightMode());
    }

    private void initPageView() {
        // 配置参数
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mBgColor);
    }

    /******************************    public method   ****************************/

    /**
     * 跳转到上一章
     *
     * @return
     */
    public boolean skipPreChapter() {
        if (!hasPrevChapter()) {
            return false;
        }
        // 载入上一章。
        if (parsePrevChapter()) {
            mCurPage = getCurPage(0);
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawCurPage(false);
        return true;
    }

    /**
     * 跳转到下一章
     *
     * @return
     */
    public boolean skipNextChapter() {
        if (!hasNextChapter()) {
            return false;
        }
        //判断是否达到章节的终止点
        if (parseNextChapter()) {
            mCurPage = getCurPage(0);
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawCurPage(false);
        return true;
    }

    /**
     * 跳转到指定章节
     *
     * @param pos:从 0 开始。
     */
    public void skipToChapter(long pos) {
        isChapterListPrepare = true;
        // 设置参数
        mCurChapter_id = pos;
        // 将上一章的缓存设置为null
        mPrePageList = null;
        // 将下一章缓存设置为null
        mNextPageList = null;
        // 打开指定章节
        openChapter();
    }

    /**
     * 跳转到指定的页
     *
     * @param pos
     */
    public boolean skipToPage(int pos) {
        if (!isChapterListPrepare) {
            return false;
        }
        mCurPage = getCurPage(pos);
        mPageView.drawCurPage(false);
        return true;
    }

    /**
     * 更新时间
     */
    public void updateTime() {
        if (!mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    /**
     * 更新电量
     *
     * @param level
     */
    public void updateBattery(int level) {
        mBatteryLevel = level;
        if (!mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    /**
     * 设置文字相关参数
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        // 设置文字相关参数
        setUpTextParams(textSize);
        // 设置画笔的字体大小
        mTextPaint.setTextSize(mTextSize);
        // 设置标题的字体大小
        mTitlePaint.setTextSize(mTitleSize);
        // 存储文字大小
        mSettingManager.setTextSize(mTextSize);
        if (mPageMode != SCROLL) {
            // 取消缓存
            mPrePageList = null;
            mNextPageList = null;
            // 如果当前已经显示数据
            if (isChapterListPrepare && mStatus == STATUS_FINISH) {
                // 重新计算当前页面
                dealLoadPageList(mCurChapter_id);

                // 防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
                if (mCurPage.position >= mCurPageList.size()) {
                    mCurPage.position = mCurPageList.size() - 1;
                }
                // 重新获取指定页面
                mCurPage = mCurPageList.get(mCurPage.position);
            }
            mPageView.drawCurPage(false);
        }
    }


    /**
     * 设置文字相关参数
     *
     * @param LineSpacing
     */
    public void setLineSpacingMode(float LineSpacing) {
        // 设置文字相关参数
        setUpLineSpacingParams(LineSpacing);
        if (mPageMode != SCROLL) {
            // 取消缓存
            mPrePageList = null;
            mNextPageList = null;

            // 如果当前已经显示数据
            if (isChapterListPrepare && mStatus == STATUS_FINISH) {
                // 重新计算当前页面
                dealLoadPageList(mCurChapter_id);

                // 防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
                if (mCurPage.position >= mCurPageList.size()) {
                    mCurPage.position = mCurPageList.size() - 1;
                }
                // 重新获取指定页面
                mCurPage = mCurPageList.get(mCurPage.position);
            }
            mPageView.drawCurPage(false);
        }
    }

    /**
     * 设置夜间模式
     *
     * @param nightMode
     */
    public void setNightMode(boolean nightMode) {
        mSettingManager.setNightMode(nightMode);
        isNightMode = nightMode;
        if (isNightMode) {
            setPageStyle(PageStyle.NIGHT);
        } else {
            setPageStyle(ReadSettingManager.getInstance().getLastLightPageStyle());
        }
    }

    /**
     * 设置页面样式
     *
     * @param pageStyle:页面样式
     */
    public void setPageStyle(PageStyle pageStyle) {
        mPageStyle = pageStyle;
        mSettingManager.setPageStyle(pageStyle);

        // 设置当前颜色样式
        mBgColor = ContextCompat.getColor(mContext, pageStyle.getFontColor());
        mTextColor = ContextCompat.getColor(mContext, pageStyle.getBgColor());

        mTipPaint.setColor(mTextColor);
        mTitlePaint.setColor(mTextColor);
        mTextPaint.setColor(mTextColor);
        mBatteryPaint.setColor(mTextColor);
        mBgPaint.setColor(mBgColor);

        mContext.bookReadLoad.setColorFilter(mTextColor);
        mContext.bookReadLoad.setAlpha(0.75f);

        if (purchaseLayout != null) {
            initPurchaseLayout();
        }
        if (authorFistNoteLayout != null) {
            initAuthorNoteLayout();
        }
        if (authorWordsLayout != null) {
            initAuthorWordsLayout(bookChapter);
        }
        if (rewardLayout != null) {
            initRewardLayout(bookChapter);
        }
        if (mPageMode != SCROLL) {
            mPageView.drawCurPage(false);
        }
    }

    /**
     * 翻页动画
     *
     * @param toPageMode:翻页模式
     * @see PageMode
     */
    public void setPageMode(PageMode toPageMode, PageMode fromMode, BookChapter bookChapter) {
        if (toPageMode != SCROLL) {
            mPageMode = toPageMode;
            mSettingManager.setPageMode(mPageMode);

            mPageView.setPageMode(mPageMode);
            // 重新绘制当前页
            if (fromMode != SCROLL) {
                mPageView.drawCurPage(false);
            } else {
                openChapter(bookChapter);
            }
        }
    }

    /**
     * 设置页面切换监听
     *
     * @param listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mPageChangeListener = listener;
        // 如果目录加载完之后才设置监听器，那么会默认回调
        if (isChapterListPrepare) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
    }

    /**
     * @return 获取当前页的状态
     */
    public int getPageStatus() {
        return mStatus;
    }

    /**
     * @return 获取书籍信息
     */
    public Book getCollBook() {
        return mCollBook;
    }

    /**
     * @return 获取章节目录
     */
    public List<BookChapter> getChapterCategory() {
        return mChapterList;
    }

    /**
     * @return 获取当前页的页码
     */
    public int getPagePos() {
        return mCurPage.position;
    }

    /**
     * @return 获取当前章节ID
     */
    public long getChapterPos() {
        return mCurChapter_id;
    }

    /**
     * @return 获取当前章节
     */
    public BookChapter getBookChapter() {
        return bookChapter;
    }

    /**
     * @return 获取距离屏幕的高度
     */
    public int getMarginHeight() {
        return mMarginHeight;
    }

    /**
     * 保存阅读记录
     */
    public void saveRecord() {
        if (mCurPageList == null || mChapterList.isEmpty()) {
            return;
        }
    }

    /**
     * 初始化书籍
     */
    private void prepareBook() {
        mCurChapter_id = bookChapter.chapter_id;
        mLastChapter_id = mCurChapter_id;
    }

    /**
     * 打开指定章节
     */
    public void openChapter() {
        isFirstOpen = false;
        if (!mPageView.isPrepare()) {
            return;
        }
        // 如果章节目录没有准备好
        if (!isChapterListPrepare) {
            mStatus = STATUS_LOADING;
            mPageView.drawCurPage(false);
            return;
        }
        // 如果获取到的章节目录为空
        if (mChapterList.isEmpty()) {
            mStatus = STATUS_CATEGORY_EMPTY;
            mPageView.drawCurPage(false);
            return;
        }

        if (parseCurChapter()) {
            // 如果章节从未打开
            if (!isChapterOpen) {
                int position = 0;
                if (markId > 0) {
                    // 书签
                    BookMarkBean bookMarkBean = ObjectBoxUtils.getBookMarkBean(markId);
                    if (bookMarkBean != null) {
                        position = bookMarkBean.getPosition();
                        if (position != 0) {
                            String chapterContent = ChapterManager.getInstance().getChapterContent(mCurPageList);
                            // 循环mCurPageList, 得到每个TxtPage的区间
                            for (TxtPage txtPage : mCurPageList) {
                                // 获取当前页的内容
                                String pageContent = txtPage.getLineTexts();
                                // 获取区间
                                if (!TextUtils.isEmpty(pageContent)) {
                                    int startCoordinate = chapterContent.indexOf(pageContent);
                                    int endCoordinate = startCoordinate + pageContent.length() - 1;
                                    if (bookMarkBean.getCoordinate() >= startCoordinate && bookMarkBean.getCoordinate() <= endCoordinate) {
                                        position = mCurPageList.indexOf(txtPage);
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        position = bookChapter.getPagePos();
                    }
                    markId = 0;
                } else {
                    position = bookChapter.getPagePos();
                }
                // 防止记录页的页号，大于当前最大页号
                if (position >= mCurPageList.size()) {
                    position = mCurPageList.size() - 1;
                }
                mCurPage = getCurPage(position);
                if (mCurPage.pageStyle >= 2) {
                    if (position + 1 < mCurPageList.size() && mCurPage.pageStyle == 3) {
                        mCurPage = getCurPage(position + 1);
                    } else {
                        mCurPage = getCurPage(position - 1);
                    }
                }
                mCancelPage = mCurPage;
                // 切换状态
                isChapterOpen = true;
            } else {
                mCurPage = getCurPage(0);
            }
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawCurPage(false);
        // 将章节设置为已读
        setIsRead(bookChapter);
        if (mCollBook.book_id < LOCAL_BOOKID) {
            ReadHistory.addReadHistory(mContext, Constant.BOOK_CONSTANT, mCollBook.book_id, mCurChapter_id);
        }
    }

    /**
     * 关闭书本
     */
    public void closeBook() {
        isChapterListPrepare = false;
        authorFistNoteLayout = null;
        authorWordsLayout = null;
        rewardLayout = null;
        mContext.stopBookReadLoad();
    }

    public boolean isChapterOpen() {
        return isChapterOpen;
    }

    /*******************************abstract method***************************************/

    /**
     * 刷新章节列表
     */
    public abstract void refreshChapterList();

    /**
     * 打开章节
     *
     * @param bookChapter
     */
    public abstract void openChapter(BookChapter bookChapter);

    /**
     * @param chapter
     * @return 获取章节的文本流
     */
    protected abstract BufferedReader getChapterReader(BookChapter chapter) throws Exception;

    /**
     * @return 章节数据是否存在
     */
    protected abstract boolean hasChapterData(BookChapter chapter);

    /************************************   绘制  ************************************/

    /**
     * 绘制界面
     *
     * @param bitmap
     * @param isUpdate
     */
    public void drawPage(Bitmap bitmap, boolean isUpdate) {
        if (mPageMode != SCROLL) {
            drawBackground(mPageView.getBgBitmap(), isUpdate);
        }
        if (!isUpdate) {
            drawContent(bitmap);
        }
        //更新绘制
        mPageView.invalidate();
    }

    /**
     * 绘制背景
     *
     * @param bitmap
     * @param isUpdate
     */
    private void drawBackground(Bitmap bitmap, boolean isUpdate) {
        ButtonY = mDisplayHeight - mTipPaint.getFontMetrics().bottom - percentMarginHeight;
        Canvas canvas = new Canvas(bitmap);
        if (!isUpdate) {
            /****   绘制背景   ****/
            canvas.drawColor(mBgColor);

            if (!mChapterList.isEmpty() && mCurPage != null && mCurPage.pageStyle != PAGE_STYLE_FIRST_PAGE) {

                /******  绘制左上角章节  ******/
                canvas.drawText(bookChapter.chapter_title, mMarginWidth, Title_height, mTipPaint);
                /******  绘制书名  ******/
                int bookNameWidth = (int) mTipPaint.measureText(mCollBook.getName());
                canvas.drawText(bookName, (mDisplayWidth - bookNameWidth) / 2, ButtonY, mTipPaint);

                /******  绘制百分比、页码  ******/
                if (mStatus == STATUS_FINISH && mCurPageList != null && totalChapter != 0) {
                    // 百分比
                    float myfPercent = (bookChapter.display_order * mCurPageList.size() +
                            mCurPage.position) / ((float) totalChapter * mCurPageList.size());
                    String strPercent = df.format(myfPercent * 100);
                    canvas.drawText(strPercent + "%", mDisplayWidth - percentWidth, ButtonY, mTipPaint);
                }
            }
        } else {
            // 擦除区域
            int bookNameWidth = (int) mTipPaint.measureText(mCollBook.getName());
            mBgPaint.setColor(mBgColor);
            canvas.drawRect(LeftMagin, ButtonY - LeftMagin + mBorderWidth,
                    (mDisplayWidth - bookNameWidth) / 2, mDisplayHeight, mBgPaint);
        }
        if (mCurPage != null && mCurPage.pageStyle != PAGE_STYLE_FIRST_PAGE && mStatus == STATUS_FINISH) {
            /******   绘制当前时间   ********/
            String time = StringUtils.dateConvert(System.currentTimeMillis(), DateUtils.FORMAT_TIME);
            canvas.drawText("  " + time, LeftMagin, ButtonY, mTipPaint);

            /******  绘制电池  ********/
            float mBatteryPercentage = (float) mBatteryLevel / 100.0f;
            float rect1Left = LeftMagin * 2 + TimeWidth;//电池外框left位置
            ButtonY = ButtonY - LeftMagin + mBorderWidth;
            //画电池外框
            rect1.set(rect1Left, ButtonY, rect1Left + mBatterWidth, ButtonY + mBatterHeight);
            rect2.set(rect1Left + mBorderWidth, ButtonY + mBorderWidth,
                    rect1Left + mBatterWidth - mBorderWidth, ButtonY + mBatterHeight - mBorderWidth);
            canvas.save();
            canvas.clipRect(rect2, Region.Op.DIFFERENCE);
            canvas.drawRoundRect(rect1, 5, 5, mBatteryPaint);
            canvas.restore();
            //画电量部分
            rect2.left += mBorderWidth;
            rect2.right -= mBorderWidth;
            rect2.right = rect2.left + rect2.width() * mBatteryPercentage;
            rect2.top += mBorderWidth;
            rect2.bottom -= mBorderWidth;
            canvas.drawRoundRect(rect2, 3, 3, mBatteryPaint);
            //画电池头
            int poleHeight = LeftMagin / 2;
            rect2.left = rect1.right;
            rect2.top = rect2.top + poleHeight / 4;
            rect2.right = rect1.right + mBorderWidth;
            rect2.bottom = rect2.bottom - poleHeight / 4;
            canvas.drawRoundRect(rect2, 1, 1, mBatteryPaint);
        }
    }

    /**
     * 绘制内容
     *
     * @param bitmap
     */
    private void drawContent(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        if (mPageMode == SCROLL) {
            canvas.drawColor(mBgColor);
        }
        /******  绘制内容  ******/
        if (mStatus != STATUS_FINISH) {
            //绘制字体
            String tip = "";
            switch (mStatus) {
                case STATUS_LOADING:
                    tip = "正在拼命加载中...";
                    break;
                case STATUS_ERROR:
                    tip = "加载失败(点击边缘重试)";
                    break;
                case STATUS_EMPTY:
                    tip = "重试";
                    break;
                case STATUS_PARING:
                    tip = "正在排版请等待...";
                    break;
                case STATUS_PARSE_ERROR:
                    tip = "文件解析错误";
                    break;
                case STATUS_CATEGORY_EMPTY:
                    tip = "目录列表为空";
                    break;
            }
            // 将提示语句放到正中间
            if (mStatus == STATUS_EMPTY) {
                initTryLayout();
                Bitmap bookReadTryLayout = ViewToBitmapUtil.convertViewToBitmap(mContext.bookReadTryLayout);
                RoundRectAgain = new RectF(mContext.bookReadTryLayout.getLeft(), mContext.bookReadTryLayout.getTop(),
                        mContext.bookReadTryLayout.getRight(), mContext.bookReadTryLayout.getBottom());
                Rect mSrcRect = new Rect(0, 0, bookReadTryLayout.getWidth(), bookReadTryLayout.getHeight());
                canvas.drawBitmap(bookReadTryLayout, mSrcRect, RoundRectAgain, videoTips);
                mContext.stopBookReadLoad();
            } else if (mStatus == STATUS_LOADING) {
                mContext.startBookReadLoad();
            } else {
                float textHeight = mTextSize;
                float textWidth = mTextPaint.measureText(tip);
                float pivotX = (mDisplayWidth - textWidth) / 2;
                float pivotY = (mDisplayHeight - textHeight) / 2;
                canvas.drawText(tip, pivotX, pivotY, mTextPaint);
                mContext.stopBookReadLoad();
            }
        } else {
            mContext.stopBookReadLoad();
            float top;
            if (mPageMode == SCROLL) {
                top = -mTextPaint.getFontMetrics().top;
            } else {
                top = Title_height + LeftMagin * 2;
            }

            // 设置总距离
            int interval = mTextInterval + (int) mTextPaint.getTextSize();
            int para = mTextPara + (int) mTextPaint.getTextSize();
            int titleInterval = mTitleInterval + (int) mTitlePaint.getTextSize();
            int titlePara = mTitlePara + (int) mTextPaint.getTextSize();
            String str = null;

            // 内容
            if (mCurPage.pageStyle == 0) {
                // 是否绘制标题
                if (mCurPage.titleLines != 0) {
                    // 对标题进行绘制
                    for (int i = 0; i < mCurPage.titleLines; ++i) {
                        str = mCurPage.lines.get(i);
                        // 设置顶部间距
                        if (i == 0) {
                            top += mTitlePara;
                        }

                        //计算文字显示的起始点
                        //进行绘制
                        canvas.drawText(str, mMarginWidth, top, mTitlePaint);

                        //设置尾部间距
                        if (i == mCurPage.titleLines - 1) {
                            top += titlePara;
                        } else {
                            //行间距
                            top += titleInterval;
                        }
                    }
                    top += isNotchEnable;
                } else {
                    top = (isNotchEnable + 6 * LeftMagin);
                }
                // 对内容进行绘制
                if (mCurPage.lines != null && !mCurPage.lines.isEmpty()) {
                    if (mCollBook.current_chapter_id_hasData != bookChapter.chapter_id) {
                        mCollBook.current_chapter_id_hasData = bookChapter.chapter_id;
                    }
                    for (int i = mCurPage.titleLines; i < mCurPage.lines.size(); ++i) {
                        str = mCurPage.lines.get(i);
                        canvas.drawText(str, mMarginWidth, top, mTextPaint);
                        if (str.endsWith("\n")) {
                            top += para;
                        } else {
                            top += interval;
                        }
                    }
                    if (bookChapter.is_preview == 1) {
                        drawPurchase(canvas);
                    } else {
                        if (mCurPage.isAuthorPage) {
                            drawAuthor(true, canvas, (int) top, bookChapter);
                        }
                    }
                }
            } else if (mCurPage.pageStyle == 1) {
                // 封面
                drawFirstPage(canvas);
            } else if (mCurPage.pageStyle == 2) {
                // 全屏作家的话
                drawAuthor(false, canvas, 0, bookChapter);
            } else if (mCurPage.pageStyle == 3) {
                // 广告
                if (baseAd != null) {
                    if (baseAd.ad_type != 1) {
                        this.canvas = canvas;
                    }
                    drawAD(canvas, bitmapCache.getBitmapFromCache("baseAdCenter"), true);
                }
            }
        }
    }

    /**
     * 重置
     *
     * @param w
     * @param h
     */
    void prepareDisplay(int w, int h) {
        // 获取PageView的宽高
        mDisplayWidth = w;
        mDisplayHeight = h;

        // 获取内容显示位置的大小
        mVisibleWidth = mDisplayWidth - mMarginWidth * 2;
        mVisibleHeight = mDisplayHeight - mMarginHeight * 2 - (isNotchEnable + 2 * LeftMagin);

        // 重置 PageMode
        mPageView.setPageMode(mPageMode);
        if (!isChapterOpen) {
            // 展示加载界面
            mPageView.drawCurPage(false);
            // 如果在 display 之前调用过 openChapter 肯定是无法打开的。
            // 所以需要通过 display 再重新调用一次。
            if (!isFirstOpen) {
                // /打开书籍
                openChapter();
            }
        } else {
            // 如果章节已显示，那么就重新计算页面
            if (mStatus == STATUS_FINISH) {
                dealLoadPageList(mCurChapter_id);
                // 重新设置文章指针的位置
                mCurPage = getCurPage(mCurPage.position);
            }
            mPageView.drawCurPage(false);
        }
    }

    /********************************  处理翻页时数据  *******************************/

    /**
     * @return 翻阅上一页
     */
    boolean prev() {
        // 以下情况禁止翻页
        if (!canTurnPage() || mContext.isShowBookEnd) {
            return false;
        }

        if (mStatus == STATUS_FINISH) {
            // 先查看是否存在上一页
            TxtPage prevPage = getPrevPage();
            if (prevPage != null) {
                mCancelPage = mCurPage;
                mCurPage = prevPage;
                mPageView.drawNextPage();
                return true;
            }
        }
        if (!hasPrevChapter()) {
            return false;
        }
        mCancelPage = mCurPage;
        if (parsePrevChapter()) {
            mCurPage = getPrevLastPage();
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawNextPage();
        addReadHistory();
        return true;
    }

    /**
     * @return 翻到下一页
     */
    boolean next() {
        // 以下情况禁止翻页
        if (!canTurnPage()) {
            return false;
        }

        if (mStatus == STATUS_FINISH) {
            // 先查看是否存在下一页
            TxtPage nextPage = getNextPage();
            if (nextPage != null) {
                mCancelPage = mCurPage;
                mCurPage = nextPage;
                mPageView.drawNextPage();
                return true;
            }
        }
        if (!hasNextChapter()) {
            return false;
        }
        mCancelPage = mCurPage;
        // 解析下一章数据
        if (parseNextChapter()) {
            mCurPage = mCurPageList.get(0);
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawNextPage();
        addReadHistory();
        return true;
    }

    /**
     * 解析上一章数据
     *
     * @return:数据是否解析成功
     */
    boolean parsePrevChapter() {
        // 加载上一章数据
        long prevChapter = bookChapter.last_chapter;
        if (prevChapter == 0) {
            return false;
        }
        bookChapter = ChapterManager.getInstance().getChapter(prevChapter);
        mLastChapter_id = mCurChapter_id;
        mCurChapter_id = prevChapter;
        // 将章节设置为已读
        setIsRead(bookChapter);
        // 当前章缓存为下一章
        mNextPageList = mCurPageList;
        // 判断是否具有上一章缓存
        if (mPrePageList != null) {
            mCurPageList = mPrePageList;
            mPrePageList = null;
            // 回调

            chapterChangeCallback();
        } else {
            dealLoadPageList(prevChapter);
        }
        return mCurPageList != null && !mCurPageList.isEmpty();
    }

    /**
     * 加载章节数据
     *
     * @return：章节是否有数据
     */
    boolean parseCurChapter() {
        // 解析数据
        dealLoadPageList(mCurChapter_id);
        // 获取章节数据
        return mCurPageList != null && !mCurPageList.isEmpty();
    }

    /**
     * 解析下一章数据
     *
     * @return:返回解析成功还是失败
     */
    boolean parseNextChapter() {
        long nextChapter = bookChapter.next_chapter;
        if (nextChapter == 0) {
            return false;
        }
        bookChapter = ChapterManager.getInstance().getChapter(nextChapter);
        mLastChapter_id = mCurChapter_id;
        mCurChapter_id = nextChapter;
        // 将章节设置为已读
        setIsRead(bookChapter);
        // 将当前章的页面列表，作为上一章缓存
        mPrePageList = mCurPageList;
        // 是否下一章数据已经预加载了
        if (mNextPageList != null) {
            mCurPageList = mNextPageList;
            mNextPageList = null;
            // 回调
            chapterChangeCallback();
        } else {
            // 处理页面解析
            dealLoadPageList(nextChapter);
        }
        // 预加载下一页面
        //preLoadNextChapter();
        return mCurPageList != null && !mCurPageList.isEmpty();
    }

    /**
     * 加载章节页码，并加入缓存
     *
     * @param chapterId
     */
    private void dealLoadPageList(long chapterId) {
        try {
            mCurPageList = loadPageList(chapterId);
            // 重置
            if (mCurPageList != null) {
                if (mCurPageList.isEmpty()) {
                    mStatus = STATUS_EMPTY;
                    // 添加一个空数据
                    TxtPage page = new TxtPage();
                    page.lines = new ArrayList<>(1);
                    mCurPageList.add(page);
                } else {
                    mStatus = STATUS_FINISH;
                }
            } else {
                mStatus = STATUS_LOADING;
                // 添加一个空数据
                mCurPageList = new ArrayList<>();
                TxtPage page = new TxtPage();
                page.lines = new ArrayList<>(1);
                mCurPageList.add(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mCurPageList = null;
            mStatus = STATUS_ERROR;
        }
        // 回调
        chapterChangeCallback();
    }

    private void chapterChangeCallback() {
        if (mPageChangeListener != null) {
            mPageChangeListener.onChapterChange(bookChapter);
            mPageChangeListener.onPageCountChange(mCurPageList != null ? mCurPageList.size() : 0);
        }
    }

    /**
     * 取消翻页
     *
     * @param isNext
     */
    void pageCancel(boolean isNext) {
        if (mCurPageList == null) {
            return;
        }
//      // MyToash.Log("read_pageCancel_index", mCurPageList.size() + "--index--" + mCurPage.position);
        if (isNext) {
            // 取消向右翻，判断是否为章节末尾页
            if (mCurPage.position == 0) {
                // 表示上一章末尾页翻到了这一章
                if (mPrePageList != null) {
                    cancelNextChapter();
                } else {
                    if (parsePrevChapter()) {
                        mCurPage = getPrevLastPage();
                    } else {
                        mCurPage = new TxtPage();
                    }
                }
            } else {
                mCurPage = mCancelPage;
            }
        } else {
            // 取消向左翻，判断是否为章节起始页
            if (mCurPage.position == mCurPageList.size() - 1) {
                // 加载上一章取消了
                if (mNextPageList != null) {
                    cancelPreChapter();
                } else {
                    if (parseNextChapter()) {
                        mCurPage = mCurPageList.get(0);
                    } else {
                        mCurPage = new TxtPage();
                    }
                }
            } else {
                mCurPage = mCancelPage;
            }
        }
    }

    /**
     * 重置下一章
     */
    private void cancelNextChapter() {
        long temp = mLastChapter_id;
        mLastChapter_id = mCurChapter_id;
        mCurChapter_id = temp;
        bookChapter = ChapterManager.getInstance().getChapter(mCurChapter_id);

        mNextPageList = mCurPageList;
        mCurPageList = mPrePageList;
        mPrePageList = null;

        chapterChangeCallback();

        mCurPage = getPrevLastPage();
        mCancelPage = null;
    }

    /**
     * 重置上一章
     */
    private void cancelPreChapter() {
        long temp = mLastChapter_id;
        mLastChapter_id = mCurChapter_id;
        mCurChapter_id = temp;
        bookChapter = ChapterManager.getInstance().getChapter(mCurChapter_id);

        mPrePageList = mCurPageList;
        mCurPageList = mNextPageList;
        mNextPageList = null;

        chapterChangeCallback();

        mCurPage = getCurPage(0);
        mCancelPage = null;
    }

    /**
     * 预加载上下章
     */
    public void preLoadNextChapter() {
        lordNextData(hasNextChapter(), hasPrevChapter());
    }

    /******************************   private method  ***********************************/

    /**
     * @param chapterId
     * @return 二次加工页面列表
     */
    private List<TxtPage> loadPageList(long chapterId) {
        try {
            // 获取章节
            BookChapter chapter = ChapterManager.getInstance().getChapter(chapterId);
            // 判断章节是否存在
            boolean f = !hasChapterData(chapter);
            if (f) {
                return new ArrayList<>();
            }
            // 获取章节的文本流
            BufferedReader reader = getChapterReader(chapter);
            // 获取章节页
            List<TxtPage> chapters = loadPages(chapter, reader);
            // 添加广告
            if (chapter.is_preview == 0 && getIsReadCenterAd(mContext) &&
                    !isCloseAd && isAdLord && baseAd != null && !chapters.isEmpty()
                    && InternetUtils.internet(mContext)) {
                int adPage = Constant.getReadAdCenterPage(mContext);
                List<TxtPage> adChapters = new ArrayList<>();
                int adNum = chapters.size() / adPage;
                if (adNum > 0) {
                    // 按固定页数插广告页
                    for (int i = 0; i < chapters.size(); i++) {
                        TxtPage txtPage = chapters.get(i);
                        txtPage.position = adChapters.size();
                        adChapters.add(txtPage);
                        if ((i + 1) % adPage == 0) {
                            adChapters.add(getNewEmptyPage(adChapters.size(), PAGE_STYLE_AD_PAGE));
                        } else if ((i + 1) % (adPage+1) == 0) {//广告页的下一页
                            txtPage.pageAdLord=true;
                        }
                    }
                    return adChapters;
                }
            }
            if (!chapters.isEmpty()) {
                mStatus = STATUS_FINISH;
            }
            return chapters;
        } catch (Exception e) {
            mStatus = STATUS_EMPTY;
            return new ArrayList<>();
        }
    }

    /**
     * @param chapter：章节信息
     * @param br：章节的文本流
     * @return 将章节数据，解析成页面列表
     */
    private List<TxtPage> loadPages(BookChapter chapter, BufferedReader br) {
        //生成的页面
        List<TxtPage> pages = new ArrayList<>();
        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        int rHeight = 0;
        if (chapter.is_preview == 0) {
            rHeight = mVisibleHeight;
        } else {
            rHeight = mVisibleHeight - mVisibleHeight / 3;
        }
        // 记录最后一页的剩余高度
        int surplusHeight = 0;
        int titleLinesCount = 0;
        // 是否展示标题
        boolean showTitle = true;
        String paragraph = chapter.getChapter_title();//默认展示标题
        try {
            while (showTitle || (paragraph = br.readLine()) != null) {
                paragraph = StringUtils.convertCC(paragraph, mContext);
                // 重置段落
                if (!showTitle) {
                    paragraph = paragraph.replaceAll("\\s", "");
                    // 如果只有换行符，那么就不执行
                    if (paragraph.equals("")) {
                        continue;
                    }
                    paragraph = StringUtils.halfToFull("  " + paragraph + "\n");
                } else {
                    // 设置 title 的顶部间距
                    rHeight -= mTitlePara;
                }
                int wordCount = 0;
                String subStr = null;
                while (paragraph.length() > 0) {
                    //当前空间，是否容得下一行文字
                    if (showTitle) {
                        rHeight -= mTitlePaint.getTextSize();
                    } else {
                        rHeight -= mTextPaint.getTextSize();
                    }
                    // 判断高度是否足够装下内容，直到内容填充满了，创建 TextPage，否则往下运行
                    // 直到内容填充满了，创建 TextPage
                    if (rHeight <= 0) {
                        // 创建Page
                        if (chapter.display_order == 0 && chapter.last_chapter == 0 && pages.size() == 0) {
                            // 封面
                            initAuthorNoteLayout();
                            pages.add(getNewEmptyPage(pages.size(), PAGE_STYLE_FIRST_PAGE));
                        }
                        TxtPage page = new TxtPage();
                        page.position = pages.size();
                        page.title = StringUtils.convertCC(chapter.getChapter_title(), mContext);
                        page.lines = new ArrayList<>(lines);
                        page.titleLines = titleLinesCount;
                        pages.add(page);
                        // 重置Lines
                        lines.clear();
                        if (chapter.is_preview == 0) {
                            rHeight = mVisibleHeight;
                        } else {
                            // 保证付费章节只有一页
                            if (chapter.display_order == 0 && chapter.last_chapter == 0) {
                                if (pages.size() >= 2) {
                                    return pages;
                                }
                            } else {
                                if (pages.size() >= 1) {
                                    return pages;
                                }
                            }
                        }
                        titleLinesCount = 0;
                        continue;
                    }

                    // 测量一行占用的字节数
                    if (showTitle) {
                        wordCount = mTitlePaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    } else {
                        wordCount = mTextPaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    }

                    subStr = paragraph.substring(0, wordCount);
                    if (!subStr.equals("\n")) {
                        //将一行字节，存储到lines中
                        lines.add(subStr);

                        //设置段落间距
                        if (showTitle) {
                            titleLinesCount += 1;
                            rHeight -= mTitleInterval;
                        } else {
                            rHeight -= mTextInterval;
                        }
                    }
                    // 裁剪，内容超出宽度，
                    paragraph = paragraph.substring(wordCount);
                }
                // 增加段落的间距
                if (!showTitle && lines.size() != 0) {
                    rHeight = rHeight - mTextPara + mTextInterval;
                }

                if (showTitle) {
                    rHeight = rHeight - mTitlePara + mTitleInterval;
                    showTitle = false;
                }
                surplusHeight = rHeight;
            }
            // 内容有多余会进入此方法，此处为最后一页
            if (lines.size() != 0) {
                // 创建Page
                TxtPage page = new TxtPage();
                page.position = pages.size();
                page.title = StringUtils.convertCC(chapter.getChapter_title(), mContext);
                page.lines = new ArrayList<>(lines);
                page.titleLines = titleLinesCount;
                // 判断剩余高度是否可以存放作家的话
                if (chapter.is_preview == 0) {
                    // 判断高度是否足够
                    boolean isShow[] = isEnoughToShow(surplusHeight, bookChapter);
                    if (isShow[0]) {
                        if (isShow[1]) {
                            // 高度足够
                            page.isAuthorPage = true;
                            pages.add(page);
                        } else {
                            pages.add(page);
                            // 插入作家的话界面
                            pages.add(getNewEmptyPage(pages.size(), PAGE_STYLE_AUTHOR_PAGE));
                        }
                        lines.clear();
                        return pages;
                    }
                } else {
                    // 保证付费章节只有一页
                    if (chapter.display_order == 0 && chapter.last_chapter == 0) {
                        if (pages.size() >= 2) {
                            return pages;
                        }
                    } else {
                        if (pages.size() >= 1) {
                            return pages;
                        }
                    }
                }
                pages.add(page);
                // 重置Lines
                lines.clear();
            } else {
                // 设置为最后一页
                if (chapter.is_preview == 0) {
                    pages.add(getNewEmptyPage(pages.size(), PAGE_STYLE_AUTHOR_PAGE));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(br);
        }
        return pages;
    }

    /**
     * @return 获取初始显示的页面
     */
    private TxtPage getCurPage(int pos) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return 获取上一个页面
     */
    private TxtPage getPrevPage() {
        int pos = mCurPage.position - 1;
        if (pos < 0) {
            return null;
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return 获取下一的页面
     */
    private TxtPage getNextPage() {
        int pos = mCurPage.position + 1;
        if (pos >= mCurPageList.size()) {
            return null;
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return 获取上一个章节的最后一页
     */
    private TxtPage getPrevLastPage() {
        int pos = mCurPageList.size() - 1;
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @param position
     * @return 获取空页面
     */
    private TxtPage getNewEmptyPage(int position, int type) {
        TxtPage adTxtPage = new TxtPage();
        adTxtPage.pageStyle = type;
        adTxtPage.position = position;
        adTxtPage.title = "";
        adTxtPage.lines = new ArrayList<>();
        adTxtPage.titleLines = -1;
        if (type == PAGE_STYLE_AUTHOR_PAGE) {
            adTxtPage.isAuthorPage = true;
        }
        return adTxtPage;
    }

    /**
     * @return 根据当前状态，决定是否能够翻页
     */
    private boolean canTurnPage() {
        if (!isChapterListPrepare) {
            return false;
        }

        if (mStatus == STATUS_PARSE_ERROR || mStatus == STATUS_PARING) {
            return false;
        } else if (mStatus == STATUS_ERROR) {
            mStatus = STATUS_LOADING;
        }
        return true;
    }

    /**
     * 将章节设置为已读
     *
     * @param bookChapter
     */
    private void setIsRead(BookChapter bookChapter) {
        if (bookChapter != null && bookChapter.is_read == 0) {
            bookChapter.is_read = 1;
            ObjectBoxUtils.addData(bookChapter, BookChapter.class);
        }
    }

    /**
     * 添加历史记录
     */
    private void addReadHistory() {
        if (mCollBook.book_id < LOCAL_BOOKID) {
            ChapterManager.getInstance().mCurrentChapter = bookChapter;
            ReadHistory.addReadHistory(mContext, Constant.BOOK_CONSTANT, mCollBook.book_id, mCurChapter_id);
        }
    }

    /**
     * @return 是否有上一章
     */
    private boolean hasPrevChapter() {
        if (bookChapter == null) {
            return false;
        }
        return bookChapter.last_chapter != 0;
    }

    /**
     * @return 是否有下一章
     */
    private boolean hasNextChapter() {
        if (bookChapter == null) {
            return false;
        }
        return bookChapter.next_chapter != 0;
    }

    /*****************     用于绘制封面、作家的话、广告的处理方法    *****************/

    /**
     * 绘制阅读器起始页
     *
     * @param canvas
     */
    private void drawFirstPage(Canvas canvas) {
        Bitmap boxBitmap, bookCoverBitmap;
        int marginTop = 0;
        if (isNotchEnable > 0) {
            marginTop = isNotchEnable + ImageUtil.dp2px(mContext, 15);
        } else {
            marginTop = ImageUtil.dp2px(mContext, 30);
        }
        int boxBitmapHeight = mDisplayHeight - marginTop - ImageUtil.dp2px(mContext, 10);
        boxBitmap = BitmapCache.getInstance().getBitmapFromCache("bookFirstBg");
        if (boxBitmap == null) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_book_first_box);
            boxBitmap = Bitmap.createScaledBitmap(mBitmap, mVisibleWidth
                    , boxBitmapHeight, true);
            BitmapCache.getInstance().addBitmapToCache("bookFirstBg", boxBitmap);
        }
        // 设置框
        if (boxBitmap != null) {
            Rect boxSizeRect = new Rect(0, 0, mVisibleWidth,
                    mDisplayHeight - ImageUtil.dp2px(mContext, 10));
            Rect boxRangeRect = new Rect(mMarginWidth, marginTop, mMarginWidth + mVisibleWidth,
                    mDisplayHeight - ImageUtil.dp2px(mContext, 10));
            canvas.drawBitmap(boxBitmap, boxSizeRect, boxRangeRect, authorPaint);
        }
        bookCoverBitmap = BitmapCache.getInstance().getBitmapFromCache(String.valueOf(mCollBook.getBook_id()));
        // 设置封面图
        int bookCoverWidth = mDisplayWidth * 2 / 5;
        int bookCoverHeight = bookCoverWidth * 4 / 3;
        if (bookCoverBitmap != null) {
            Rect imageSizeRect = new Rect(0, 0, bookCoverWidth, bookCoverHeight);
            Rect imageRangeRect = new Rect((mDisplayWidth - bookCoverWidth) / 2, mDisplayHeight / 6,
                    (mDisplayWidth - bookCoverWidth) / 2 + bookCoverWidth, mDisplayHeight / 6 + bookCoverHeight);
            canvas.drawBitmap(bookCoverBitmap, imageSizeRect, imageRangeRect, authorPaint);
        }
        // 绘制书名
        bookFirstPaint.setColor(mTextColor);
        bookFirstPaint.setTextSize(ImageUtil.dp2px(mContext, 18));
        int bookNameWidth = (int) bookFirstPaint.measureText(mCollBook.getName());
        canvas.drawText(mCollBook.getName(), (mDisplayWidth - bookNameWidth) / 2,
                (mDisplayHeight / 6) + bookCoverHeight + ImageUtil.dp2px(mContext, 35), bookFirstPaint);
        if (!TextUtils.isEmpty(mCollBook.author_note) && !mCollBook.author_note.equals(" ")) {
            // 绘制作者的扉语
            initAuthorNoteLayout();
            Bitmap authorBitmap = ViewToBitmapUtil.getLayoutBitmap(authorFistNoteLayout);
            if (authorBitmap != null) {
                int top = (mDisplayHeight / 6) + bookCoverHeight + ImageUtil.dp2px(mContext, 100);
                Rect authorSizeRect = new Rect(0, 0, authorBitmap.getWidth(), authorBitmap.getHeight());
                Rect authorRangeRect = new Rect((mDisplayWidth - authorBitmap.getWidth()) / 2, top,
                        (mDisplayWidth - authorBitmap.getWidth()) / 2 + authorBitmap.getWidth(),
                        top + authorBitmap.getHeight());
                canvas.drawBitmap(authorBitmap, authorSizeRect, authorRangeRect, authorPaint);
            } else {
                if (!TextUtils.isEmpty(mCollBook.author) && !mCollBook.author.equals("null")) {
                    // 绘制作者
                    bookFirstPaint.setTextSize(ImageUtil.dp2px(mContext, 14));
                    int bookAuthorWidth = (int) bookFirstPaint.measureText(mCollBook.author +
                            LanguageUtil.getString(mContext, R.string.ReadActivity_works));
                    canvas.drawText(mCollBook.author + LanguageUtil.getString(mContext, R.string.ReadActivity_works),
                            (mDisplayWidth - bookAuthorWidth) / 2,
                            (mDisplayHeight / 6) + bookCoverHeight + ImageUtil.dp2px(mContext, 65), bookFirstPaint);
                }
            }
        } else {
            if (!TextUtils.isEmpty(mCollBook.author) && !mCollBook.author.equals("null")) {
                // 绘制作者
                bookFirstPaint.setTextSize(ImageUtil.dp2px(mContext, 14));
                int bookAuthorWidth = (int) bookFirstPaint.measureText(mCollBook.author +
                        LanguageUtil.getString(mContext, R.string.ReadActivity_works));
                canvas.drawText(mCollBook.author + LanguageUtil.getString(mContext, R.string.ReadActivity_works),
                        (mDisplayWidth - bookAuthorWidth) / 2,
                        (mDisplayHeight / 6) + bookCoverHeight + ImageUtil.dp2px(mContext, 65), bookFirstPaint);
            }
        }
    }

    /**
     * 初始化时设置内容
     */
    @SuppressLint("SetTextI18n")
    public void initAuthorNoteLayout() {
        if (mCollBook != null && !TextUtils.isEmpty(mCollBook.author_note) && !mCollBook.author_note.equals(" ")) {
            authorFistNoteLayout.setVisibility(View.INVISIBLE);
            TextView note = authorFistNoteLayout.findViewById(R.id.book_first_content_tv);
            TextView author = authorFistNoteLayout.findViewById(R.id.book_first_author_tv);
            note.setTextColor(mTextColor);
            author.setTextColor(mTextColor);
            note.setText(mCollBook.author_note);
            author.setText("—" + mCollBook.author_name);
        } else {
            authorFistNoteLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 用于判断高度是否足够
     *
     * @param top
     * @param chapter
     * @return 是否显示，高度是否足够
     */
    private boolean[] isEnoughToShow(float top, BookChapter chapter) {
        Bitmap authorWordsBitmap = null;
        if (chapter.author_note != null && !TextUtils.isEmpty(chapter.author_note)) {
            initAuthorWordsLayout(chapter);
            authorWordsBitmap = ViewToBitmapUtil.getLayoutBitmap(authorWordsLayout, mBgColor);
        }
        int height = 0;
        if (authorWordsBitmap != null) {
            height = authorWordsBitmap.getHeight();
        }
        if (!(!tab1 && !tab2 && !tab3)) {
            height += ImageUtil.dp2px(mContext, 40);
        }
        if (top < 0) {
            return new boolean[]{height != 0, false};
        }
        return new boolean[]{height != 0, top > height};
    }

    /**
     * 设置作家的话
     *
     * @param bookChapter
     */
    public void initAuthorWordsLayout(BookChapter bookChapter) {
        if (bookChapter != null && mCollBook != null && bookChapter.author_note != null
                && !TextUtils.isEmpty(bookChapter.author_note)) {
            authorWordsLayout.setVisibility(View.INVISIBLE);
            LinearLayout linearLayout = authorWordsLayout.findViewById(R.id.book_author_layout);
            View line = authorWordsLayout.findViewById(R.id.book_author_line);
            TextView title = authorWordsLayout.findViewById(R.id.book_author_title);
            TextView name = authorWordsLayout.findViewById(R.id.book_author_name);
            ImageView imageView = authorWordsLayout.findViewById(R.id.book_author_image);
            TextView content = authorWordsLayout.findViewById(R.id.book_author_content);
            linearLayout.setBackground(MyShape.setMyShapeWithAlpha(ImageUtil.dp2px(mContext, 5), mTextColor, 30));
            line.setBackgroundColor(mTextColor);
            title.setTextColor(mTextColor);
            name.setTextColor(mTextColor);
            content.setTextColor(mTextColor);
            MyGlide.GlideImageHeadNoSize(mContext, mCollBook.author_avatar, imageView);
            name.setText(mCollBook.author_name);
            content.setText(bookChapter.author_note);
        } else {
            authorWordsLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 绘制作家的话
     *
     * @param isEnough 高度是否足够
     * @param canvas   画布
     * @param height   高度
     * @param chapter  需要获取Bitmap的章节Id
     */
    private void drawAuthor(boolean isEnough, Canvas canvas, int height, BookChapter chapter) {
        // 绘制作家的话
        if (chapter.author_note != null && !TextUtils.isEmpty(chapter.author_note)) {
            initAuthorWordsLayout(chapter);
            Bitmap authorWordsBitmap = ViewToBitmapUtil.getLayoutBitmap(authorWordsLayout, mBgColor);
            if (authorWordsBitmap != null) {
                int top;
                if (isEnough && height != 0) {
                    top = height;
                } else {
                    top = ImageUtil.dp2px(mContext, 80);
                }
                Rect authorSizeRect = new Rect(0, 0, authorWordsBitmap.getWidth(), authorWordsBitmap.getHeight());
                Rect authorRangeRect = new Rect(ImageUtil.dp2px(mContext, 15), top,
                        mDisplayWidth - ImageUtil.dp2px(mContext, 15), top + authorWordsBitmap.getHeight());
                canvas.drawBitmap(authorWordsBitmap, authorSizeRect, authorRangeRect, authorPaint);
            }
        }
        // 绘制底部
        if (!(!tab1 && !tab2 && !tab3)) {
            initRewardLayout(chapter);
            Bitmap rewardBitmap = ViewToBitmapUtil.getLayoutBitmap(rewardLayout, mBgColor);
            if (rewardBitmap != null) {
                int top = mDisplayHeight - rewardBitmap.getHeight() - ImageUtil.dp2px(mContext, 30);
                Rect rewardSizeRect = new Rect(0, 0, rewardBitmap.getWidth(), rewardBitmap.getHeight());
                Rect rewardRangeRect = new Rect(0, top, mDisplayWidth, top + rewardBitmap.getHeight());
                canvas.drawBitmap(rewardBitmap, rewardSizeRect, rewardRangeRect, authorPaint);
            }
        }
    }

    /**
     * 绘制广告
     *
     * @param canvas
     */
    public FrameLayout frameLayoutAd;
    public BaseAd baseAd;
    public Canvas canvas;
    public boolean USE_AD_VIDEO;

    public void drawAD(Canvas canvas, Bitmap bitmap, boolean drawTips) {
        putLookVideoS();
        Rect mSrcRect = new Rect(0, 0, AD_WIDTH, AD_H);
        if (bitmap != null) {
            Rect mDestRect = new Rect(AD_MAGIN, AD_TOP, AD_WIDTH + AD_MAGIN, AD_BUTTOM);
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, videoTips);
        }
        if (USE_AD_VIDEO && drawTips) {
            float ClickH = LookVideoS[2];
            float srrollTextH = (ClickH + AD_BUTTOM) / 2;
            videoTips.setTextSize(CommonUtil.sp2px(mContext, 20));
            float xS = (mDisplayWidth - scrollClickWidth) / 2;
            videoTips.setAlpha(102);
            canvas.drawText(scrollClickText, xS, srrollTextH, videoTips);
            videoTips.setAlpha(153);
            videoTips.setTextSize(CommonUtil.sp2px(mContext, 15));
            float x = LookVideoS[0];
            canvas.drawText(lookVideoText, x, ClickH, videoTips);
            float h2 = ClickH + ImageUtil.dp2px(mContext, 4);
            canvas.drawLine(x, h2, x + ADVideo_text_WIDTH, h2, videoTips);
            videoTips.setTextSize(CommonUtil.sp2px(mContext, 12));
            videoTips.setAlpha(255);
        }
    }

    public void putLookVideoS() {
        if (LookVideoS[0] == 0) {
            LookVideoS[0] = (mDisplayWidth - ADVideo_text_WIDTH) / 2;
            LookVideoS[1] = (mDisplayWidth + ADVideo_text_WIDTH) / 2;
            int ClickH = (int) (ButtonY - ImageUtil.dp2px(mContext, 40));
            LookVideoS[2] = ClickH;
            LookVideoS[3] = ClickH + ImageUtil.dp2px(mContext, 26);
        }
    }

    public int[] getLookVideoS() {
        return LookVideoS;
    }

    /**
     * 重试按钮
     */
    private void initTryLayout() {
        if (mContext.bookReadTryLayout != null) {
            mContext.bookReadTryLayout.setBackgroundColor(Color.TRANSPARENT);
            TextView tips = mContext.bookReadTryLayout.findViewById(R.id.book_read_try_tips);
            TextView btn = mContext.bookReadTryLayout.findViewById(R.id.book_read_again_try);
            tips.setTextColor(mTextColor);
            btn.setTextColor(mTextColor);
            btn.setBackground(MyShape.setMyshapeStroke(mContext, 40, 1,
                    mTextColor, Color.TRANSPARENT));
        }
    }

    /**
     * 初始化时设置内容
     *
     * @param bookChapter
     */
    public void initRewardLayout(BookChapter bookChapter) {
        if (bookChapter != null) {
            View firstLine = rewardLayout.findViewById(R.id.book_bottom_first_line);
            View secondLine = rewardLayout.findViewById(R.id.book_bottom_second_line);
            firstLine.setBackgroundColor(mTextColor);
            secondLine.setBackgroundColor(mTextColor);
            LinearLayout reward = rewardLayout.findViewById(R.id.book_bottom_reward_layout);
            LinearLayout monthlyPass = rewardLayout.findViewById(R.id.book_bottom_monthly_pass_layout);
            LinearLayout comment = rewardLayout.findViewById(R.id.book_bottom_comment_layout);
            if ((bookChapter.reward_num == null || Constant.getRewardSwitch(mContext) != 1) &&
                    (bookChapter.ticket_num == null || Constant.getMonthlyTicket(mContext) != 1) &&
                    bookChapter.comment_num == null) {
                tab1 = tab2 = tab3 = false;
                rewardLayout.setVisibility(View.GONE);
            } else {
                rewardLayout.setVisibility(View.INVISIBLE);
                if ((bookChapter.reward_num == null || Constant.getRewardSwitch(mContext) != 1) &&
                        (bookChapter.ticket_num == null || Constant.getMonthlyTicket(mContext) != 1)) {
                    reward.setVisibility(View.GONE);
                    monthlyPass.setVisibility(View.GONE);
                    firstLine.setVisibility(View.GONE);
                    secondLine.setVisibility(View.GONE);
                    tab1 = tab2 = false;
                    if (bookChapter.comment_num == null) {
                        comment.setVisibility(View.GONE);
                        tab3 = false;
                    } else {
                        comment.setVisibility(View.VISIBLE);
                        tab3 = true;
                        TextView commentTopTv = rewardLayout.findViewById(R.id.book_bottom_comment_top_tv);
                        TextView commentBottomTv = rewardLayout.findViewById(R.id.book_bottom_comment_bottom_tv);
                        commentTopTv.setTextColor(mTextColor);
                        commentBottomTv.setTextColor(mTextColor);
                        commentBottomTv.setText(bookChapter.comment_num);
                    }
                } else if ((bookChapter.ticket_num == null || Constant.getMonthlyTicket(mContext) != 1) &&
                        bookChapter.comment_num == null) {
                    monthlyPass.setVisibility(View.GONE);
                    comment.setVisibility(View.GONE);
                    firstLine.setVisibility(View.GONE);
                    secondLine.setVisibility(View.GONE);
                    tab2 = tab3 = false;
                    if (bookChapter.reward_num == null || Constant.getRewardSwitch(mContext) != 1) {
                        reward.setVisibility(View.GONE);
                        tab1 = false;
                    } else {
                        reward.setVisibility(View.VISIBLE);
                        tab1 = true;
                        TextView rewardTopTv = rewardLayout.findViewById(R.id.book_bottom_reward_top_tv);
                        TextView rewardBottomTv = rewardLayout.findViewById(R.id.book_bottom_reward_bottom_tv);
                        rewardTopTv.setTextColor(mTextColor);
                        rewardBottomTv.setTextColor(mTextColor);
                        rewardBottomTv.setText(bookChapter.reward_num);
                    }
                } else {
                    if (bookChapter.reward_num == null || Constant.getRewardSwitch(mContext) != 1) {
                        reward.setVisibility(View.GONE);
                        firstLine.setVisibility(View.GONE);
                        tab1 = false;
                    } else {
                        reward.setVisibility(View.VISIBLE);
                        firstLine.setVisibility(View.VISIBLE);
                        tab1 = true;
                        TextView rewardTopTv = rewardLayout.findViewById(R.id.book_bottom_reward_top_tv);
                        TextView rewardBottomTv = rewardLayout.findViewById(R.id.book_bottom_reward_bottom_tv);
                        rewardTopTv.setTextColor(mTextColor);
                        rewardBottomTv.setTextColor(mTextColor);
                        rewardBottomTv.setText(bookChapter.reward_num);
                    }
                    if (bookChapter.ticket_num == null || Constant.getMonthlyTicket(mContext) != 1) {
                        monthlyPass.setVisibility(View.GONE);
                        tab2 = false;
                    } else {
                        monthlyPass.setVisibility(View.VISIBLE);
                        tab2 = true;
                        TextView monthlyPassTopTv = rewardLayout.findViewById(R.id.book_bottom_monthly_pass_top_tv);
                        TextView monthlyPassBottomTv = rewardLayout.findViewById(R.id.book_bottom_monthly_pass_bottom_tv);
                        monthlyPassTopTv.setTextColor(mTextColor);
                        monthlyPassBottomTv.setTextColor(mTextColor);
                        monthlyPassBottomTv.setText(bookChapter.ticket_num);
                    }
                    if (bookChapter.comment_num == null) {
                        comment.setVisibility(View.GONE);
                        secondLine.setVisibility(View.GONE);
                        tab3 = false;
                    } else {
                        comment.setVisibility(View.VISIBLE);
                        secondLine.setVisibility(View.VISIBLE);
                        tab3 = true;
                        TextView commentTopTv = rewardLayout.findViewById(R.id.book_bottom_comment_top_tv);
                        TextView commentBottomTv = rewardLayout.findViewById(R.id.book_bottom_comment_bottom_tv);
                        commentTopTv.setTextColor(mTextColor);
                        commentBottomTv.setTextColor(mTextColor);
                        commentBottomTv.setText(bookChapter.comment_num);
                    }
                }
            }
        }
    }

    /**
     * 初始化购买View
     */
    public void initPurchaseLayout() {
        View leftLine = purchaseLayout.findViewById(R.id.activity_read_line_left);
        TextView title = purchaseLayout.findViewById(R.id.activity_read_support);
        View rightLine = purchaseLayout.findViewById(R.id.activity_read_line_right);
        BorderTextView singlePurchase = purchaseLayout.findViewById(R.id.activity_read_purchase_one);
        BorderTextView volumePurchase = purchaseLayout.findViewById(R.id.activity_read_purchase_some);
        leftLine.setBackgroundColor(mTextColor);
        title.setTextColor(mTextColor);
        rightLine.setBackgroundColor(mTextColor);
        singlePurchase.setBorder(mTextColor, new int[]{2, 2, 2, 2}, new int[]{6, 6, 6, 6});
        volumePurchase.setBorder(mTextColor, new int[]{2, 2, 2, 2}, new int[]{6, 6, 6, 6});
        BitmapCache.getInstance().addBitmapToCache("purchase",
                ViewToBitmapUtil.getLayoutBitmap(purchaseLayout));
    }

    /**
     * 绘制购买View
     *
     * @param canvas
     */
    private void drawPurchase(Canvas canvas) {
        Bitmap purchaseBitmap = BitmapCache.getInstance().getBitmapFromCache("purchase");
        if (purchaseBitmap == null) {
            initPurchaseLayout();
            purchaseBitmap = ViewToBitmapUtil.getLayoutBitmap(purchaseLayout);
        }
        if (purchaseBitmap != null) {
            int top = mDisplayHeight - purchaseBitmap.getHeight() - ImageUtil.dp2px(mContext, 30);
            int left = ImageUtil.dp2px(mContext, 10);
            Rect purchaseSizeRect = new Rect(0, 0, purchaseBitmap.getWidth(), purchaseBitmap.getHeight());
            Rect purchaseRangeRect = new Rect(left, top, mDisplayWidth - left, top + purchaseBitmap.getHeight());
            canvas.drawBitmap(purchaseBitmap, purchaseSizeRect, purchaseRangeRect, authorPaint);
        }
    }

    /*********************************     interface     *******************************/

    public interface OnPageChangeListener {
        /**
         * 作用：章节切换的时候进行回调
         *
         * @param bookChapter:切换章节的序号
         */
        void onChapterChange(BookChapter bookChapter);

        /**
         * 作用：请求加载章节内容
         *
         * @param requestChapters:需要下载的章节列表
         */
        void requestChapters(List<BookChapter> requestChapters);

        /**
         * 作用：章节目录加载完成时候回调
         *
         * @param chapters：返回章节目录
         */
        void onCategoryFinish(List<BookChapter> chapters);

        /**
         * 作用：章节页码数量改变之后的回调。==> 字体大小的调整，或者是否关闭虚拟按钮功能都会改变页面的数量。
         *
         * @param count:页面的数量
         */
        void onPageCountChange(int count);

        /**
         * 作用：当页面改变的时候回调
         *
         * @param pos:当前的页面的序号
         */
        void onPageChange(long pos);
    }

    /**
     * 获取上下目录的信息
     *
     * @param next
     * @param last
     */
    public void lordNextData(boolean next, boolean last) {
        if (last) {
            LordNext = false;
            BookChapter bookChapterLast = ChapterManager.getInstance().getChapter(bookChapter.last_chapter);
            if (bookChapterLast != null) {
                ChapterManager.notfindChapter(bookChapterLast, new ChapterManager.ChapterDownload() {
                    @Override
                    public void finish(boolean has) {
                        if (has) {
                            mPrePageList = loadPageList(bookChapterLast.chapter_id);
                        }
                    }
                });
            }
        }
        if (next) {
            LordNext = false;
            BookChapter bookChapterNext = ChapterManager.getInstance().getChapter(bookChapter.next_chapter);
            if (bookChapterNext != null) {
                ChapterManager.notfindChapter(bookChapterNext, new ChapterManager.ChapterDownload() {
                    @Override
                    public void finish(boolean has) {
                        if (has) {
                            mNextPageList = loadPageList(bookChapterNext.chapter_id);
                        }
                    }
                });
            }
        }
    }

    public void saveCurrentChapterPos(boolean noAd) {
        int cuP = mCurPage.position;
        if (noAd) {
            int i = 0;
            for (TxtPage txtPage : mCurPageList) {
                if (i <= mCurPage.position) {
                    if (txtPage.pageStyle == 3) {
                        --cuP;
                    }
                } else {
                    break;
                }
                ++i;
            }
        }
        bookChapter.setPagePos(cuP);
        isChapterOpen = false;
        ObjectBoxUtils.addData(bookChapter, BookChapter.class);
    }
}
