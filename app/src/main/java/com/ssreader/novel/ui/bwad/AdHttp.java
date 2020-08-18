package com.ssreader.novel.ui.bwad;

import android.app.Activity;
import android.text.TextUtils;

import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;

public class AdHttp {

    public interface GetBaseAd {
        void getBaseAd(BaseAd baseAd);
    }

    public interface AdClick {
        void adClick(String result);
    }

    /**
     * 获取广告
     *
     * @param activity
     * @param PRODUCT
     * @param position
     * @param getBaseAd
     */
    public static void getWebViewAD(Activity activity, int PRODUCT, int position, GetBaseAd getBaseAd) {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("type", PRODUCT + "");
        params.putExtraParams("position", position + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.ADVERT_INFO, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        if (!TextUtils.isEmpty(result)) {
                            BaseAd baseAd = HttpUtils.getGson().fromJson(result, BaseAd.class);
                            if (getBaseAd != null) {
                                getBaseAd.getBaseAd(baseAd);
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (getBaseAd != null) {
                            getBaseAd.getBaseAd(null);
                        }
                    }
                }
        );
    }

    public static void adClick(Activity activity, BaseAd baseAd, int position, AdClick adClick) {
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("advert_id", baseAd.advert_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.ACTOR_ADVERT_CLICK, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                if (adClick != null) {
                    adClick.adClick(response);
                }
            }

            @Override
            public void onErrorResponse(String ex) {
            }
        });
    }

}
