package com.ssreader.novel.pay;

import android.app.Activity;
import android.view.View;

import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;

public abstract class ReaderPay implements GoPay {

    public Activity context;
    protected View ClickView;

    public ReaderPay(Activity context, View ClickView) {
        this.context = context;
        this.ClickView = ClickView;
    }

    public void requestPayOrder(String url, String goodsId) {
        ClickView.setClickable(false);
        ReaderParams params = new ReaderParams(context);
        params.putExtraParams("goods_id", goodsId);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(context,url, json,  new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        handleOrderInfo(result);
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (ClickView != null) {
                            ClickView.setClickable(true);
                        }
                    }
                }
        );
    }

    public abstract void startPay(String orderInfo);

    public abstract void handleOrderInfo(String result);
}
