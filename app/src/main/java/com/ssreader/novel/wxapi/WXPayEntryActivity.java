package com.ssreader.novel.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ssreader.novel.R;

import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshRecharge;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信支付结果显示
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_PAY_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        MyToash.Log("onResp", resp.getType() + "  " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                EventBus.getDefault().post(new RefreshMine(true));
                WXPayEntryActivity.this.finish();
            } else if (resp.errCode == -1) {
                MyToash.ToashError(WXPayEntryActivity.this, LanguageUtil.getString(WXPayEntryActivity.this, R.string.PayActivity_zhifucuowu));
                MyToash.setDelayedFinash(this);
            } else if (resp.errCode == -2) {
                finish();
                EventBus.getDefault().post(new RefreshRecharge(resp.errCode));
            } else {
                MyToash.setDelayedFinash(this);
            }
        }
    }
}