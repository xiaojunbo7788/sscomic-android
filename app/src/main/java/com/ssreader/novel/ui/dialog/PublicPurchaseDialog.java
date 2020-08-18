package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.suke.widget.SwitchButton;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AdStatusRefresh;
import com.ssreader.novel.eventbus.RefreshPageFactoryChapter;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.eventbus.ChapterBuyRefresh;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.model.PurchaseDialogBean;
import com.ssreader.novel.model.PurchaseItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.ui.activity.NewRechargeActivity;
import com.ssreader.novel.ui.activity.SettingActivity;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.getCurrencyUnit;

/**
 * 购买弹窗
 */
public class PublicPurchaseDialog extends Dialog {

    @BindView(R.id.dialog_purchase_some_remain)
    TextView dialog_purchase_some_remain;
    @BindView(R.id.dialog_purchase_some_auto_buy)
    SwitchButton dialog_purchase_some_auto_buy;
    @BindView(R.id.dialog_purchase_some_total_price)
    TextView dialog_purchase_some_total_price;
    @BindView(R.id.dialog_purchase_some_original_price)
    TextView dialog_purchase_some_original_price;
    @BindView(R.id.dialog_purchase_some_buy)
    TextView dialog_purchase_some_buy;
    @BindView(R.id.dialog_purchase_some_tite)
    TextView dialog_purchase_some_tite;

    @BindView(R.id.dialog_purchase_auto)
    RelativeLayout purchaseAutoLayout;

