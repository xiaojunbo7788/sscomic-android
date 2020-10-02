package com.ssreader.novel.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.AcquirePrivilegeItem;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.model.BaseStoreMemberCenterBean;
import com.ssreader.novel.model.PayBeen;
import com.ssreader.novel.model.VipPayBeen;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.ui.adapter.RechargeChannelAdapter;
import com.ssreader.novel.ui.adapter.RechargePrivilegeAdapter;
import com.ssreader.novel.ui.adapter.RechargeVipAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.banner.ConvenientBanner;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.SystemUtil;
import com.ssreader.novel.utils.UpdateApp;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;

public class RechargeVipFragment extends BaseFragment {

    @BindView(R.id.activity_user_img)
    ImageView activityUserImg;
    @BindView(R.id.activity_user_name)
    TextView activityUserName;
    @BindView(R.id.acitivity_vip_img)
    ImageView acitivityVipImg;
    @BindView(R.id.activity_vip_time)
    TextView activityVipTime;
    @BindView(R.id.activity_recharge_head_layout)
    LinearLayout activityRechargeHeadLayout;
    @BindView(R.id.activity_head_bar_rcy)
    RecyclerView activityHeadBarRcy;
    @BindView(R.id.activity_pay_channel_rcy)
    RecyclerView activityPayChannelRcy;
    @BindView(R.id.framgent_tequan_rcy)
    RecyclerView framgentTequanRcy;
    @BindView(R.id.activity_recharge_instructions)
    LinearLayout activity_recharge_instructions;
    @BindView(R.id.MineNewFragment_app_install_desc)
    TextView appInstallDesc;
    @BindView(R.id.activity_baoyue_banner_male)
    ConvenientBanner convenientBanner;

    public String mGoodsId;
    public PayBeen.ItemsBean.PalChannelBean palChannelBean;

    private TextView pay_recharge_tv;
    private String current_price;
    private RechargePrivilegeAdapter rechargePrivilegeAdapter;
    private VipPayBeen vipPayBeen;
    private List<PayBeen.ItemsBean> payListBeanList;

    private RechargeVipAdapter payHeadBarAdapter;
    private List<PayBeen.ItemsBean.PalChannelBean> palChannelBeanList;
    private RechargeChannelAdapter rechargeChannelAdapter;
    private String mPrice;
    private List<AcquirePrivilegeItem> iconList;
    private int is_vip;

    public RechargeVipFragment(TextView pay_recharge_tv) {
        this.pay_recharge_tv = pay_recharge_tv;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_recharge_vip;
    }

    @Override
    public void initView() {
        activityRechargeHeadLayout.setBackgroundResource(R.mipmap.pay_user_vip_bg);
        iconList = new ArrayList<>();
        payListBeanList = new ArrayList<>();
        palChannelBeanList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        activityHeadBarRcy.setLayoutManager(linearLayoutManager);
        payHeadBarAdapter = new RechargeVipAdapter(payListBeanList, activity);
        activityHeadBarRcy.setAdapter(payHeadBarAdapter);
        activityPayChannelRcy.setLayoutManager(new LinearLayoutManager(activity));
        rechargeChannelAdapter = new RechargeChannelAdapter(palChannelBeanList, activity);
        activityPayChannelRcy.setAdapter(rechargeChannelAdapter);
        rechargePrivilegeAdapter = new RechargePrivilegeAdapter(iconList, activity);
        framgentTequanRcy.setLayoutManager(new GridLayoutManager(activity, 4));
        framgentTequanRcy.setAdapter(rechargePrivilegeAdapter);
        initListener();
    }

