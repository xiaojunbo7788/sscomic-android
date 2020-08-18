package com.ssreader.novel.net;

import android.content.Context;

import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.utils.UserUtils;
import com.ssreader.novel.ui.utils.MyToash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReaderParams {

    private final String TAG = ReaderParams.class.getSimpleName();

    private List<String> paramList = new ArrayList<>();
    private ReaderNameValuePair nameValuePair = new ReaderNameValuePair();

    public void destroy() {
        paramList.clear();
        nameValuePair.destroy();
        paramList = null;
    }

    /**
     * 公共参数
     * @param context
     */
    public ReaderParams(Context context) {
        String appId = "";
        String osType = UserUtils.getOsType();
        String product = UserUtils.getProduct();
        String sysVer = UserUtils.getSystemVersion();
        String time = System.currentTimeMillis() / 1000 + "";
        String token = UserUtils.getToken(context);
        String udid = UserUtils.getUUID(context);
        String ver = UserUtils.getAppVersionName(context);

        paramList.add("appId=" + appId);
        paramList.add("osType=" + osType);
        paramList.add("product=" + product);
        paramList.add("sysVer=" + sysVer);
        paramList.add("time=" + time);
        paramList.add("token=" + token);
        paramList.add("udid=" + udid);
        paramList.add("ver=" + ver);

        String marketChannel = UserUtils.getChannelName(context);
        paramList.add("marketChannel=" + marketChannel);
        paramList.add("packageName=" + BuildConfig.APPLICATION_ID);

        nameValuePair.put("appId", appId);
        nameValuePair.put("osType", osType);
        nameValuePair.put("product", product);
        nameValuePair.put("sysVer", sysVer);
        nameValuePair.put("time", time);
        nameValuePair.put("token", token);
        nameValuePair.put("udid", udid);
        nameValuePair.put("ver", ver);
        nameValuePair.put("packageName", BuildConfig.APPLICATION_ID);
        nameValuePair.put("marketChannel", marketChannel);

    }

    /**
     * 额外参数另行添加
     *
     * @param key
     * @param value
     */
    public void putExtraParams(String key, String value) {
        paramList.add(key + "=" + value);
        nameValuePair.put(key, value);
    }

    public void putExtraParams(String key, long value) {
        paramList.add(key + "=" + value);
        nameValuePair.put(key, value + "");
    }

    public void removeExtraParams(String key, String value) {
        paramList.remove(key + "=" + value);
        nameValuePair.remove(key, value);
    }

    public void removeExtraParams(String key, int value) {
        paramList.remove(key + "=" + value);
        nameValuePair.remove(key, value + "");
    }

    public void putExtraParams(String key, int value) {
        paramList.add(key + "=" + value);
        nameValuePair.put(key, value + "");
    }

    /**
     * 生成最终的json参数
     *
     * @return
     */
    public String generateParamsJson() {
        String sortedParamString = getSortedParams(paramList);
        String sign = UserUtils.MD5(sortedParamString);
        nameValuePair.put("sign", sign);
        String json = nameValuePair.toJson();
        return json;
    }

    /**
     * 参数按照字典顺讯排序
     *
     * @param list
     * @return
     */
    public String getSortedParams(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.mAppkey);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                stringBuilder.append(list.get(i));
            } else {
                stringBuilder.append("&").append(list.get(i));
            }
        }
        stringBuilder.append(Constant.mAppSecretKey);
        MyToash.Log(TAG, "getSortedParams:" + stringBuilder.toString());
        return stringBuilder.toString();
    }
}
