package com.ssreader.novel.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.ssreader.novel.R;

import com.ssreader.novel.eventbus.RefreshMine;

import com.ssreader.novel.model.UserInfoItem;
import com.ssreader.novel.pay.ReaderPay;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 阿里支付
 */
public class AlipayGoPay extends ReaderPay {

    private Activity activity;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    public AlipayGoPay(Activity context, View view) {
        super(context, view);
        activity = context;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            if (ClickView != null) {
                ClickView.setClickable(true);
            }
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    //MyToash.Log("resultStatus",resultStatus+"");
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.PayActivity_zhifuok));
                        EventBus.getDefault().post(new RefreshMine(true));
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.PayActivity_zhifucuowu));
                    }if (TextUtils.equals(resultStatus, "6001")) {
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.PayActivity_zhifucancle));
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        //  Utils.showToast("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        //  Utils.showToast("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void startPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                //   Utils.printLog(TAG, "支付宝支付结果：" + result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void handleOrderInfo(String result) {
        JSONObject object = null;
        try {
            object = new JSONObject(result);
            String orderInfo = object.getString("order_str");
            startPay(orderInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
