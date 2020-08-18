package com.ssreader.novel.ui.fragment;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.RechargeChannelAdapter;
import com.ssreader.novel.ui.adapter.RechargeGoldAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.PublicCallBackSpan;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.SystemUtil;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

public class RechargeGoldFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView publicRecycleview;
    @BindView(R.id.fragment_movieticket_rcy)
    RecyclerView fragmentMovieticketRcy;
    @BindView(R.id.activity_recharge_instructions)
    LinearLayout activity_recharge_instructions;
    @BindView(R.id.fragment_recharge_gold)
    TextView fragment_recharge_gold;
    @BindViews({R.id.fragment_recharge_gold, R.id.fragment_recharge_gold_name,
            R.id.fragment_recharge_shuquan, R.id.fragment_recharge_shuquan_name})
    List<TextView> rechargeTexts;
    @BindView(R.id.MineNewFragment_app_install_desc)
    TextView appInstallDesc;

    public TextView textView;
    private TextView pay_discount_tv;
    private String current_price;
    private List<PayBeen.ItemsBean.PalChannelBean> channelList;
    private List<PayBeen.ItemsBean> payListBeanList;
    private RechargeGoldAdapter rechargeMovieAdapter;
    private RechargeChannelAdapter rechargeChannelAdapter;
    private String mPrice;

    public String mGoodsId;
    public PayBeen.ItemsBean.PalChannelBean palChannelBean;

    public RechargeGoldFragment() {
    }

    public RechargeGoldFragment(TextView textView, TextView pay_discount_tv) {
        this.textView = textView;
        this.pay_discount_tv = pay_discount_tv;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_recharge_gold;
    }

    @Override
    public void initView() {
        payListBeanList = new ArrayList<>();
        channelList = new ArrayList<>();
        initSCRecyclerView(publicRecycleview, RecyclerView.VERTICAL, 0);
        publicRecycleview.setPullRefreshEnabled(false);
        publicRecycleview.setLoadingMoreEnabled(false);
        rechargeChannelAdapter = new RechargeChannelAdapter(channelList, activity);
        publicRecycleview.setAdapter(rechargeChannelAdapter);
        fragmentMovieticketRcy.setLayoutManager(new LinearLayoutManager(activity));
        rechargeMovieAdapter = new RechargeGoldAdapter(payListBeanList, activity);
        fragmentMovieticketRcy.setAdapter(rechargeMovieAdapter);
        initListener();
    }

    private void initListener() {
        // 充值通道
        rechargeChannelAdapter.setOnRechargeClick(new RechargeChannelAdapter.onRechargeClick() {
            @Override
            public void onRecharge(int position) {
                for (PayBeen.ItemsBean.PalChannelBean palChannelBean : channelList) {
                    palChannelBean.choose = false;
                }
                palChannelBean = channelList.get(position);
                palChannelBean.choose = true;
                rechargeChannelAdapter.notifyDataSetChanged();
            }
        });
        // 充值选项
        rechargeMovieAdapter.setOnRechargeClick(new RechargeGoldAdapter.onRechargeClick() {
            @Override
            public void onRecharge(int position) {
                // 将充值通道置空
                palChannelBean = null;
                channelList.clear();
                for (PayBeen.ItemsBean payListBean : payListBeanList) {
                    payListBean.choose = false;
                }
                textView.setText(payListBeanList.get(position).getFat_price());
                payListBeanList.get(position).choose = true;
                rechargeMovieAdapter.notifyDataSetChanged();
                mPrice = payListBeanList.get(position).getPrice();
                mGoodsId = payListBeanList.get(position).goods_id + "";
                if (payListBeanList.get(position).pal_channel != null && !payListBeanList.get(position).pal_channel.isEmpty()) {
                    channelList.addAll(payListBeanList.get(position).pal_channel);
                    for (PayBeen.ItemsBean.PalChannelBean palChannelBean : payListBeanList.get(position).pal_channel) {
                        palChannelBean.choose = false;
                    }
                    channelList.get(0).choose = true;
                    palChannelBean = channelList.get(0);
                    rechargeChannelAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.mPayRechargeCenterUrl,
                readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        channelList.clear();
        payListBeanList.clear();
        PayBeen vipPayBean = gson.fromJson(json, PayBeen.class);
        if (vipPayBean.getUnit_tag() != null) {
            rechargeTexts.get(1).setText(vipPayBean.getUnit_tag().getCurrencyUnit());
            rechargeTexts.get(3).setText(vipPayBean.getUnit_tag().getSubUnit());
        }
        if (UserUtils.isLogin(activity)) {
            rechargeTexts.get(0).setText(vipPayBean.goldRemain);
            rechargeTexts.get(2).setText(vipPayBean.silverRemain + "");
        } else {
            rechargeTexts.get(0).setText("- -");
            rechargeTexts.get(2).setText("- -");
        }
        if (!vipPayBean.items.isEmpty()) {
            current_price = vipPayBean.items.get(0).getFat_price();
            vipPayBean.items.get(0).choose = true;
            payListBeanList.addAll(vipPayBean.items);
            mPrice = vipPayBean.items.get(0).getFat_price();
            textView.setText(mPrice);
            mGoodsId = vipPayBean.items.get(0).goods_id + "";
            if (vipPayBean.items.get(0).pal_channel != null && !vipPayBean.items.get(0).pal_channel.isEmpty()) {
                channelList.addAll(vipPayBean.items.get(0).pal_channel);
                palChannelBean = channelList.get(0);
                palChannelBean.choose = true;
            }
            rechargeMovieAdapter.notifyDataSetChanged();
        }
        rechargeChannelAdapter.notifyDataSetChanged();
        setRechargeInfo(activity, vipPayBean.about, activity_recharge_instructions);
    }

    public static void setRechargeInfo(Activity activity, List<String> about, LinearLayout activity_recharge_instructions) {
        try {
            if (!about.isEmpty()) {
                if (activity_recharge_instructions.getChildCount() > 0) {
                    activity_recharge_instructions.removeAllViews();
                }
                // 用于记录字符位置
                int userStart, privacyStart, opinionStart, vipStart;
                // 现共有三条信息
                int H5 = ImageUtil.dp2px(activity, 5);
                for (String info : about) {
                    // 显示说明
                    TextView textView = new TextView(activity);
                    textView.setLineSpacing(H5, 1);
                    textView.setHighlightColor(ContextCompat.getColor(activity,R.color.transparent));

                    textView.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                    // 动态给一段文本内容设置不同的样式
                    SpannableStringBuilder style = new SpannableStringBuilder();
                    // 添加文本
                    style.append(info);
                    for (int i = 0; i < info.length(); i++) {
                        char c = info.charAt(i);
                        char c1 = '1';
                        if (i + 1 < info.length()) {
                            c1 = info.charAt(i + 1);
                        }
                        if (String.valueOf(c).equals("《")) {
                            if (String.valueOf(c1).equals("用")) {
                                userStart = i;
                                ForegroundColorSpan userColorSpan = new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.maincolor));
                                PublicCallBackSpan userSpan = new PublicCallBackSpan(activity, 5);
                                style.setSpan(userSpan, userStart, userStart + 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                style.setSpan(userColorSpan, userStart, userStart + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            if (String.valueOf(c).equals("《")) {
                                if (String.valueOf(c1).equals("隐")) {
                                    privacyStart = i;
                                    ForegroundColorSpan privacyColorSpan = new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.maincolor));
                                    PublicCallBackSpan privacySpan = new PublicCallBackSpan(activity, 2);
                                    style.setSpan(privacySpan, privacyStart, privacyStart + 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                    style.setSpan(privacyColorSpan, privacyStart, privacyStart + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            if (String.valueOf(c).equals("《")) {
                                if (String.valueOf(c1).equals("会")) {
                                    vipStart = i;
                                    ForegroundColorSpan vipColorSpan = new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.maincolor));
                                    PublicCallBackSpan vipSpan = new PublicCallBackSpan(activity, 4);
                                    style.setSpan(vipSpan, vipStart, vipStart + 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                    style.setSpan(vipColorSpan, vipStart, vipStart + 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                        }
                        if (String.valueOf(c).equals("【")) {
                            if (String.valueOf(c1).equals("意")) {
                                opinionStart = i;
                                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.maincolor));
                                PublicCallBackSpan opinionSpan = new PublicCallBackSpan(activity, 3);
                                style.setSpan(opinionSpan, opinionStart, opinionStart + 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                style.setSpan(colorSpan, opinionStart, opinionStart + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                    // 激活点击
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    // 将样式写入TextView
                    textView.setText(style);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = 2 * H5;
                    // 添加view
                    activity_recharge_instructions.addView(textView, layoutParams);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 用户支付后刷新界面
     * @param toStore
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshPay(RefreshMine toStore) {
        if (toStore.isPayVip) {
            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.PayActivity_zhifuok));
        }
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SystemUtil.checkAppInstalled(activity, SystemUtil.WEChTE_PACKAGE_NAME) &&
                !SystemUtil.checkAppInstalled(activity, SystemUtil.ALIPAY_PACKAGE_NAME)) {
            appInstallDesc.setVisibility(View.VISIBLE);
        } else {
            appInstallDesc.setVisibility(View.GONE);
        }
    }
}
