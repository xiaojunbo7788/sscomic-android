package com.ssreader.novel.pay;

/**
 * Created by  on 2018/8/12.
 */
public interface GoPay {
    void handleOrderInfo(String result);

    void startPay(String orderInfo);
}
