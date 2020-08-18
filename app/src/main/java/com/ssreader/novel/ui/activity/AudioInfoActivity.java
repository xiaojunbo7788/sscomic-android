package com.ssreader.novel.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshAudioShelf;
import com.ssreader.novel.eventbus.RefreshBookInfo;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.MyFragmentPagerAdapter;
import com.ssreader.novel.ui.audio.fragment.AudioInfoCatalogFragment;
import com.ssreader.novel.ui.audio.fragment.AudioInfoRecommendFragment;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.AutoTextView;
import com.ssreader.novel.ui.view.RoundImageView;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.ui.utils.StatusBarUtil.setStatusTextColor;

/**
 * 有声详情界面
 */
public class AudioInfoActivity extends BaseActivity {

    @BindView(R.id.sound_coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.sound_dec_open_layout)
    LinearLayout openDesLayout;
    @BindView(R.id.sound_appBar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.sound_toolbar_layout)
    Toolbar toolbar;
    @BindView(R.id.audio_background_layout)
    View backgroundView;
    @BindView(R.id.sound_appbar_content_bg)
    FrameLayout appBarBackground;
    @BindViews({R.id.sound_appBar_tabLayout, R.id.sound_appBar_vip_layout})
    List<LinearLayout> appBarBackgroundLayout;

    @BindView(R.id.sound_toolbar_right_layout)
    LinearLayout toolBarRightLayout;
    @BindViews({R.id.sound_toolbar_add_shelf_layout, R.id.sound_add_shelf_layout})
    List<LinearLayout> addShelfLayout;
    @BindViews({R.id.sound_toolbar_title, R.id.sound_toolbar_add_shelf_tv, R.id.sound_add_shelf_tv})
    List<TextView> toolBarText;
    @BindViews({R.id.sound_toolbar_add_shelf_iv, R.id.sound_add_shelf_iv})
    List<ImageView> toolBarImage;
    @BindViews({R.id.sound_toolbar_down, R.id.sound_toolbar_share})
    List<RelativeLayout> toolBarLayout;

    @BindView(R.id.sound_appBar_cover_iv)
    RoundImageView bookCover;
    @BindView(R.id.sound_appBar_cover_layout)
    View sound_appBar_cover_layout;

    @BindView(R.id.sound_appBar_status_v)
    View sound_appBar_status_v;

    @BindView(R.id.sound_appBar_content_layout)
    LinearLayout appBarContentLayout;
    @BindViews({R.id.sound_appBar_name, R.id.sound_appBar_sort, R.id.sound_appBar_status, R.id.sound_appBar_listener_num,
            R.id.sound_appBar_collection_num, R.id.sound_appBar_author})
    List<TextView> appBarText;

    @BindViews({R.id.sound_toolBar_dec_layout, R.id.sound_toolBar_close_bg_layout})
    List<LinearLayout> audioDesBgLayout;
    @BindViews({R.id.sound_toolBar_dec_open_layout, R.id.sound_toolBar_dec_close_layout, R.id.sound_toolBar_close_layout})
    List<LinearLayout> desLayouts;
    @BindViews({R.id.sound_toolBar_dec, R.id.sound_open_des})
    List<TextView> audioDec;
    @BindView(R.id.sound_open_des_image)
    ImageView audioDecImage;

    @BindView(R.id.list_ad_view_layout)
    FrameLayout list_ad_view_layout;

