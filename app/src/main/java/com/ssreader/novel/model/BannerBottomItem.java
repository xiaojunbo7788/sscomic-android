package com.ssreader.novel.model;

import android.app.Activity;
import android.content.Intent;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.utils.LanguageUtil;

import static com.ssreader.novel.constant.Constant.BAOYUE;
import static com.ssreader.novel.constant.Constant.MIANFEI;
import static com.ssreader.novel.constant.Constant.PAIHANGINSEX;
import static com.ssreader.novel.constant.Constant.SHUKU;
import static com.ssreader.novel.constant.Constant.WANBEN;

/**
 * 书城tab
 */
public class BannerBottomItem {

    private String title, icon, action;

    private int share_icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getShare_icon() {
        return share_icon;
    }

    public void setShare_icon(int share_icon) {
        this.share_icon = share_icon;
    }

    public BannerBottomItem(String title, int share_icon, String action) {
        this.title = title;
        this.share_icon = share_icon;
        this.action = action;
    }

    public void intentOption(Activity activity,int productType){
        Intent intent = new Intent(activity, BaseOptionActivity.class);
        intent.putExtra("productType", productType);
        intent.putExtra("title", title);
        switch (action) {
            case "free":
                intent.putExtra("OPTION", MIANFEI);
                break;
            case "finished":
                intent.putExtra("OPTION", WANBEN);
                break;
            case "category":
                intent.putExtra("OPTION", SHUKU);
                break;
            case "rank":
                intent.putExtra("OPTION", PAIHANGINSEX);
                break;
            case "vip":
            default:
                intent.putExtra("OPTION", BAOYUE);
                break;
        }
       activity.startActivity(intent);
    }
}
