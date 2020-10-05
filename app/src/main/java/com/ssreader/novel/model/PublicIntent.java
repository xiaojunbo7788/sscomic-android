package com.ssreader.novel.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ssreader.novel.R;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.ui.activity.BookInfoActivity;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.activity.FeedBackActivity;
import com.ssreader.novel.ui.activity.InviteActivity;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.ui.activity.NewRechargeActivity;
import com.ssreader.novel.ui.activity.SettingActivity;
import com.ssreader.novel.ui.activity.TaskCenterActivity;
import com.ssreader.novel.ui.activity.WebViewActivity;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.RegularUtlis;

import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.BAOYUE;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.getCurrencyUnit;
import static com.ssreader.novel.utils.RegularUtlis.isNumber;

public class PublicIntent {

    public int skip_type;
    public int action;
    public String content;
    public String image;
    public String color;
    public String title;
    public String desc;
    public String ad_android_key;
    // 自己设置，用于区分banner的类型（productType）
    public int actionBanner;
    public static long ClickTime;

    public void intentTo(Activity activity) {
        intentTo(activity, true, skip_type, content, title);
    }

    public static Intent intentTo(Context activity, boolean isInto, int skip_type, String content, String title) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            Intent intent = new Intent();
            long id = RegularUtlis.isNumber(content) ? Long.parseLong(content) : 0;
            switch (skip_type) {
                case 1:
                    intent.setClass(activity, BookInfoActivity.class);
                    intent.putExtra("book_id", id);
                    break;
                case 2:
                    intent.setClass(activity, WebViewActivity.class);
                    intent.putExtra("url", content);
                    intent.putExtra("title", title);
                    break;
                case 3:
                    intent.setClass(activity, ComicInfoActivity.class);
                    intent.putExtra("comic_id", id);
                    break;
                case 4:
                    intent.setClass(activity, WebViewActivity.class);
                    intent.putExtra("url", content);
                    intent.putExtra("is_otherBrowser", true);
                    intent.putExtra("title", title);
                    break;
                case 8:
                    intent.setClass(activity, AudioInfoActivity.class);
                    intent.putExtra("audio_id", id);
                    break;
                default:
                    intent.setClass(activity, MainActivity.class);
                    break;
            }
            if (isInto) {
                activity.startActivity(intent);
                MyToash.setDelayedFinash((Activity) activity);
            }
            return intent;
        }
        return null;
    }

    public void intentBannerTo(Activity activity) {
        Intent intent = new Intent();
        switch (action) {
            case 1:
                if (!TextUtils.isEmpty(content)) {
                    if (isNumber(content)) {
                        if (actionBanner == Constant.BOOK_CONSTANT) {
                            intent.setClass(activity, BookInfoActivity.class);
                            intent.putExtra("book_id", new Long(content));
                        } else if (actionBanner == COMIC_CONSTANT) {
                            intent.setClass(activity, ComicInfoActivity.class);
                            intent.putExtra("comic_id", new Long(content));
                        } else if (actionBanner == AUDIO_CONSTANT) {
                            intent.setClass(activity, AudioInfoActivity.class);
                            intent.putExtra("audio_id", new Long(content));
                        }
                    } else return;
                } else return;
                break;
            case 2:
                switch (getContent()) {
                    case "sign":
                    case "task":
                        intent.setClass(activity, TaskCenterActivity.class);
                        break;
                    case "recharge":
                        intent.putExtra("RechargeTitle", getCurrencyUnit(activity) + LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi));
                        intent.putExtra("RechargeRightTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_chongzhijilu));
                        intent.putExtra("RechargeType", "gold");
                        intent.setClass(activity, NewRechargeActivity.class);
                        break;
                    case "feedback":
                        intent.setClass(activity, FeedBackActivity.class);
                        break;
                    case "setting":
                        intent.setClass(activity, SettingActivity.class);
                        break;
                    case "vip":
                        intent.setClass(activity, BaseOptionActivity.class);
                        intent.putExtra("OPTION", BAOYUE);
                        intent.putExtra("productType", actionBanner);
                        intent.putExtra("title", LanguageUtil.getString(activity, R.string.BaoyueActivity_center));
                        break;
                    case "login":
                        intent.setClass(activity, LoginActivity.class);
                        break;
                    case "invite":
                        intent.setClass(activity, InviteActivity.class);
                        break;
                }
                break;
            case 3:
                intent.setClass(activity, WebViewActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("title", title);
                break;
            case 9:
                return;
            default:
                intent.setClass(activity, WebViewActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("is_otherBrowser", true);
                break;
        }
        activity.startActivity(intent);
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PublicIntent{" +
                "skip_type=" + skip_type +
                ", action=" + action +
                ", actionBanner=" + actionBanner +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", color='" + color + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
