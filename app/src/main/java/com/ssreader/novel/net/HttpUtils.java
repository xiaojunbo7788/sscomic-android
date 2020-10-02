package com.ssreader.novel.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.ui.activity.NewRechargeActivity;
import com.ssreader.novel.ui.activity.SettingActivity;
import com.ssreader.novel.ui.dialog.VoteTipsPopWindow;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.InternetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;

import okhttp3.Request;

import static com.ssreader.novel.constant.Constant.getCurrencyUnit;

public class HttpUtils {

    private static HttpUtils httpUtils;
    private static Gson gson;
    private Activity activity;
    private String flagString;

    public HttpUtils() {

    }

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    /**
     * 发送请求
     *
     * @param url
     * @param body
     * @param responseListener
     */
    public void sendRequestRequestParams(Activity activity, final String url, final String body, final ResponseListener responseListener) {

        sendRequestRequestParams(activity, url, body, "", responseListener);
    }

    public void sendRequestRequestParams(Activity activity, final String url, final String body, final String flagString, final ResponseListener responseListener) {
        this.activity = activity;
        this.flagString = flagString;
        if ((activity == null || activity.isFinishing()) && responseListener != null) {
            responseListener.onErrorResponse("");
            return;
        }
        if (InternetUtils.internet(activity)) {
            if (url != null) {
                MyToash.Log("bwhttp ====> request", Constant.BASE_URL + url + " \n  " + body);
            }
            OkHttp3.getInstance(activity).postAsyncHttp(url, body, new ResultCallback() {
                @Override
                public void onFailure(Request request, Exception e) {
                    if (responseListener != null && activity != null && !activity.isFinishing()) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseListener.onErrorResponse(e.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onResponse(final String result) {
                    if (responseListener != null) {
                        handleData(false,url, activity, result, responseListener);
                    }
                }
            });
        } else {
            if (responseListener != null) {
                responseListener.onErrorResponse("no_net");
            }
        }
    }

    public void downloadText(final String url, Context activity, final OnDownloadListenerText listener) {
        OkHttp3.getInstance(activity).getAsyncHttp(url, new ResultCallback() {
            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(String s) {
                if (!TextUtils.isEmpty(s)) {
                    listener.onDownloadSuccess(s);
                }
            }
        });
    }

    public void handleData(boolean subThread,String url, Activity activity, String result, ResponseListener responseListener) {
        MyToash.Log("bwhttp ====> response: url =",url+ " result =" + result);
        if (activity != null && !activity.isFinishing()) {
            handJsonData(subThread, result, responseListener, activity);
        }
    }

    private void handJsonData(boolean subThread, String result, ResponseListener responseListener, Activity activity) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            int code = jsonObj.getInt("code");
            String msg = jsonObj.getString("msg");
            if (code == 0) {
                if (subThread) {
                    responseListener.onResponse(jsonObj.getString("data"));
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseListener.onResponse(jsonObj.getString("data"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            switch (code) {
                                case 0:
                                    // 成功
                                    responseListener.onResponse(jsonObj.getString("data"));
                                    break;
                                case 315:
                                    MyToash.ToashSuccess(HttpUtils.this.activity, msg);
                                    responseListener.onResponse(jsonObj.getString("data"));
                                    break;
                                case 802://余额不足 充值
                                    if (flagString != null && flagString.equals("oneBuy")) {
                                        activity.startActivity(new Intent(HttpUtils.this.activity, NewRechargeActivity.class)
                                                .putExtra("RechargeTitle", getCurrencyUnit(HttpUtils.this.activity) + LanguageUtil.getString(HttpUtils.this.activity, R.string.MineNewFragment_chongzhi))
                                                .putExtra("RechargeRightTitle", LanguageUtil.getString(HttpUtils.this.activity, R.string.BaoyueActivity_chongzhijilu))
                                                .putExtra("RechargeType", "gold"));
                                      /*  MyToash.setDelayedHandle(1000, new MyToash.DelayedHandle() {
                                            @Override
                                            public void handle() {
                                                MyToash.ToashError(BWNApplication.applicationContext.getActivity(), msg);
                                            }
                                        });*/
                                    }
                                    responseListener.onErrorResponse("802");
                                    break;
                                case 750://下架或者不存在
                                case 1301://未认证
                                    MyToash.ToashError(HttpUtils.this.activity, msg);
                                    responseListener.onErrorResponse(code + "");
                                    break;
                                case 1302://开通vip
                                    responseListener.onErrorResponse("1302");
                                    break;
                                case 301: // 登录已过期
                                    responseListener.onErrorResponse(msg);
                                    break;
                                case 302: // 用户封禁
                                    MyToash.ToashError(activity, msg);
                                    responseListener.onErrorResponse(msg);
                                    SettingActivity.exitUser(activity);
                                    break;
                                case 701://余额不足 充值
                                case 602:
                                case 311:
                                case 304:
                                case 107:
                                case 704://重复订阅
                                    responseListener.onErrorResponse(code + "");
                                    break;
                                case 902:
                                    new VoteTipsPopWindow(activity, jsonObj.getString("data"));
                                    break;
                                default:
                                    MyToash.ToashError(HttpUtils.this.activity, msg);
                                    responseListener.onErrorResponse("null");
                                    break;
                            }
                        } catch (JSONException s) {
                        }
                    }
                });
            }
        } catch (JSONException json) {
        }
    }


    public void sendRequestRequestParamsSubThread(Activity activity, final String url, final String body, final ResponseListener responseListener) {
        this.activity = activity;
        if ((activity == null || activity.isFinishing()) && responseListener != null) {
            responseListener.onErrorResponse("");
            return;
        }
        if (InternetUtils.internet(activity)) {
            MyToash.Log("bwhttp === > request", Constant.BASE_URL + url + " \n  " + body);
            OkHttp3.getInstance(activity).postAsyncHttp(url, body, new ResultCallback() {
                @Override
                public void onFailure(Request request, Exception e) {
                    if (responseListener != null && activity != null && !activity.isFinishing()) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseListener.onErrorResponse(e.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onResponse(final String result) {
                    if (responseListener != null) {
                        handleData(true,url, activity, result, responseListener);
                    }
                }
            });
        } else {
            if (responseListener != null) {
                responseListener.onErrorResponse("no_net");
            }
        }
    }

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            gson = builder.create();
        }
        return gson;
    }

    public interface OnDownloadListenerText {

        void onDownloadSuccess(String file);
    }

    public interface ResponseListener {

        void onResponse(String response);

        void onErrorResponse(String ex);
    }
}


