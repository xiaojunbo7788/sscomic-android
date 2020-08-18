package com.ssreader.novel.ui.read.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.read.manager.ReadSettingManager;
import com.ssreader.novel.ui.read.animation.HorizonPageAnim;
import com.ssreader.novel.ui.read.page.PageLoader;
import com.ssreader.novel.ui.read.page.PageMode;
import com.ssreader.novel.ui.read.page.PageStyle;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.ShareUitls;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.ui.read.manager.ReadSettingManager.LINE_SPACING_BIG;
import static com.ssreader.novel.ui.read.manager.ReadSettingManager.LINE_SPACING_MEDIUM;
import static com.ssreader.novel.ui.read.manager.ReadSettingManager.LINE_SPACING_SMALL;
import static com.ssreader.novel.ui.read.page.PageMode.*;
import static com.ssreader.novel.ui.read.page.PageStyle.*;

public class SettingDialog extends Dialog {

    @BindView(R.id.tv_subtract)
    View tv_subtract;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_add)
    View tv_add;
    @BindView(R.id.iv_bg_default)
    View iv_bg_default;
    @BindView(R.id.iv_bg_1)
    View iv_bg1;
    @BindView(R.id.iv_bg_2)
    View iv_bg2;
    @BindView(R.id.iv_bg_3)
    View iv_bg3;
    @BindView(R.id.iv_bg_4)
    View iv_bg4;
    @BindView(R.id.iv_bg_7)
    View iv_bg7;
    @BindView(R.id.iv_bg_8)
    View iv_bg8;

    @BindView(R.id.iv_bg_default_select)
    View iv_bg_default_select;
    @BindView(R.id.iv_bg_1_select)
    View iv_bg1_select;
    @BindView(R.id.iv_bg_2_select)
    View iv_bg2_select;
    @BindView(R.id.iv_bg_3_select)
    View iv_bg3_select;
    @BindView(R.id.iv_bg_4_select)
    View iv_bg4_select;
    @BindView(R.id.iv_bg_7_select)
    View iv_bg7_select;
    @BindView(R.id.iv_bg_8_select)
    View iv_bg8_select;

    @BindView(R.id.tv_simulation)
    TextView tv_simulation;
    @BindView(R.id.tv_cover)
    TextView tv_cover;
    @BindView(R.id.tv_slide)
    TextView tv_slide;
    @BindView(R.id.tv_none)
    TextView tv_none;
    @BindView(R.id.tv_shangxia)
    TextView tv_shangxia;
    @BindView(R.id.margin_big)
    View margin_big;
    @BindView(R.id.margin_medium)
    View margin_medium;
    @BindView(R.id.margin_small)
    View margin_small;
    @BindView(R.id.margin_big_tv)
    View margin_big_tv;
    @BindView(R.id.margin_medium_tv)
    View margin_medium_tv;
    @BindView(R.id.margin_small_tv)
    View margin_small_tv;
    @BindView(R.id.auto_read_layout)
    View auto_read_layout;
    @BindView(R.id.dialog_read_setting_layout)
    View dialog_read_setting_layout;
    @BindView(R.id.tv_jianfan)
    TextView tv_jianfan;

    private long ClickTime, AgainTime = 500;
    private boolean fanti;
    private Book book;
    private PageStyle pageStyle;
    private float MarginMode;
    private ReadActivity mContext;
    private ReadSettingManager config;
    private SettingListener mSettingListener;
    private int FONT_SIZE_MIN;
    private int FONT_SIZE_MAX;
    private int currentFontSize;
    private ProgressBar auto_read_progress_bar;
    private PageLoader mPageLoader;
    private PageMode pageMode;

    public SettingDialog(Context context, Book book) {
        this(context, R.style.setting_dialog);
        mContext = (ReadActivity) context;
        this.book = book;
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @SuppressLint("WrongConstant")
    private void setBG(int i) {
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(10f);
        gd.setGradientType(GradientDrawable.RECTANGLE);
        switch (i) {
            case 0:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_default));
                iv_bg_default.setBackground(gd);
                break;
            case 1:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_1));
                iv_bg1.setBackground(gd);
                break;
            case 2:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_2));
                iv_bg2.setBackground(gd);
                break;
            case 3:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_3));
                iv_bg3.setBackground(gd);
                break;
            case 4:
                gd.setStroke(2, mContext.getResources().getColor(R.color.grayline));//描边的颜色和宽度
                gd.setColor(Color.WHITE);
                iv_bg4.setBackground(gd);
                break;
            case 7:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_7));
                iv_bg7.setBackground(gd);
                break;
            case 8:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_8));
                iv_bg8.setBackground(gd);
                break;
        }
    }

    private void setPageStyleUi(PageStyle i, boolean flag) {
        switch (i) {
            case BG_0:
                if (flag) {
                    iv_bg_default_select.setVisibility(View.VISIBLE);
                } else {
                    iv_bg_default_select.setVisibility(View.GONE);
                }
                break;
            case BG_1:
                if (flag) {
                    iv_bg1_select.setVisibility(View.VISIBLE);
                } else {
                    iv_bg1_select.setVisibility(View.GONE);
                }
                break;
            case BG_2:
                if (flag) {
                    iv_bg2_select.setVisibility(View.VISIBLE);
                } else {
                    iv_bg2_select.setVisibility(View.GONE);
                }
                break;
            case BG_3:
                if (flag) {
                    iv_bg3_select.setVisibility(View.VISIBLE);
                } else {
                    iv_bg3_select.setVisibility(View.GONE);
                }
                break;
            case BG_4:
                if (flag) {
                    iv_bg4_select.setVisibility(View.VISIBLE);
                } else {
                    iv_bg4_select.setVisibility(View.GONE);
                }
                break;
            case BG_7:
                if (flag) {
                    iv_bg7_select.setVisibility(View.VISIBLE);
                } else {
                    iv_bg7_select.setVisibility(View.GONE);
                }
                break;
            case BG_8:
                if (flag) {
                    iv_bg8_select.setVisibility(View.VISIBLE);
                } else {
                    iv_bg8_select.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_setting);
        initview();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(R.dimen.reading_min_text_size);
        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(R.dimen.reading_max_text_size);

        config = ReadSettingManager.getInstance();
        currentFontSize = config.getTextSize();
        tv_size.setText(currentFontSize + "");

        pageStyle = config.getPageStyle();
        setPageStyleUi(pageStyle, true);
        selectPageMode(pageMode = config.getPageMode());
        selectLineSpacing(MarginMode = config.getLineSpacingMode());
        mPageLoader = mContext.getBookPage().mPageLoader;
    }

    private void initview() {
        ButterKnife.bind(this);
        dialog_read_setting_layout.setBackground(MyShape.setMyshapeStroke(mContext, 8, 8, 0, 0, 1, ContextCompat.getColor(mContext, R.color.graybg), Color.WHITE));
        if (book != null && book.language != null && (book.language.equals("zh") || book.language.equals("tw"))) {
            fanti = ShareUitls.getBoolean(mContext, "fanti", book.language.equals("tw"));
            if (fanti) {
                tv_jianfan.setTextColor(Color.parseColor("#3ba166"));
                tv_jianfan.setBackground(MyShape.setMyshapeStroke(getContext(), 2, 1, Color.parseColor("#3ba166")));
            } else {
                tv_jianfan.setTextColor(Color.BLACK);
                tv_jianfan.setBackground(MyShape.setMyshape(ImageUtil.dp2px(getContext(), 2), Color.parseColor("#ededed")));
            }
        } else {
            tv_jianfan.setVisibility(View.GONE);
        }
        // 初始化View注入
        for (int i = 0; i <= 8; i++) {
            if (i != 5 && i != 6) {
                setBG(i);
            }
        }
    }

    /**
     * 选择翻页
     *
     * @param pageMode
     */
    private void selectPageMode(PageMode pageMode) {
        if (pageMode == SIMULATION) {
            setTextViewSelect(tv_simulation, true);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
            setTextViewSelect(tv_shangxia, false);
        } else if (pageMode == COVER) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, true);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
            setTextViewSelect(tv_shangxia, false);
        } else if (pageMode == SLIDE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, true);
            setTextViewSelect(tv_none, false);
            setTextViewSelect(tv_shangxia, false);
        } else if (pageMode == SCROLL) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_shangxia, true);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == NONE) {
            setTextViewSelect(tv_shangxia, false);
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, true);
        }
    }

    /**
     * 设置按钮选择的背景
     *
     * @param view
     * @param tv
     * @param isSelect
     * @param selectedRes
     * @param unSelectedRes
     */
    private void setLineSpacingViewSelect(View view, View tv, Boolean isSelect, int selectedRes, int unSelectedRes) {
        if (isSelect) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_pressed));
            tv.setBackgroundResource(selectedRes);
        } else {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_unpressed));
            tv.setBackgroundResource(unSelectedRes);
        }
    }

    /**
     * 选择间距
     *
     * @param marginMode
     */
    private void selectLineSpacing(float marginMode) {
        MarginMode = marginMode;
        if (marginMode == LINE_SPACING_SMALL) {//LINE_SPACING_SMALL
            setLineSpacingViewSelect(margin_big, margin_big_tv, true, R.mipmap.line_spacing_big_select, R.mipmap.line_spacing_big);
            setLineSpacingViewSelect(margin_medium, margin_medium_tv, false, R.mipmap.line_spacing_medium_select, R.mipmap.line_spacing_medium);
            setLineSpacingViewSelect(margin_small, margin_small_tv, false, R.mipmap.line_spacing_small_select, R.mipmap.line_spacing_small);
        } else if (marginMode == LINE_SPACING_MEDIUM) {
            setLineSpacingViewSelect(margin_big, margin_big_tv, false, R.mipmap.line_spacing_big_select, R.mipmap.line_spacing_big);
            setLineSpacingViewSelect(margin_medium, margin_medium_tv, true, R.mipmap.line_spacing_medium_select, R.mipmap.line_spacing_medium);
            setLineSpacingViewSelect(margin_small, margin_small_tv, false, R.mipmap.line_spacing_small_select, R.mipmap.line_spacing_small);
        } else if (marginMode == LINE_SPACING_BIG) {
            setLineSpacingViewSelect(margin_big, margin_big_tv, false, R.mipmap.line_spacing_big_select, R.mipmap.line_spacing_big);
            setLineSpacingViewSelect(margin_medium, margin_medium_tv, false, R.mipmap.line_spacing_medium_select, R.mipmap.line_spacing_medium);
            setLineSpacingViewSelect(margin_small, margin_small_tv, true, R.mipmap.line_spacing_small_select, R.mipmap.line_spacing_small);
        }
    }

    /**
     * 设置按钮选择的背景
     *
     * @param textView
     * @param isSelect
     */
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackground(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_pressed));
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        } else {
            textView.setBackground(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_unpressed));
            textView.setTextColor(getContext().getResources().getColor(R.color.black_7));
        }
    }

    public void setProgressBar(ProgressBar bar) {
        auto_read_progress_bar = bar;
    }

    public Boolean isShow() {
        return isShowing();
    }

    @OnClick({R.id.tv_subtract, R.id.tv_add, R.id.iv_bg_default,
            R.id.iv_bg_1, R.id.iv_bg_2, R.id.iv_bg_3,
            R.id.iv_bg_4, R.id.iv_bg_7, R.id.iv_bg_8, R.id.tv_simulation,
            R.id.tv_cover, R.id.tv_slide, R.id.tv_none, R.id.tv_shangxia,
            R.id.margin_big, R.id.margin_medium, R.id.margin_small,
            R.id.auto_read_layout, R.id.tv_jianfan})
    public void onClick(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.tv_jianfan:
                    fanti = !fanti;
                    ShareUitls.putBoolean(mContext, "fanti", fanti);
                    if (fanti) {
                        tv_jianfan.setTextColor(Color.parseColor("#3ba166"));
                        tv_jianfan.setBackground(MyShape.setMyshapeStroke(getContext(), 2, 1, Color.parseColor("#3ba166")));
                    } else {
                        tv_jianfan.setTextColor(Color.BLACK);
                        tv_jianfan.setBackground(MyShape.setMyshape(ImageUtil.dp2px(getContext(), 2), Color.parseColor("#ededed")));
                    }
                    mSettingListener.changeTypeFace(fanti);
                    break;
                case R.id.tv_subtract:
                    subtractFontSize();
                    break;
                case R.id.tv_add:
                    addFontSize();
                    break;
                case R.id.iv_bg_default:
                    sePageStyle(BG_0);
                    break;
                case R.id.iv_bg_1:
                    sePageStyle(BG_1);
                    break;
                case R.id.iv_bg_2:
                    sePageStyle(BG_2);
                    break;
                case R.id.iv_bg_3:
                    sePageStyle(BG_3);
                    break;
                case R.id.iv_bg_4:
                    sePageStyle(BG_4);
                    break;
                case R.id.iv_bg_7:
                    sePageStyle(BG_7);
                    break;
                case R.id.iv_bg_8:
                    sePageStyle(BG_8);
                    break;
                case R.id.tv_simulation:
                    setPageMode(SIMULATION);
                    break;
                case R.id.tv_cover:
                    setPageMode(COVER);
                    break;
                case R.id.tv_slide:
                    setPageMode(SLIDE);
                    break;
                case R.id.tv_shangxia:
                    setPageMode(SCROLL);
                    break;
                case R.id.tv_none:
                    setPageMode(NONE);
                    break;
                case R.id.margin_big:
                    if (MarginMode != LINE_SPACING_SMALL) {
                        selectLineSpacing(LINE_SPACING_SMALL);
                        setLineSpacingMode(LINE_SPACING_SMALL);
                    }
                    break;
                case R.id.margin_medium:
                    if (MarginMode != LINE_SPACING_MEDIUM) {
                        selectLineSpacing(LINE_SPACING_MEDIUM);
                        setLineSpacingMode(LINE_SPACING_MEDIUM);
                    }
                    break;
                case R.id.margin_small:
                    if (MarginMode != LINE_SPACING_BIG) {
                        selectLineSpacing(LINE_SPACING_BIG);
                        setLineSpacingMode(LINE_SPACING_BIG);
                    }
                    break;
                case R.id.auto_read_layout:
                    if (true) {
                        AutoProgress.getInstance().setProgressBar(auto_read_progress_bar);
                        AutoProgress.getInstance().setTime((config.getAutoSpeed() ));
                        AutoProgress.getInstance().start(new AutoProgress.ProgressCallback() {
                            @Override
                            public void finish() {
                                if ( ChapterManager.getInstance().hasNextChapter() && InternetUtils.internet(mContext)) {
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ((HorizonPageAnim) mContext.mPvPage.mPageAnim).nextPage(true);
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                                } else {
                                    if (!AutoProgress.getInstance().isStop()) {
                                        AutoProgress.getInstance().stop();
                                    }
                                }
                            }
                        });
                        dismiss();
                    } else {
                        MyToash.ToashError(mContext, "当前模式不支持自动阅读");
                    }
                    break;
            }
        }
    }


    /**
     * 设置翻页
     *
     * @param toPageMode
     */
    public void setPageMode(PageMode toPageMode) {
        if (toPageMode != pageMode) {
            selectPageMode(toPageMode);
            config.setPageMode(toPageMode);
            mPageLoader.setPageMode(toPageMode, pageMode, mContext.chapter);
            pageMode = toPageMode;
        }
    }

    /**
     * 行距
     *
     * @param mode
     */
    public void setLineSpacingMode(float mode) {
        config.setLineSpacingMode(mode);
        mPageLoader.setLineSpacingMode(mode);
        if (mSettingListener != null) {
            mSettingListener.changeLineSpacing(mode);
        }
    }

    /**
     * 变大书本字体
     */
    private void addFontSize() {
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1;
            tv_size.setText(currentFontSize + "");
            config.setTextSize(currentFontSize);
            mPageLoader.setTextSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    private void sePageStyle(PageStyle i) {
        if (pageStyle != i) {
            setPageStyleUi(pageStyle, false);
            setPageStyleUi(i, true);
            pageStyle = i;
            config.setPageStyle(i);
            config.setLastLightPageStyle(i);
            mContext.changeDayOrNight(true);
            mPageLoader.setPageStyle(i);
        }
    }

    private void defaultFontSize() {
        currentFontSize = (int) getContext().getResources().getDimension(R.dimen.reading_default_text_size);
        tv_size.setText(currentFontSize + "");
        config.setTextSize(currentFontSize);
        if (mSettingListener != null) {
            mSettingListener.changeFontSize(currentFontSize);
        }
    }

    /**
     * 变小书本字体
     */
    private void subtractFontSize() {
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1;
            tv_size.setText(currentFontSize + "");
            config.setTextSize(currentFontSize);
            mPageLoader.setTextSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    public interface SettingListener {

        void changeSystemBright(Boolean isSystem, float brightness);

        void changeFontSize(int fontSize);

        void changeTypeFace(boolean typeface);

        void changeBookBg(PageStyle type);

        void changeLineSpacing(float mode);
    }
}