package com.ssreader.novel.pay.wxpay;

import android.app.Activity;
import android.view.View;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ssreader.novel.pay.ReaderPay;
import com.ssreader.novel.ui.utils.MyToash;

import org.json.JSONObject;

/**
 * Created by  on 2018/8/12.
 */
public class WXGoPay extends ReaderPay {

    private Activity mActivity;

    public WXGoPay(Activity context, View view) {
        super(context, view);
        mActivity = context;
    }

    @Override
    public void startPay(String prepayId) {
        try {
            JSONObject jsonObject = new JSONObject(prepayId);
            IWXAPI msgApi = WXAPIFactory.createWXAPI(mActivity, jsonObject.getString("appid"), false);
            msgApi.registerApp(jsonObject.getString("appid"));
            PayReq req = new PayReq();
            req.appId = jsonObject.getString("appid");
            req.partnerId = jsonObject.getString("partnerid");
            req.packageValue = jsonObject.getString("package");
            req.prepayId = jsonObject.getString("prepayid");
            req.nonceStr = jsonObject.getString("noncestr");
            req.timeStamp = jsonObject.getString("timestamp");
            req.sign = jsonObject.getString("paySign");
            msgApi.sendReq(req);
            if (ClickView != null) {
                MyToash.setDelayedHandle(3000, new MyToash.DelayedHandle() {
                    @Override
                    public void handle() {
                        ClickView.setClickable(true);
                    }
                });
            }
        } catch (Exception E) {
        }
    }

    @Override
    public void handleOrderInfo(String result) {
        startPay(result);
    }
}