    private void initListener() {
        // 充值通道
        rechargeChannelAdapter.setOnRechargeClick(new RechargeChannelAdapter.onRechargeClick() {
            @Override
            public void onRecharge(int position) {
                for (PayBeen.ItemsBean.PalChannelBean palChannelBean : palChannelBeanList) {
                    palChannelBean.choose = false;
                }
                palChannelBean = palChannelBeanList.get(position);
                palChannelBean.choose = true;
                rechargeChannelAdapter.notifyDataSetChanged();
            }
        });
        // 充值套餐
        payHeadBarAdapter.setOnRechargeClick(new RechargeVipAdapter.onRechargeClick() {
            @Override
            public void onRecharge(int position) {
                palChannelBean = null;
                palChannelBeanList.clear();
                for (PayBeen.ItemsBean payListBean : payListBeanList) {
                    payListBean.choose = false;
                }
                pay_recharge_tv.setText(payListBeanList.get(position).getFat_price());
                payListBeanList.get(position).choose = true;
                payHeadBarAdapter.notifyDataSetChanged();
                mPrice = payListBeanList.get(position).getPrice();
                mGoodsId = payListBeanList.get(position).goods_id + "";
                if (payListBeanList.get(position).pal_channel != null && !payListBeanList.get(position).pal_channel.isEmpty()) {
                    palChannelBeanList.addAll(payListBeanList.get(position).pal_channel);
                    for (PayBeen.ItemsBean.PalChannelBean palChannelBean : payListBeanList.get(position).pal_channel) {
                        palChannelBean.choose = false;
                    }
                    palChannelBeanList.get(0).choose = true;
                    palChannelBean = palChannelBeanList.get(0);
                    rechargeChannelAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.mPayBaoyueCenterUrl, readerParams.generateParamsJson(),responseListener);

        httpUtils.sendRequestRequestParams(activity, Api.MEMBER_CENTER, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                BaseStoreMemberCenterBean memberCenterBean = gson.fromJson(response, BaseStoreMemberCenterBean.class);
                if (memberCenterBean.getBanner() != null) {
                    convenientBanner.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)convenientBanner.getLayoutParams();
                    layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
                    layoutParams.height = (layoutParams.width - ImageUtil.dp2px(activity, 20)) / 4;
                    convenientBanner.setLayoutParams(layoutParams);
                    ConvenientBanner.initBanner(activity, 3, memberCenterBean.getBanner(), convenientBanner, COMIC_CONSTANT);
                } else {
                    convenientBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorResponse(String ex) {

            }
        });
    }

    @Override
    public void initInfo(String json) {
        payListBeanList.clear();
        iconList.clear();
        palChannelBeanList.clear();
        activity_recharge_instructions.removeAllViews();
        vipPayBeen = gson.fromJson(json, VipPayBeen.class);
        VipPayBeen.UserBean userBean = vipPayBeen.getUser();
        if (vipPayBeen.getPrivilege() != null && !vipPayBeen.getPrivilege().isEmpty()) {
            iconList.addAll(vipPayBeen.getPrivilege());
            rechargePrivilegeAdapter.notifyDataSetChanged();
        }
        if (UserUtils.isLogin(activity)) {
            is_vip = userBean.getBaoyue_status();
            activityUserName.setText(userBean.getNickname());
            if (UserUtils.isLogin(activity)) {
                if (userBean.avatar != null && !userBean.getAvatar().isEmpty()) {
                    MyGlide.GlideImageHeadNoSize(activity, userBean.getAvatar(), activityUserImg);
                } else {
                    activityUserImg.setImageResource(R.mipmap.hold_user_avatar);
                }
            } else {
                activityUserImg.setImageResource(R.mipmap.hold_user_avatar);
            }
            if (is_vip == 0) {
                acitivityVipImg.setImageResource(R.mipmap.icon_novip);
                activityVipTime.setText(userBean.getVip_desc());
            } else {
                acitivityVipImg.setImageResource(R.mipmap.icon_isvip);
                activityVipTime.setText(userBean.getExpiry_date());
            }
        } else {
            activityUserImg.setImageResource(R.mipmap.hold_user_avatar);
            activityUserName.setText(LanguageUtil.getString(activity, R.string.MineNewFragment_nologin));
            activityVipTime.setText(userBean.getVip_desc());
        }
        payListBeanList.addAll(vipPayBeen.getList());
        if (payListBeanList != null && !payListBeanList.isEmpty()) {
            payListBeanList.get(0).choose = true;
            mGoodsId = payListBeanList.get(0).goods_id + "";
            payHeadBarAdapter.notifyDataSetChanged();
            current_price = vipPayBeen.getList().get(0).getFat_price();
            pay_recharge_tv.setText(current_price);
            if (vipPayBeen.getList().get(0).pal_channel != null && !vipPayBeen.getList().get(0).pal_channel.isEmpty()) {
                palChannelBeanList.addAll(vipPayBeen.getList().get(0).pal_channel);
                palChannelBean = palChannelBeanList.get(0);
                palChannelBean.choose = true;
                rechargeChannelAdapter.notifyDataSetChanged();
            }
            rechargePrivilegeAdapter.notifyDataSetChanged();
        }
        RechargeGoldFragment.setRechargeInfo(activity,vipPayBeen.getAbout(), activity_recharge_instructions);
    }

    /**
     * 用户支付后刷新界面
     * @param toStore
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshPay(RefreshMine toStore) {
        if (toStore.isPayVip) {
            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.PayActivity_zhifuok));
            // 刷新用户状态
            new UpdateApp(BWNApplication.applicationContext.getActivity()).getRequestData(false, null);
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
