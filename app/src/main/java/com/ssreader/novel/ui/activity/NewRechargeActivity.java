package com.ssreader.novel.ui.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.pay.alipay.AlipayGoPay;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.ui.fragment.RechargeGoldFragment;
import com.ssreader.novel.ui.fragment.RechargeVipFragment;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.SystemUtil;
import com.ssreader.novel.utils.UserUtils;
import com.ssreader.novel.pay.wxpay.WXGoPay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.LIUSHUIJIELU;

public class NewRechargeActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_back)
    RelativeLayout publicSnsTopbarBack;
    @BindView(R.id.activity_viewpager)
    ViewPager activityViewpager;
    @BindView(R.id.pay_discount_tv)
    TextView pay_discount_tv;
    @BindView(R.id.public_sns_topbar_back_img)
    ImageView publicSnsTopbarBackImg;
    @BindView(R.id.pay_recharge_tv)
    TextView pay_recharge_tv;

    @BindView(R.id.public_sns_topbar_title)
    TextView public_sns_topbar_title;
    @BindView(R.id.public_sns_topbar_layout)
    RelativeLayout public_sns_topbar_layout;
    @BindView(R.id.public_sns_topbar_right_other)
    RelativeLayout public_sns_topbar_right_other;
    @BindView(R.id.public_sns_topbar_right_tv)
    TextView public_sns_topbar_right_tv;

    @BindView(R.id.activity_pay_tv)
    TextView activity_pay_tv;

    private List<Fragment> fragmentList;
    private RechargeVipFragment rechargeVipFragment;
    private RechargeGoldFragment rechargeGoldFragment;

    public String rechargeType;
    public static Activity activity;

    @Override
    public int initContentView() {
        isImmersionbar = true;
        USE_EventBus = true;
        return R.layout.activity_new_recharge;
    }

    @OnClick({R.id.public_sns_topbar_back, R.id.activity_pay_tv, R.id.public_sns_topbar_right_other,
            R.id.pay_recharge_tv_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_sns_topbar_right_other:
                startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("OPTION", LIUSHUIJIELU)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.MineNewFragment_liushuijilu_title))
                        .putExtra("Extra", false));
                break;
            case R.id.public_sns_topbar_back:
                finish();
                break;
            case R.id.activity_pay_tv:
                if (LoginUtils.goToLogin(activity)) {
                    if (rechargeType.equals("vip")) {
                        payGood(rechargeVipFragment.palChannelBean, rechargeVipFragment.mGoodsId);
                    } else if (rechargeType.equals("gold")) {
                        payGood(rechargeGoldFragment.palChannelBean, rechargeGoldFragment.mGoodsId);
                    }
                }
                break;
            case R.id.pay_recharge_tv_layout:
                break;
        }
    }

    @Override
    public void initView() {
        activity = this;
        fragmentList = new ArrayList<>();
        publicSnsTopbarBackImg.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        public_sns_topbar_right_tv.setTextColor(Color.WHITE);
        public_sns_topbar_title.setTextColor(Color.WHITE);
        public_sns_topbar_title.setText(formIntent.getStringExtra("RechargeTitle"));
        public_sns_topbar_right_tv.setText(formIntent.getStringExtra("RechargeRightTitle"));
        rechargeType = formIntent.getStringExtra("RechargeType");
        if (rechargeType.equals("vip")) {
            public_sns_topbar_right_other.setVisibility(View.GONE);
            rechargeVipFragment = new RechargeVipFragment(pay_recharge_tv);
            fragmentList.add(rechargeVipFragment);
            activity_pay_tv.setText(LanguageUtil.getString(activity, R.string.BaoyueActivity_kaitong));
        } else if (rechargeType.equals("gold")) {
            public_sns_topbar_right_other.setVisibility(View.VISIBLE);
            rechargeGoldFragment = new RechargeGoldFragment(pay_recharge_tv, pay_discount_tv);
            fragmentList.add(rechargeGoldFragment);
            activity_pay_tv.setText(LanguageUtil.getString(activity, R.string.BaoyueActivity_now_pay));
        }
        activityViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    /**
     * 支付
     * @param palChannelBean
     * @param mGoosId
     */
    public void payGood(PayBeen.ItemsBean.PalChannelBean palChannelBean, String mGoosId) {
        if (palChannelBean != null && mGoosId != null) {
            switch (palChannelBean.getPay_type()) {
                case 1:
                    // 界面内调用sdk
                    if (palChannelBean.getChannel_id() == 1) {
                        if (SystemUtil.checkAppInstalled(activity, SystemUtil.WEChTE_PACKAGE_NAME)) {
                            WXGoPay wxGoPay = new WXGoPay(this, activity_pay_tv);
                            wxGoPay.requestPayOrder(Api.mWXPayUrl, mGoosId);
                        } else {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.Mine_no_install_wechat));
                        }
                    } else if (palChannelBean.getChannel_id() == 2) {
                        if (SystemUtil.checkAppInstalled(activity, SystemUtil.ALIPAY_PACKAGE_NAME)) {
                            AlipayGoPay alipayGoPay = new AlipayGoPay(this, activity_pay_tv);
                            alipayGoPay.requestPayOrder(Api.mAlipayUrl, mGoosId);
                        } else {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.Mine_no_install_alipay));
                        }
                    }
                    break;
                case 2:
                case 4:
                    // 应用内使用web
                    if (palChannelBean.getGateway() != null && !TextUtils.isEmpty(palChannelBean.getGateway())) {
                        Intent intent = new Intent();
                        intent.setClass(activity, WebViewActivity.class);
                        intent.putExtra("title", palChannelBean.getTitle());
                        if (palChannelBean.getGateway().contains("?")) {
                            intent.putExtra("url", palChannelBean.getGateway() +
                                    "&token=" + UserUtils.getToken(activity) +
                                    "&goods_id=" + mGoosId);
                        } else {
                            intent.putExtra("url", palChannelBean.getGateway() +
                                    "?token=" + UserUtils.getToken(activity) +
                                    "&goods_id=" + mGoosId);
                        }
                        if (palChannelBean.getPay_type() == 2) {
                            intent.putExtra("is_otherBrowser", true);
                        }
                        startActivity(intent);
                    }
                    break;
            }
        } else {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.PayActivity_zhifucuowu));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }
}
