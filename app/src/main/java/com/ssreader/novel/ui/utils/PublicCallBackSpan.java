package com.ssreader.novel.ui.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.activity.FeedBakcPostActivity;
import com.ssreader.novel.ui.activity.WebViewActivity;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;

import static com.ssreader.novel.constant.Constant.BASE_URL;

public class PublicCallBackSpan extends ClickableSpan {

    // 软件协议
    private final String NOTIFY = BASE_URL + "/site/notify";
    // 隐私协议
    private final String PRIVACY = BASE_URL + "/site/privacy-policy";
    // 用户协议
    public static final String USER = BASE_URL + "/site/user-agreement";
    // vip服务协议
    private final String VIP_SERVICE = BASE_URL + "/site/membership-service";
    // 注销协议
    public static final String LOGOFF = BASE_URL + "/site/logoff-protocol";

    private Activity activity;
    public boolean isSplashYinsi;
    private int flag; //判断跳转Activity  1.用户协议 2.隐私协议 3.意见反馈 4.会员服务协议

    public PublicCallBackSpan(Activity activity, int flag) {
        this.activity = activity;
        this.flag = flag;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
      /*  ds.setColor(Color.RED);
        ds.clearShadowLayer();*/
    }
    @Override
    public void onClick(@NonNull View view) {
        Intent intent = new Intent();
        switch (flag) {
            case 1:
                intent.putExtra("title", LanguageUtil.getString(activity, R.string.AboutActivity_title));
                intent.putExtra("url", ShareUitls.getString(activity, "app_notify", NOTIFY));
                intent.putExtra("flag", isSplashYinsi ? "yinsi" : null);
                intent.setClass(activity, WebViewActivity.class);
                break;
            case 2:
                intent.putExtra("title", LanguageUtil.getString(activity, R.string.AboutActivity_PRIVACY));
                intent.putExtra("url", ShareUitls.getString(activity, "app_privacy", PRIVACY));
                intent.putExtra("flag", isSplashYinsi ? "yinsi" : null);
                intent.setClass(activity, WebViewActivity.class);
                break;
            case 3:
                intent.setClass(activity, FeedBakcPostActivity.class);
                break;
            case 4:
                intent.putExtra("title", LanguageUtil.getString(activity, R.string.AboutActivity_VIPFUWUXIEYI));
                intent.putExtra("url", ShareUitls.getString(activity, "app_vip_service", VIP_SERVICE));
                intent.putExtra("flag", isSplashYinsi ? "yinsi" : null);
                intent.setClass(activity, WebViewActivity.class);
                break;
            case 5:
                intent.putExtra("url", ShareUitls.getString(activity, "app_user", USER));
                intent.putExtra("title", LanguageUtil.getString(activity, R.string.AboutActivity_xieyi));
                intent.setClass(activity, WebViewActivity.class);
                break;
        }
        activity.startActivity(intent);
    }
}
