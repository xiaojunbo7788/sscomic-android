package com.ssreader.novel.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MeizuRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.alibaba.sdk.android.push.register.VivoRegister;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AdStatusRefresh;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UpdateApp {

    private Activity activity;

    public UpdateApp(Activity activity) {
        this.activity = activity;
    }

    public interface UpdateAppInterface {

        void Next(String str, AppUpdate dataBean);
    }

    public void getRequestData(boolean isnext, final UpdateAppInterface updateAppInterface) {
        ReaderParams readerParams = new ReaderParams(activity);

        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.check_setting, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        ShareUitls.putString(activity, "Update", response);
                        GetCheckVer(response, isnext, updateAppInterface);
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        //String response = ShareUitls.getString(activity, "Update", "");
                        //GetCheckVer(response, isnext, updateAppInterface);
                    }
                }
        );
    }

    private void GetCheckVer(String response, boolean isnext, UpdateAppInterface updateAppInterface) {
        AppUpdate dataBean = null;
        try {
            dataBean = HttpUtils.getInstance().getGson().fromJson(response, AppUpdate.class);
            if (dataBean != null) {
                AppUpdate.AdStatusSettingBean adStatusSettingBean = dataBean.ad_status_setting;
                if (adStatusSettingBean != null) {
                    ShareUitls.putBoolean(activity, "USE_AD_READBUTTOM", adStatusSettingBean.getChapter_read_bottom() == 1);
                    ShareUitls.putBoolean(activity, "USE_AD_READCENDET", adStatusSettingBean.getChapter_read_end() == 1);
                    ShareUitls.putBoolean(activity, "USE_AD_READCENDET_COMIC", adStatusSettingBean.getComic_read_end() == 1);
                    ShareUitls.putBoolean(activity, "USE_AD_VIDEO", adStatusSettingBean.getVideo_ad_switch() == 1);
                    ShareUitls.putString(activity, "USE_AD_VIDEO_TEXT", adStatusSettingBean.getVideo_ad_text());
                    ShareUitls.putInt(activity, "USE_AD_VIDEO_TIME", adStatusSettingBean.getAd_free_time() * 60);
                    // 刷新阅读器广告状态
                    EventBus.getDefault().post(new AdStatusRefresh());
                }
                if (dataBean.getProtocol_list() != null) {
                    AppUpdate.ProtocolBean protocolBean = dataBean.getProtocol_list();
                    // 协议
                    ShareUitls.putString(activity, "app_notify", protocolBean.getNotify());
                    ShareUitls.putString(activity, "app_privacy", protocolBean.getPrivacy());
                    ShareUitls.putString(activity, "app_logoff", protocolBean.getLogoff());
                    ShareUitls.putString(activity, "app_user", protocolBean.getUser());
                    ShareUitls.putString(activity, "app_vip_service", protocolBean.getVip_service());
                }
            }
            if (isnext) {
                next(dataBean, response, updateAppInterface);
            } else {
                if (updateAppInterface != null) {
                    updateAppInterface.Next(response, dataBean);
                }
            }
        } catch (Exception e) {
            if (updateAppInterface != null) {
                updateAppInterface.Next(response, dataBean);
            }
        }
    }

    private void next(AppUpdate dataBean, String response, UpdateAppInterface updateAppInterface) {
        if (dataBean != null) {
            ShareUitls.setDataList(activity, "web_view_urlist", dataBean.getWeb_view_urlist());
            ShareUitls.putString(activity, "web_view_url", dataBean.web_view_url);
            AppUpdate.SystemSettingBean systemSettingBean = dataBean.system_setting;
            if (systemSettingBean != null) {
                if (dataBean.system_setting.getCheck_status() == 1) {
                    // 笔记详情
                    ShareUitls.putBoolean(activity, "USE_AD_READBUTTOM", false);
                    ShareUitls.putBoolean(activity, "USE_AD_READCENDET", false);
                    ShareUitls.putBoolean(activity, "USE_AD_READCENDET_COMIC", false);
                }
                // 设置站点属性
                Constant.setRewardSwitch(activity, systemSettingBean.novel_reward_switch);
                Constant.setMonthlyTicket(activity, systemSettingBean.monthly_ticket_switch);
                Constant.setProductTypeList(activity, systemSettingBean.getSite_type());
                Constant.USE_AUDIO_AI = dataBean.system_setting.getAi_switch() == 1;
                Constant.currencyUnit = dataBean.system_setting.getCurrencyUnit();
                Constant.subUnit = dataBean.system_setting.getSubUnit();
                ShareUitls.putString(activity, "currencyUnit", Constant.currencyUnit);
                ShareUitls.putString(activity, "subUnit", Constant.subUnit);
            }
            try {
                List<AppUpdate.PushPassagewayBean> push_passageway = dataBean.push_passageway;
                if (push_passageway != null && !push_passageway.isEmpty()) {
                    for (AppUpdate.PushPassagewayBean pushPassagewayBean : push_passageway) {
                        if (pushPassagewayBean.getP_key().equals("xiaomi") && !TextUtils.isEmpty(pushPassagewayBean.getApp_id()) && !TextUtils.isEmpty(pushPassagewayBean.getApp_key())) {
                            MiPushRegister.register(BWNApplication.applicationContext, pushPassagewayBean.getApp_id(), pushPassagewayBean.getApp_key()); // 初始化小米辅助推送
                        }
                        if (pushPassagewayBean.getP_key().equals("oppo") && !TextUtils.isEmpty(pushPassagewayBean.getApp_key()) && !TextUtils.isEmpty(pushPassagewayBean.getApp_secret())) {
                            OppoRegister.register(BWNApplication.applicationContext, pushPassagewayBean.getApp_key(), pushPassagewayBean.getApp_secret());
                        }
                        if (pushPassagewayBean.getP_key().equals("vivo")) {
                            VivoRegister.register(BWNApplication.applicationContext);
                        }
                        if (pushPassagewayBean.getP_key().equals("huawei")) {
                            HuaWeiRegister.register(BWNApplication.applicationContext); // 接入华为辅助推送
                        }
                        if (pushPassagewayBean.getP_key().equals("meizu") && !TextUtils.isEmpty(pushPassagewayBean.getApp_id()) && !TextUtils.isEmpty(pushPassagewayBean.getApp_key())) {
                            MeizuRegister.register(BWNApplication.applicationContext, pushPassagewayBean.getApp_id(), pushPassagewayBean.getApp_key()); // appId/appkey在魅族开发者平台获取
                        }
                    }
                }
            } catch (Throwable e) {
            }
        }
        if (updateAppInterface != null) {
            updateAppInterface.Next(response, dataBean);
        }
    }
}
