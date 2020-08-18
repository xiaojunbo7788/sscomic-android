package com.ssreader.novel.ui.view.banner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.ssreader.novel.R;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.PublicIntent;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.activity.BookInfoActivity;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.banner.adapter.CBPageAdapter;
import com.ssreader.novel.ui.view.banner.holder.CBViewHolderCreator;
import com.ssreader.novel.ui.view.banner.listener.CBPageChangeListener;
import com.ssreader.novel.ui.view.banner.listener.OnItemClickListener;
import com.ssreader.novel.ui.view.banner.view.CBLoopViewPager;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;

/**
 * banner控件
 * 支持无限循环，自动翻页，翻页特效
 */
public class ConvenientBanner<T> extends LinearLayout {

    private List<T> mDatas;
    private int type = 0;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private CBPageChangeListener pageChangeListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private CBPageAdapter pageAdapter;
    private CBLoopViewPager viewPager;
    private ViewPagerScroller scroller;
    public LinearLayout loPageTurningPoint;
    private long autoTurningTime;
    private boolean turning;
    public boolean canTurn = true;
    private boolean canLoop = true;
    public int MainType;

     // 主布局
    private View bannerLayout;
    private RelativeLayout item_relativeLayout;
    // 书城、发现、会员中心
    private View item_store_entrance_comic_bgVIEW, item_store_entrance_comic_layout;
    private ImageView item_store_entrance_comic_bg, item_store_entrance_comic_img;
    // 书架banner
    private View item_shelf_banner_layout;
    private ImageView item_BookShelfBannerHolderView_img;
    private ImageView item_BookShelfBannerHolderView_audio_img;
    private TextView item_BookShelfBannerHolderView_title;
    private TextView item_BookShelfBannerHolderView_des;
    private View shelffragment_banner;

    private int S3, S8;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    private AdSwitchTask adSwitchTask;

    public ConvenientBanner(Context context) {
        this(context, null);
    }