    @BindView(R.id.sound_appBar_tab)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.sound_viewPager)
    ViewPager viewPager;

    // id
    private long audioId;
    // 用户是否是Vip
    private int isVip;
    // audio
    private Audio mAudio;
    private AudioBean.Audio audio;

    // fragment
    private Fragment fragment1, fragment2;
    private List<Fragment> fragmentList;
    private List<TextView> textViewList;
    private List<String> tabList;

    private long ClickTime;

    @Override
    public int initContentView() {
        FULL_CCREEN = true;
        USE_EventBus = true;
        mAudio = (Audio) formIntent.getSerializableExtra("audio");
        if (mAudio == null) {
            audioId = formIntent.getLongExtra("audio_id", 0);
            mAudio = ObjectBoxUtils.getAudio(audioId);
            if (mAudio == null) {
                mAudio = new Audio();
                mAudio.audio_id = audioId;
            }
        } else {
            audioId = mAudio.audio_id;
        }
        return R.layout.activity_audio_info;
    }

    @Override
    public void initView() {
        float w = (float) (ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 30)) / 3;
        ViewGroup.LayoutParams layoutParams = sound_appBar_cover_layout.getLayoutParams();
        layoutParams.height = (int) (w * 4 / 3);
        sound_appBar_cover_layout.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams layoutParamsbookCover = bookCover.getLayoutParams();
        layoutParamsbookCover.width = (int) w;
        bookCover.setLayoutParams(layoutParamsbookCover);
        fragmentList = new ArrayList<>();
        tabList = new ArrayList<>();
        textViewList = new ArrayList<>();
        int statusHeight = ImmersionBar.getStatusBarHeight(this);
        // 设置toolbar
        CollapsingToolbarLayout.LayoutParams toolbarParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        toolbarParams.topMargin = statusHeight;
        toolbar.setLayoutParams(toolbarParams);
        CollapsingToolbarLayout.LayoutParams bgParams = (CollapsingToolbarLayout.LayoutParams) appBarBackground.getLayoutParams();
        bgParams.topMargin = statusHeight + ImageUtil.dp2px(this, 55);
        appBarBackground.setLayoutParams(bgParams);
        setToolbarLayout(false);
        setAddShelfLayout((mAudio != null && mAudio.is_collect == 1));
        if (Constant.USE_SHARE) {
            toolBarLayout.get(1).setVisibility(View.VISIBLE);
        } else {
            toolBarLayout.get(1).setVisibility(View.GONE);
        }
        // 设置简介
        audioDesBgLayout.get(0).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 5),
                ContextCompat.getColor(this, R.color.black_alpha_20)));
        audioDesBgLayout.get(1).setBackground(MyShape.setGradient(ContextCompat.getColor(this, R.color.black_alpha_2),
                ContextCompat.getColor(this, R.color.black_alpha_20), 0, 270));
        desLayouts.get(0).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 15),
                ContextCompat.getColor(this, R.color.white_alpha_20)));
        desLayouts.get(1).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 15),
                ContextCompat.getColor(this, R.color.white_alpha_20)));
        desLayouts.get(2).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 40),
                ContextCompat.getColor(this, R.color.black_alpha_30)));
        setOpenDes(false);
        // 设置tab
        appBarBackgroundLayout.get(0).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 10), ImageUtil.dp2px(this, 10),
                0, 0, ContextCompat.getColor(this, R.color.white)));
        // 设置开通会员按钮
        ViewGroup.LayoutParams openVipParams = appBarBackgroundLayout.get(1).getLayoutParams();
        openVipParams.height = (ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 30)) * 90 / 800;
        appBarBackgroundLayout.get(1).setLayoutParams(openVipParams);
        // 设置viewPager
        tabList.add(LanguageUtil.getString(activity, R.string.BookInfoActivity_mulu));
        tabList.add(LanguageUtil.getString(activity, R.string.audio_info_recommend));
        fragment1 = new AudioInfoCatalogFragment(audioId, mAudio.getCurrent_listen_chapter_id() != 0 ? mAudio.getCurrent_listen_chapter_id() : -1);
        fragment2 = new AudioInfoRecommendFragment(audioId);
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabList));
        smartTabLayout.setViewPager(viewPager);
        textViewList.add(smartTabLayout.getTabAt(0).findViewById(R.id.item_tablayout_text));
        textViewList.add(smartTabLayout.getTabAt(1).findViewById(R.id.item_tablayout_text));
        textViewList.get(0).setTextColor(ContextCompat.getColor(activity, R.color.gray_title));
        textViewList.get(1).setTextColor(ContextCompat.getColor(activity, R.color.gray_title));
        textViewList.get(0).setTextSize(16);
        textViewList.get(1).setTextSize(16);
        viewPager.setCurrentItem(0);
        initListener();
        setNoNetLayout();
    }

    @OnClick({R.id.sound_toolbar_back, R.id.sound_toolbar_down, R.id.sound_toolbar_share,
            R.id.sound_toolBar_dec_open_layout, R.id.sound_toolBar_dec_close_layout, R.id.sound_toolBar_close_layout,
            R.id.sound_toolbar_add_shelf_layout, R.id.sound_add_shelf_layout, R.id.sound_appBar_vip_layout})
    public void onAudioInfoClick(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.sound_appBar_vip_layout:
                    startActivity(new Intent(activity, NewRechargeActivity.class)
                            .putExtra("RechargeTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_title))
                            .putExtra("RechargeType", "vip"));
                    break;
                case R.id.sound_toolbar_back:
                    if ( !BWNApplication.applicationContext.isMainActivityStartUp()) {
                        startActivity(new Intent(activity, MainActivity.class));
                    }
                    finish();
                    break;
                case R.id.sound_toolbar_down:
                    // 下载
                    ((AudioInfoCatalogFragment) fragment1).setDown();
                    break;
                case R.id.sound_toolbar_share:
                    // 分享
                    if (mAudio != null) {
                        new MyShare(activity)
                                .setFlag(AUDIO_CONSTANT)
                                .setId(mAudio.getAudio_id())
                                .Share();
                    }
                    break;
                case R.id.sound_toolbar_add_shelf_layout:
                case R.id.sound_add_shelf_layout:
                    // 收藏
                    if (mAudio != null && mAudio.getIs_collect() == 0) {
                        setAddShelfLayout(true);
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
                    }
                    break;
                case R.id.sound_toolBar_dec_open_layout:
                    //  设置是否展开简介
                    setOpenDes(true);
                    break;
                case R.id.sound_toolBar_dec_close_layout:
                case R.id.sound_toolBar_close_layout:
                    //  设置是否展开简介
                    setOpenDes(false);
                    break;
            }
        }
    }

    private void initListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float offset = Math.abs(verticalOffset);
                int scrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset > -10) {
                    setToolbarLayout(false);
                } else if (verticalOffset < (scrollRange * -1) + 10) {
                    setToolbarLayout(true);
                }
                // 设置透明度
                if (offset <= scrollRange * 5 / 6) {
                    float scale = offset / (scrollRange * 5 / 6);
                    float alpha = 1 - (1 * scale);
                    appBarBackground.setAlpha(alpha);
                } else if (offset > scrollRange * 5 / 6) {
                    appBarBackground.setAlpha(0.0f);
                }
            }
        });
    }

    @Override
    public void initData() {
        if (audioId != 0) {
            ReaderParams readerParams = new ReaderParams(activity);
            readerParams.putExtraParams("audio_id", audioId);
            HttpUtils.getInstance().sendRequestRequestParams(activity, Api.AUDIO_INFO, readerParams.generateParamsJson(), responseListener);
        }
    }

    @Override
    public void initInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            AudioBean audioBean = HttpUtils.getGson().fromJson(json, AudioBean.class);
            if (audioBean.getAudio() != null) {
                audio = audioBean.getAudio();
                // 更新本地数据
                mAudio.display_label = audio.getDisplay_label();
                mAudio.finished = audio.getFinished();
//                if (audio.author != null && !audio.author.isEmpty() && !TextUtils.isEmpty(audio.author.get(0))) {
//                    StringBuilder name = new StringBuilder();
//                    for (int i = 0; i < audio.author.size(); i++) {
//                        if (i == 0) {
//                            name.append(audio.author.get(i));
//                        } else {
//                            name.append(" ").append(audio.author.get(i));
//                        }
//                    }
//                    mAudio.author = audio.author;
//                }
                mAudio.author = audio.author;
                mAudio.cover = audio.getCover();
                mAudio.name = audio.getName();
                mAudio.hot_num = audio.getHot_num();
                mAudio.description = audio.getDescription();
                if (mAudio.is_collect == 1) {
                    ObjectBoxUtils.addData(mAudio, Audio.class);
                }
                mAudio.total_chapter = audio.getTotal_chapter();
                // 是否显示会员畅读提醒
                if (audio.getIs_baoyue() == 0) {
                    setOpenVipLayout(false);
                } else {
                    if (audioBean.getUser() != null) {
                        isVip = audioBean.getUser().getIs_vip();
                        setOpenVipLayout(isVip == 0);
                    }
                }
                setAudioInPage(audio);
            }
            if (http_flag == 0) {
                if (audioBean.getAdvert() != null && audioBean.getAdvert().ad_type != 0) {
                    list_ad_view_layout.setVisibility(View.VISIBLE);
                    list_ad_view_layout.setBackgroundColor(ContextCompat.getColor(activity, R.color.transparent));
                    audioBean.getAdvert().setAd(activity, list_ad_view_layout, 2);
                }
                ((AudioInfoCatalogFragment) fragment1).setAudio();
            }
        }
        http_flag = 1;
    }

    private void setNoNetLayout() {
        if (!InternetUtils.internet(activity)) {
            MyToash.ToashError(activity, R.string.splashactivity_nonet);
            toolBarRightLayout.setVisibility(View.GONE);
            appBarContentLayout.setVisibility(View.GONE);
            audioDesBgLayout.get(0).setVisibility(View.GONE);
            appBarBackgroundLayout.get(0).setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
        }
    }

    /**
     * 设置界面
     *
     * @param audio
     */
    @SuppressLint("Range")
    private void setAudioInPage(AudioBean.Audio audio) {
        if (http_flag == 0) {
            if (!audio.getColor().isEmpty()) {
                String color1 = audio.getColor().get(0).isEmpty() ? "#a6ada6" : audio.getColor().get(0);
                String color2 = "";
                if (audio.getColor().size() > 1) {
                    color2 = audio.getColor().get(1).isEmpty() ? "#50554f" : audio.getColor().get(1);
                } else {
                    color2 = "#50554f";
                }
                // 设置背景
                backgroundView.setBackground(MyShape.setGradient(Color.parseColor(color1),
                        Color.parseColor(color2), 0, 315));
            }
            toolBarText.get(0).setText(audio.getName());
            MyGlide.GlideImageNoSize(activity, audio.getCover(), bookCover);
            appBarText.get(0).setText(!TextUtils.isEmpty(audio.getName()) ? audio.getName() : "- -");
            if (audio.getTag() != null && !audio.getTag().isEmpty()) {
                appBarText.get(1).setText(!TextUtils.isEmpty(audio.getTag().get(0).getTab()) ? audio.getTag().get(0).getTab() : "- -");
                if (audio.getTag().size() > 1) {
                    appBarText.get(2).setText(!TextUtils.isEmpty(audio.getTag().get(1).getTab()) ? audio.getTag().get(1).getTab() : "- -");
                } else {
                    sound_appBar_status_v.setVisibility(View.GONE);
                }
            }
        }
        appBarText.get(3).setText(!TextUtils.isEmpty(audio.getHot_num()) ? audio.getHot_num() : "- -");
        appBarText.get(4).setText(!TextUtils.isEmpty(audio.getTotal_favors()) ? audio.getTotal_favors() : "- -");
//        if (audio.getAuthor() != null && !audio.getAuthor().isEmpty() && !TextUtils.isEmpty(audio.getAuthor().get(0))) {
//            StringBuilder name = new StringBuilder();
//            for (int i = 0; i < audio.getAuthor().size(); i++) {
//                if (i == 0) {
//                    name.append(audio.getAuthor().get(i));
//                } else {
//                    name.append(" ").append(audio.getAuthor().get(i));
//                }
//            }
//            appBarText.get(5).setText(name);
//        } else {
//            appBarText.get(5).setText("- -");
//        }
        appBarText.get(5).setText(!TextUtils.isEmpty(audio.getAuthor()) ? audio.getAuthor() : "- -");
        // 动态设置展开简介
        if (http_flag == 0) {
            if (audio.getDescription() != null && !TextUtils.isEmpty(audio.getDescription())) {
                audioDesBgLayout.get(0).setVisibility(View.VISIBLE);
                // 获取控件的宽度
                int width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 60);
                int openWidth = ImageUtil.dp2px(activity, 70);
                // 简介
                ((AutoTextView) audioDec.get(0)).setAutoText(audio.getDescription(), new AutoTextView.SetAutoTextOver() {
                    @Override
                    public void setAutoTextOver() {
                        Layout textLayout = audioDec.get(0).getLayout();
                        if (textLayout != null) {
                            int start = textLayout.getLineStart(0);
                            int end = textLayout.getLineEnd(textLayout.getLineCount() - 1);
                            // 获取真实内容文字的最后一个字符的位置
                            int offset = audioDec.get(0).getText().toString().substring(start, end).length() - 1;
                            Rect bound = new Rect();
                            // 获取bound的区域
                            textLayout.getLineBounds(textLayout.getLineForOffset(offset), bound);
                            int textRight = (int) textLayout.getSecondaryHorizontal(offset) + ImageUtil.dp2px(activity, 10);
                            RelativeLayout.LayoutParams copyParams = (RelativeLayout.LayoutParams) desLayouts.get(0).getLayoutParams();
                            if (width < openWidth + textRight) {
                                // 长度不足时
                                copyParams.topMargin = bound.bottom;
                            } else {
                                copyParams.topMargin = bound.bottom - ImageUtil.dp2px(activity, 16);
                            }
                            desLayouts.get(0).setLayoutParams(copyParams);
                            audioDec.get(1).setText(audio.getDescription());
                        }
                    }
                });
            } else {
                audioDesBgLayout.get(0).setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(audio.getHorizontal_cover())) {
                setDesImage(audio.getHorizontal_cover());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStatusTextColor(false, this);
    }

    /**
     * toolbar是否显示标题
     *
     * @param isShow
     */
    private void setToolbarLayout(boolean isShow) {
        if (isShow) {
            addShelfLayout.get(0).setVisibility(View.VISIBLE);
            toolBarText.get(0).setVisibility(View.VISIBLE);
        } else {
            addShelfLayout.get(0).setVisibility(View.GONE);
            toolBarText.get(0).setVisibility(View.GONE);
        }
    }

    /**
     * 设置收藏按钮
     *
     * @param isAdd
     */
    private void setAddShelfLayout(boolean isAdd) {
        if (http_flag == 1) {
            initData();
        }
        if (!isAdd) {
            toolBarImage.get(0).setVisibility(View.VISIBLE);
            toolBarImage.get(1).setVisibility(View.VISIBLE);
            toolBarText.get(1).setText(LanguageUtil.getString(this, R.string.audio_collection));
            toolBarText.get(2).setText(LanguageUtil.getString(this, R.string.audio_collection));
            addShelfLayout.get(0).setBackground(MyShape.setGradient(Color.parseColor("#FFB084"),
                    ContextCompat.getColor(this, R.color.add_shelf_bg),
                    ImageUtil.dp2px(this, 20), 270));
            addShelfLayout.get(1).setBackground(MyShape.setGradient(Color.parseColor("#FFB084"),
                    ContextCompat.getColor(this, R.color.add_shelf_bg),
                    ImageUtil.dp2px(this, 20), 270));
        } else {
            mAudio.setIs_collect(1);
            ObjectBoxUtils.addData(mAudio, Audio.class);
            EventBus.getDefault().post(new RefreshShelf(AUDIO_CONSTANT, new RefreshAudioShelf(mAudio, 1)));
            toolBarImage.get(0).setVisibility(View.GONE);
            toolBarImage.get(1).setVisibility(View.GONE);
            toolBarText.get(1).setText(LanguageUtil.getString(this, R.string.BookInfoActivity_collection));
            toolBarText.get(2).setText(LanguageUtil.getString(this, R.string.BookInfoActivity_collection));
            addShelfLayout.get(0).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 20),
                    ContextCompat.getColor(this, R.color.black_alpha_20)));
            addShelfLayout.get(1).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 20),
                    ContextCompat.getColor(this, R.color.black_alpha_20)));
            addShelfLayout.get(0).setEnabled(false);
            addShelfLayout.get(1).setEnabled(false);
        }
    }

    /**
     * 展开（关闭）简介
     *
     * @param isOpen
     */
    private void setOpenDes(boolean isOpen) {
        if (isOpen) {
            // 展开
            audioDesBgLayout.get(0).setVisibility(View.GONE);
            appBarBackgroundLayout.get(0).setVisibility(View.GONE);
            appBarBackgroundLayout.get(1).setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            ViewGroup.LayoutParams coordinatorParams = coordinatorLayout.getLayoutParams();
            coordinatorParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            coordinatorLayout.setLayoutParams(coordinatorParams);
            openDesLayout.setVisibility(View.VISIBLE);
            audioDecImage.setVisibility(View.VISIBLE);
            // 禁止滑动coordinatorLayout
            setAppBarScroll(false);
        } else {
            // 收起
            openDesLayout.setVisibility(View.GONE);
            audioDecImage.setVisibility(View.GONE);
            ViewGroup.LayoutParams coordinatorParams = coordinatorLayout.getLayoutParams();
            coordinatorParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            coordinatorLayout.setLayoutParams(coordinatorParams);
            audioDesBgLayout.get(0).setVisibility(View.VISIBLE);
            appBarBackgroundLayout.get(0).setVisibility(View.VISIBLE);
            if (audio != null) {
                // 是否显示会员畅读提醒
                if (audio.getIs_baoyue() == 0) {
                    setOpenVipLayout(false);
                } else {
                    setOpenVipLayout(isVip == 0);
                }
            }

            viewPager.setVisibility(View.VISIBLE);
            // 允许滑动coordinatorLayout
            setAppBarScroll(true);
        }
    }

    /**
     * 加载图片
     * @param url
     */
    private void setDesImage(String url) {
        Glide.with(activity).asBitmap().load(url).into(new ImageViewTarget<Bitmap>(audioDecImage) {
            @Override
            protected void setResource(@Nullable Bitmap resource) {
                if (resource == null) {
                    return;
                }
                audioDecImage.setImageBitmap(resource);
                //获取原图的宽高
                int width = resource.getWidth();
                int height = resource.getHeight();
                // 获取imageView的宽
                int imageViewWidth = audioDecImage.getWidth();
                //计算缩放比例
                float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);
                //计算图片等比例放大后的高
                int imageViewHeight = (int) (height * sy);
                ViewGroup.LayoutParams params = audioDecImage.getLayoutParams();
                params.height = imageViewHeight;
                audioDecImage.setLayoutParams(params);
            }
        });
    }

    /**
     * 会员畅听是否可以显示
     * @param isShow
     */
    private void setOpenVipLayout(boolean isShow) {
        if (isShow) {
            appBarBackgroundLayout.get(1).setVisibility(View.VISIBLE);
        } else {
            appBarBackgroundLayout.get(1).setVisibility(View.GONE);
        }
    }

    /**
     * 设置是否可以滑动
     *
     * @param isScroll
     */
    private void setAppBarScroll(boolean isScroll) {
        View mAppBarChildAt = appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams mAppBarParams = (AppBarLayout.LayoutParams) mAppBarChildAt.getLayoutParams();
        if (isScroll) {
            mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
            mAppBarChildAt.setLayoutParams(mAppBarParams);
        } else {
            mAppBarParams.setScrollFlags(0);
        }
        mAppBarChildAt.setLayoutParams(mAppBarParams);
    }

    /**
     * 获取数据
     *
     * @return
     */
    public Audio getAudio() {
        return mAudio;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBookSelf(RefreshShelfCurrent s) {
        if (mAudio != null && mAudio.getName() != null) {
            if (s.productType == AUDIO_CONSTANT) {
                if (s.audio != null && mAudio.audio_id == s.audio.audio_id) {
                    mAudio = s.audio;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMine(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity) && refreshMine.type != 1) {
            initData();
        }
    }

    /**
     * 刷新书架
     * @param refreshBookInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshBookInfo refreshBookInfo) {
        if (refreshBookInfo.audio == null) {
            return;
        }
        if (audio != null && audio.getName() != null) {
            if (refreshBookInfo.audio.audio_id == audioId) {
                if (refreshBookInfo.isSave) {
                    setAddShelfLayout(true);
                } else {
                    setAddShelfLayout(false);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ( !BWNApplication.applicationContext.isMainActivityStartUp()) {
                startActivity(new Intent(activity, MainActivity.class));
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