    @BindView(R.id.dialog_purchase_HorizontalScrollView)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.dialog_purchase_some_select_rgs)
    RadioGroup radioGroup;
    @BindView(R.id.dialog_discount_tv)
    TextView dialog_discount_tv;
    @BindView(R.id.dialog_purchase_some_original_price_layout)
    View dialog_purchase_some_original_price_layout;

    @BindViews({R.id.dialog_purchase_line2, R.id.dialog_purchase_line3})
    List<View> lines;

    private int mFlag = 0;
    private Activity activity;
    private boolean isdown;
    private BuySuccess buySuccess;
    private int mNum;
    private boolean CanceledOnTouchOutside;
    private int productType;
    private BookChapter bookChapter;
    private boolean isreaderbook;
    private OnPurchaseClickListener onPurchaseClickListener;
    private String unit, subUnit;
    private long remain;
    private boolean auto_sub;

    public void setOnPurchaseClickListener(OnPurchaseClickListener onPurchaseClickListener) {
        this.onPurchaseClickListener = onPurchaseClickListener;
    }

    public PublicPurchaseDialog(Activity context, int productType, boolean isdown, BuySuccess buySuccess, boolean CanceledOnTouchOutside) {
        super(context, R.style.BottomDialog);
        this.isdown = isdown;
        this.productType = productType;
        this.buySuccess = buySuccess;
        this.CanceledOnTouchOutside = CanceledOnTouchOutside;
        activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_purchase_some);
        setCanceledOnTouchOutside(CanceledOnTouchOutside);
        // 初始化View注入
        ButterKnife.bind(this);
        dialog_purchase_some_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        if (productType == Constant.COMIC_CONSTANT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        }
        p.width = ScreenSizeUtils.getInstance(getContext()).getScreenWidth();
        getWindow().setAttributes(p);
        if (isdown) {
            purchaseAutoLayout.setVisibility(View.GONE);
            lines.get(1).setVisibility(View.GONE);
        }
        auto_sub = ShareUitls.getSetBoolean(getContext(), Constant.AUTOBUY, true);
        if (auto_sub) {
            dialog_purchase_some_auto_buy.setChecked(true);
        }
    }


    public void setIsreaderbook(boolean isreaderbook) {
        this.isreaderbook = isreaderbook;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (productType == Constant.COMIC_CONSTANT) {
            if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.dialog_purchase_head_back, R.id.dialog_purchase_head_list,
            R.id.dialog_purchase_head_layout, R.id.dialog_purchase_center_layout})
    public void onHeadClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_purchase_head_back:
                if (onPurchaseClickListener != null) {
                    onPurchaseClickListener.onBack();
                }
                if (CanceledOnTouchOutside) {
                    dismiss();
                }
                break;
            case R.id.dialog_purchase_head_list:
                if (onPurchaseClickListener != null) {
                    onPurchaseClickListener.onGotoList();
                }
                if (CanceledOnTouchOutside) {
                    dismiss();
                }
                break;
            case R.id.dialog_purchase_head_layout:
            case R.id.dialog_purchase_center_layout:
                if (CanceledOnTouchOutside) {
                    dismiss();
                }
                break;
        }
    }

    /**
     * 用于批量购买
     *  @param Id
     * @param chapterId
     * @param isdown
     * @param downoption
     */
    public void initData(final long Id, final String chapterId, boolean isdown, Downoption downoption) {
        String url = "";
        ReaderParams params = new ReaderParams(activity);
        if (productType == Constant.BOOK_CONSTANT) {
            url = Api.mChapterBuyIndex;
            params.putExtraParams("book_id", Id);
        } else if (productType == Constant.AUDIO_CONSTANT) {
            url = Api.audio_chapter_buyoption;
            params.putExtraParams("audio_id", Id);
        } else if (productType == Constant.COMIC_CONSTANT) {
            url = Api.COMIC_buy_index;
            params.putExtraParams("comic_id", Id);
        }
        if (isdown) {
            if (productType != Constant.COMIC_CONSTANT) {
                if (downoption != null) {
                    params.putExtraParams("num", downoption.down_num + "");
                    mNum = downoption.down_num;
                }
            }
            params.putExtraParams("page_from", "down");
        } else {
            params.putExtraParams("page_from", "read");
        }
        params.putExtraParams("chapter_id", chapterId);
        HttpUtils.getInstance().sendRequestRequestParams(activity, url, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        PurchaseDialogBean purchaseDialogBean = HttpUtils.getGson().fromJson(result, PurchaseDialogBean.class);
                        PurchaseDialogBean.BaseInfoBean base_info = purchaseDialogBean.getBase_info();
                        remain = base_info.getRemain();
                        long goldRemain = base_info.getGold_remain();
                        long silverRemain = base_info.getSilver_remain();
                        unit = base_info.getUnit();
                        subUnit = base_info.getSubUnit();
                        int auto_sub_t = base_info.getAuto_sub();
                        dialog_purchase_some_remain.setText(getText(goldRemain, silverRemain));
                        List<PurchaseItem> purchaseItems = purchaseDialogBean.getBuy_option();
                        // 设置选项
                        if (purchaseItems != null && !purchaseItems.isEmpty()) {
                            if (!isdown) {
                                radioGroup.removeAllViews();
                                radioGroup.setOrientation(RadioGroup.HORIZONTAL);
                                for (int k = 0; k < purchaseItems.size(); k++) {
                                    PurchaseItem purchaseItem = purchaseItems.get(k);
                                    RadioButton radioButton = (RadioButton) LayoutInflater.from(activity).inflate(R.layout.activity_radiobutton_purchase, null, false);
                                    radioButton.setId(k);
                                    radioButton.setBackgroundResource(R.drawable.selector_purchase_some);
                                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, ImageUtil.dp2px(activity, 25));
                                    params.rightMargin = 10;
                                    if (k == 0) {
                                        radioButton.setChecked(true);
                                    }
                                    radioButton.setText(purchaseItem.getLabel());
                                    radioGroup.addView(radioButton, params);
                                }
                            } else {
                                horizontalScrollView.setVisibility(View.GONE);
                                lines.get(0).setVisibility(View.GONE);
                            }
                            setText(purchaseItems.get(0));
                            // 点击事件
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    setText(purchaseItems.get(checkedId));
                                }
                            });
                        }
                        // 自动购买
                        if (auto_sub != (auto_sub_t == 1)) {
                            auto_sub = auto_sub_t == 1;
                            dialog_purchase_some_auto_buy.setChecked(auto_sub);
                            ShareUitls.putSetBoolean(activity, Constant.AUTOBUY, auto_sub);
                        }
                        dialog_purchase_some_auto_buy.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                SettingActivity.Auto_sub(activity, new SettingActivity.Auto_subSuccess() {
                                    @Override
                                    public void success(boolean open) {
                                        if (open) {
                                            if (productType == Constant.BOOK_CONSTANT) {
                                                if (mFlag == 1) {
                                                    // 开启后重新刷新数据
                                                    if (BWNApplication.applicationContext.getActivity() instanceof ReadActivity) {
                                                        EventBus.getDefault().post(new AdStatusRefresh(true));
                                                    }
                                                }
                                            }
//                                            else if (productType == Constant.COMIC_CONSTANT) {
//                                                if (mFlag == 1) {
//                                                    purchaseSingleChapter(Id, chapterId, 2);
//                                                }
//                                            }
                                        }
                                    }
                                });
                            }
                        });
                        // 确定按钮
                        dialog_purchase_some_buy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mFlag == -1) {
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    if (isdown) {
                                        Dismiss();
                                    }
                                } else if (mFlag == 0) {
                                    activity.startActivity(new Intent(activity, NewRechargeActivity.class)
                                            .putExtra("RechargeTitle", getCurrencyUnit(activity) + LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi))
                                            .putExtra("RechargeRightTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_chongzhijilu))
                                            .putExtra("RechargeType", "gold"));
                                    if (isdown) {
                                        Dismiss();
                                    }
                                } else if (mFlag == 1) {
                                    purchaseSingleChapter(Id, chapterId, mNum);
                                }
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        Dismiss();
                    }
                }
        );
    }

    public void purchaseSingleChapter(final long id, final String chapter_id, final int num) {
        ReaderParams params = new ReaderParams(activity);
        String url = "";
        if (productType == Constant.BOOK_CONSTANT) {
            url = Api.mChapterBuy;
            params.putExtraParams("book_id", id);
        } else if (productType == Constant.AUDIO_CONSTANT) {
            params.putExtraParams("audio_id", id);
            url = Api.audio_chapter_buy;
        } else if (productType == Constant.COMIC_CONSTANT) {
            params.putExtraParams("comic_id", id);
            url = Api.COMIC_buy_buy;
        }
        ChapterManager.getInstance().purchaseSingleChapter(BWNApplication.applicationContext.getActivity(), url, id, chapter_id, num, new ChapterManager.OnPurchaseListener() {
            @Override
            public void purchaseSuc(long[] chapter_ids) {
                if (chapter_ids != null) {
                    if (productType == Constant.BOOK_CONSTANT) {
                        for (long chapter_id : chapter_ids) {
                            if (!isreaderbook && isdown) {
                                //单纯下载
                                BookChapter bookChapterLocal = ObjectBoxUtils.getBookChapter(chapter_id);
                                if (bookChapterLocal != null) {
                                    bookChapterLocal.is_preview = 0;
                                    bookChapterLocal.chapter_path = null;
                                    ObjectBoxUtils.addData(bookChapterLocal, BookChapter.class);
                                }
                            } else {//在阅读器界面的下载
                                BookChapter tempbookChapter = ChapterManager.getInstance().getChapter(chapter_id);
                                if (tempbookChapter != null) {
                                    tempbookChapter.setIs_preview(0);
                                    tempbookChapter.chapter_path = null;
                                    if (bookChapter == null) {
                                        bookChapter = tempbookChapter;
                                    }
                                }
                                ObjectBoxUtils.addData(bookChapter, BookChapter.class);
                            }
                        }
                        if (isdown) {//下载
                            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.ReadActivity_buysuccessyidown));
                            if (buySuccess != null) {
                                buySuccess.buySuccess(chapter_ids, num);
                            }
                            if (isreaderbook && bookChapter != null) {
                                EventBus.getDefault().post(new RefreshPageFactoryChapter(bookChapter, null));
                            }
                        } else {
                            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.ReadActivity_buysuccess));
                            if (bookChapter != null) {
                                EventBus.getDefault().post(new RefreshPageFactoryChapter(bookChapter, null));
                            }
                        }
                    } else {
                        for (long chapter_id : chapter_ids) {
                            if (productType == Constant.COMIC_CONSTANT) {
                                ComicChapter comicChapter = ObjectBoxUtils.getComicChapter(chapter_id);
                                if (comicChapter != null) {
                                    comicChapter.is_preview = 0;
                                    ObjectBoxUtils.addData(comicChapter, ComicChapter.class);
                                }
                            } else {
                                AudioChapter audioChapter = ObjectBoxUtils.getAudioChapter(chapter_id);
                                if (audioChapter != null) {
                                    audioChapter.setIs_preview(0);
                                    ObjectBoxUtils.addData(audioChapter, AudioChapter.class);
                                }
                            }
                        }
                        MyToash.ToashSuccess(activity, R.string.ReadActivity_buysuccess);
                        EventBus.getDefault().post(new ChapterBuyRefresh(productType, num, chapter_ids));
                        if (buySuccess != null) {
                            buySuccess.buySuccess(chapter_ids, num);
                        }
                    }
                }
                Dismiss();
            }
        });
    }

    public interface BuySuccess {

        void buySuccess(long[] ids, int num);
    }

    public interface OnPurchaseClickListener {

        void onBack();

        void onGotoList();
    }

    private boolean MyshelfDismiss;

    public void Dismiss() {
        MyshelfDismiss = true;
        dismiss();
    }

    @Override
    public void dismiss() {
        if (!MyshelfDismiss) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ComicDownActivity_cancle_buy));
        }
        super.dismiss();
    }

    /**
     * 设置选项的UI
     * @param purchaseItem
     */
    private void setText(PurchaseItem purchaseItem) {
        if (purchaseItem == null) {
            return;
        }
        if (!TextUtils.isEmpty(purchaseItem.getDiscount())) {
            dialog_discount_tv.setVisibility(View.VISIBLE);
            dialog_discount_tv.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 3),
                    ContextCompat.getColor(activity, R.color.red)));
            dialog_discount_tv.setText(purchaseItem.getDiscount());
        } else {
            dialog_discount_tv.setVisibility(View.GONE);
        }
        dialog_purchase_some_tite.setText(purchaseItem.getLabel());
        dialog_purchase_some_total_price.setText(getText(purchaseItem.actual_cost.gold_cost,
                purchaseItem.actual_cost.silver_cost));
        if (!TextUtils.isEmpty(purchaseItem.getDiscount())) {
            dialog_purchase_some_original_price_layout.setVisibility(View.VISIBLE);
            dialog_purchase_some_original_price.setText(getText(purchaseItem.original_cost.gold_cost,
                    purchaseItem.original_cost.silver_cost));
        } else {
            dialog_purchase_some_original_price_layout.setVisibility(View.GONE);
        }
        if (productType != Constant.COMIC_CONSTANT) {
            if (!isdown) {
                mNum = purchaseItem.getBuy_num();
            }
        } else {
            mNum = purchaseItem.getBuy_num();
        }
        // 余额不足，提示"充值并购买"
        if (!UserUtils.isLogin(activity)) {
            dialog_purchase_some_buy.setText(LanguageUtil.getString(activity, R.string.ReadActivity_login_buy));
            mFlag = -1;
        } else if (remain < purchaseItem.getTotal_price()) {
            dialog_purchase_some_buy.setText(LanguageUtil.getString(activity, R.string.ReadActivity_chongzhibuy));
            mFlag = 0;
        } else {//余额够显示"确认购买"
            dialog_purchase_some_buy.setText(LanguageUtil.getString(activity, R.string.ReadActivity_buy));
            mFlag = 1;
        }
    }

    private String getText(long goldRemain, long silverRemain) {
        String Text = "";
        if (!TextUtils.isEmpty(unit) || !TextUtils.isEmpty(subUnit)) {
            if (TextUtils.isEmpty(unit)) {
                Text = silverRemain + subUnit;
            } else {
                if (TextUtils.isEmpty(subUnit)) {
                    Text = goldRemain + unit;
                } else {
                    if ((goldRemain != 0 && silverRemain != 0) || (goldRemain == 0 && silverRemain == 0)) {
                        Text = goldRemain + unit + " + " + silverRemain + subUnit;
                    } else if (goldRemain != 0) {
                        Text = goldRemain + unit;
                    } else {
                        Text = silverRemain + subUnit;
                    }
                }
            }
        }
        return Text;
    }
}