    public ConvenientBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConvenientBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ConvenientBanner);
        canLoop = a.getBoolean(R.styleable.ConvenientBanner_canLoop, true);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        S3 = ImageUtil.dp2px(context, 2);
        S8 = ImageUtil.dp2px(context, 10);
        bannerLayout = LayoutInflater.from(context).inflate(R.layout.include_viewpager, this, true);
        item_relativeLayout = bannerLayout.findViewById(R.id.item_banner_layout);
        // 书架banner
        item_shelf_banner_layout = bannerLayout.findViewById(R.id.item_shelf_banner_layout);
        item_BookShelfBannerHolderView_img = bannerLayout.findViewById(R.id.item_BookShelfBannerHolderView_img);
        item_BookShelfBannerHolderView_audio_img = bannerLayout.findViewById(R.id.item_BookShelfBannerHolderView_audio_img);
        shelffragment_banner = bannerLayout.findViewById(R.id.shelffragment_banner);
        item_BookShelfBannerHolderView_title = bannerLayout.findViewById(R.id.item_BookShelfBannerHolderView_title);
        item_BookShelfBannerHolderView_des = bannerLayout.findViewById(R.id.item_BookShelfBannerHolderView_des);
        // 书城、发现
        item_store_entrance_comic_layout = bannerLayout.findViewById(R.id.item_store_entrance_comic_layout);
        item_store_entrance_comic_bgVIEW = bannerLayout.findViewById(R.id.item_store_entrance_comic_bgVIEW);
        item_store_entrance_comic_bg = bannerLayout.findViewById(R.id.item_store_entrance_comic_bg);
        item_store_entrance_comic_img = bannerLayout.findViewById(R.id.item_store_entrance_comic_img);
        // banner
        viewPager = bannerLayout.findViewById(R.id.cbLoopViewPager);
        loPageTurningPoint = bannerLayout.findViewById(R.id.loPageTurningPoint);
        if (adSwitchTask == null) {
            adSwitchTask = new AdSwitchTask(this);
        }
        initViewPagerScroll();
    }

    /**
     * @param holderCreator
     * @param datas
     * @return 设置viewPager
     */
    public ConvenientBanner setPages(int MainType, CBViewHolderCreator holderCreator, List<T> datas) {
        this.MainType = MainType;
        if (datas.size() > 1) {
            canTurn = true;
            this.mDatas = datas;
            pageAdapter = new CBPageAdapter(holderCreator, datas);
            item_store_entrance_comic_layout.setVisibility(GONE);
            item_shelf_banner_layout.setVisibility(View.GONE);
            if (viewPager == null) {
                viewPager = new CBLoopViewPager(getContext());
                item_relativeLayout.addView(viewPager, 0, new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            viewPager.setVisibility(VISIBLE);
            viewPager.setAdapter(pageAdapter, canLoop);
            loPageTurningPoint.setVisibility(VISIBLE);
            if (page_indicatorId != null) {
                setPageIndicator(type, page_indicatorId);
            }
        } else {
            canTurn = false;
            PublicIntent publicIntent = (PublicIntent) datas.get(0);
            stopTurning();
            if (viewPager != null) {
                item_relativeLayout.removeView(viewPager);
                viewPager = null;
            }
            loPageTurningPoint.setVisibility(GONE);
            if (MainType != 1) {
                item_shelf_banner_layout.setVisibility(View.GONE);
                item_store_entrance_comic_layout.setVisibility(VISIBLE);
            } else {
                item_store_entrance_comic_layout.setVisibility(GONE);
                item_shelf_banner_layout.setVisibility(View.VISIBLE);
            }
            setOneScroll(publicIntent, (Activity) getContext());
        }
        return this;
    }

    /**
     * 通知数据变化 如果只是增加数据建议使用 notifyDataSetAdd()
     */
    public void notifyDataSetChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
        if (page_indicatorId != null) {
            setPageIndicator(type, page_indicatorId);
        }
    }

    /**
     * 设置底部指示器是否可见
     * @param visible
     */
    public ConvenientBanner setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 底部指示器资源图片
     * @param page_indicatorId
     */
    public ConvenientBanner setPageIndicator(int type, int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.type = type;
        this.page_indicatorId = page_indicatorId;
        if (mDatas == null) {
            return this;
        }
        for (int count = 0; count < mDatas.size(); count++) {
            addConut(type, page_indicatorId);
        }
        pageChangeListener = new CBPageChangeListener(mPointViews, page_indicatorId);
        viewPager.setOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(viewPager.getRealItem());
        if (onPageChangeListener != null) {
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        }
        return this;
    }

    private void addConut(int type, int[] page_indicatorId) {
        // 翻页指示的点
        ImageView pointView = new ImageView(getContext());
        if (mPointViews.isEmpty()) {
            pointView.setBackgroundResource(page_indicatorId[1]);
        } else {
            pointView.setBackgroundResource(page_indicatorId[0]);
        }
        mPointViews.add(pointView);
        loPageTurningPoint.addView(pointView);
        // 设置图片的宽高,需要先添加到view中，才能使用getLayoutParams()，该方法的使用需要支撑
        LinearLayout.LayoutParams params = (LayoutParams) pointView.getLayoutParams();
        if (type == 0) {
            params.width = S3 * 2;
            params.height = params.width;
            params.rightMargin = S3 * 2;
        } else {
            params.rightMargin = S3 * 3;
            params.width = S8;
            params.height = S3;
        }
        pointView.setLayoutParams(params);
    }

    /**
     * @param align 三个方向：
     *              居左（RelativeLayout.ALIGN_PARENT_LEFT）
     *              居中（RelativeLayout.CENTER_HORIZONTAL）
     *              居右（RelativeLayout.ALIGN_PARENT_RIGHT）
     * @return 指示器的方向
     */
    public ConvenientBanner setPageIndicatorAlign(PageIndicatorAlign align) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        if (align == PageIndicatorAlign.ALIGN_PARENT_RIGHT) {
            // 书架
            layoutParams.rightMargin = S3 * 2;
        }
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 是否开启了翻页
     *
     * @return
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * 开始翻页
     *
     * @param autoTurningTime 自动翻页时间
     * @return
     */
    public ConvenientBanner startTurning(long autoTurningTime) {
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        this.autoTurningTime = autoTurningTime;
        turning = true;
        if (canTurn) {
            postDelayed(adSwitchTask, autoTurningTime);
        }
        return this;
    }

    public void stopTurning() {
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    /**
     * 自定义翻页动画效果
     *
     * @param transformer
     * @return
     */
    public ConvenientBanner setPageTransformer(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }

    /**
     * 设置ViewPager的滑动
     */
    private void initViewPagerScroll() {
        try {
            Field field = ViewPager.class.getDeclaredField("mTouchSlop");
            field.setAccessible(true); // 设置Java不检查权限。
            field.setInt(viewPager, 1); // 设置字段的值，此处应该使用ViewPager实例。设置只有滑动长度大于1px的时候，ViewPager才进行滑动

            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isManualPageable() {
        return viewPager.isCanScroll();
    }

    public void setManualPageable(boolean manualPageable) {
        viewPager.setCanScroll(manualPageable);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float dx = 0, xDiff = 0;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                if (canTurn) {
                    stopTurning();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                dx = x - mLastMotionX;
                xDiff = Math.abs(dx);
                if (canTurn) {
                    final float y = ev.getY();
                    final float yDiff = Math.abs(y - mLastMotionY);
                    if (xDiff > yDiff) {
                        mLastMotionX = x;
                        mLastMotionY = y;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (canTurn) {
                    startTurning(autoTurningTime);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private float mLastMotionX;
    private float mLastMotionY;

    //获取当前的页面index
    public int getCurrentItem() {
        if (viewPager != null) {
            return viewPager.getRealItem();
        }
        return -1;
    }

    //设置当前的页面index
    public void setcurrentitem(int index) {
        if (viewPager != null) {
            viewPager.setCurrentItem(index);
        }
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    /**
     * 设置翻页监听器
     *
     * @param onPageChangeListener
     * @return
     */
    public ConvenientBanner setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        //如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (pageChangeListener != null) {
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        } else {
            viewPager.setOnPageChangeListener(onPageChangeListener);
        }
        return this;
    }

    public boolean isCanLoop() {
        return viewPager.isCanLoop();
    }

    /**
     * @param
     * @return 设置一页的布局
     */
    public void setOneScroll(PublicIntent date, Activity activity) {
        if (MainType != 1) {
            int width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 20);
            if (MainType == 2) {
                int height = width * 3 / 5;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item_store_entrance_comic_img.getLayoutParams();
                layoutParams.height = height;
                item_store_entrance_comic_img.setLayoutParams(layoutParams);
                try {
                    int[] colors = {0xF2FFFFFF, Color.parseColor("#E6" + date.getColor().substring(1))};
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
                    item_store_entrance_comic_bgVIEW.setBackground(g);
                } catch (Exception e) {
                }
                MyGlide.GlideImage(activity, date.getImage(), item_store_entrance_comic_bg);
                MyGlide.GlideImageRoundedCornersNoSize(6, activity, date.getImage(), item_store_entrance_comic_img);
            } else if (MainType == 3) {
                item_store_entrance_comic_bg.setVisibility(GONE);
                item_store_entrance_comic_bgVIEW.setVisibility(GONE);
                int height = width / 4;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item_store_entrance_comic_img.getLayoutParams();
                layoutParams.height = height;
                item_store_entrance_comic_img.setLayoutParams(layoutParams);
                MyGlide.GlideImageRoundedCornersNoSize(8, activity, date.getImage(), item_store_entrance_comic_img);
            }
            item_store_entrance_comic_img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    date.intentBannerTo(activity);
                }
            });
        } else {
            shelffragment_banner.setBackground(MyShape.setMyshape(20,
                    ContextCompat.getColor(activity, R.color.graybg)));
            MyGlide.GlideImageNoSize(activity, date.image, item_BookShelfBannerHolderView_img);
            if (type == Constant.AUDIO_CONSTANT) {
                item_BookShelfBannerHolderView_audio_img.setVisibility(View.VISIBLE);
            } else {
                item_BookShelfBannerHolderView_audio_img.setVisibility(View.GONE);
            }
            item_BookShelfBannerHolderView_title.setText(date.title);
            item_BookShelfBannerHolderView_des.setText(date.desc);
            item_shelf_banner_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if (date.action == BOOK_CONSTANT) {
                        intent.setClass(activity, BookInfoActivity.class);
                        intent.putExtra("book_id", Long.parseLong(date.getContent()));
                    } else if (date.action == COMIC_CONSTANT) {
                        intent.setClass(activity, ComicInfoActivity.class);
                        intent.putExtra("comic_id", Long.parseLong(date.getContent()));
                    } else if (date.action == AUDIO_CONSTANT) {
                        intent.setClass(activity, AudioInfoActivity.class);
                        intent.putExtra("audio_id", Long.parseLong(date.getContent()));
                    }
                    activity.startActivity(intent);
                }
            });
        }
    }

    /**
     * 监听item点击
     *
     * @param onItemClickListener
     */
    public ConvenientBanner setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) {
            viewPager.setOnItemClickListener(null);
            return this;
        }
        viewPager.setOnItemClickListener(onItemClickListener);
        return this;
    }

    /**
     * 设置ViewPager的滚动速度
     *
     * @param scrollDuration
     */
    public void setScrollDuration(int scrollDuration) {
        scroller.setScrollDuration(scrollDuration);
    }

    public int getScrollDuration() {
        return scroller.getScrollDuration();
    }

    public CBLoopViewPager getViewPager() {
        return viewPager;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        viewPager.setCanLoop(canLoop);
    }

    /**
     * 设置banner
     *
     * @param activity
     * @param
     * @param mBannerItemListMale
     * @param mStoreBannerMale
     * @param flag
     */
    public static void initBanner(Activity activity, int MainType, List<PublicIntent> mBannerItemListMale,
                                  ConvenientBanner<PublicIntent> mStoreBannerMale, int flag) {
        if (!mBannerItemListMale.isEmpty()) {
            if (mBannerItemListMale.size() > 1) {
                CBViewHolderCreator creator = new CBViewHolderCreator() {
                    @Override
                    public Object createHolder() {
                        if (MainType == 2) {
                            return new HomeBannerHolderViewComic();
                        } else {
                            return new DiscoverBannerHolderViewBook();
                        }
                    }
                };
                ConvenientBanner convenientBanner = mStoreBannerMale.setPages(MainType, creator, mBannerItemListMale);
                convenientBanner.setPageIndicator(1, new int[]{R.mipmap.banner_indicator, R.mipmap.banner_indicator_focused})
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Onclick(mBannerItemListMale.get(position), activity, flag);
                            }
                        });

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mStoreBannerMale.loPageTurningPoint.getLayoutParams();
                if (MainType == 3) {
                    layoutParams.bottomMargin = ImageUtil.dp2px(activity, 4);
                } else {
                    layoutParams.bottomMargin = ImageUtil.dp2px(activity, 12);
                }
                mStoreBannerMale.loPageTurningPoint.setLayoutParams(layoutParams);
                mStoreBannerMale.startTurning(5000);
            } else {
                if (mBannerItemListMale.get(0) != null) {
                    mBannerItemListMale.get(0).actionBanner = flag;
                }
                mStoreBannerMale.setPages(MainType, null, mBannerItemListMale);
            }
        } else {
            mStoreBannerMale.setVisibility(View.GONE);
        }
    }

    public static void Onclick(PublicIntent bannerItemStore, Activity activity, int flag) {
        try {
            bannerItemStore.actionBanner = flag;
            bannerItemStore.intentBannerTo(activity);
        } catch (Exception E) {
        }
    }

    public static class AdSwitchTask implements Runnable {

        private final WeakReference<ConvenientBanner> reference;

        AdSwitchTask(ConvenientBanner convenientBanner) {
            this.reference = new WeakReference<ConvenientBanner>(convenientBanner);
        }

        @Override
        public void run() {
            ConvenientBanner convenientBanner = reference.get();
            if (convenientBanner != null) {
                if (convenientBanner.viewPager != null && convenientBanner.turning) {
                    int page = convenientBanner.viewPager.getCurrentItem() + 1;
                    convenientBanner.viewPager.setCurrentItem(page);
                    convenientBanner.postDelayed(convenientBanner.adSwitchTask, convenientBanner.autoTurningTime);
                }
            }
        }
    }
}
