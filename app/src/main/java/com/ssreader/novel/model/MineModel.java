package com.ssreader.novel.model;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.dialog.BottomMenuDialog;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.cache.ClearCacheManager;
import com.ssreader.novel.ui.activity.AboutUsActivity;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.ui.activity.FeedBackActivity;
import com.ssreader.novel.ui.activity.NewRechargeActivity;
import com.ssreader.novel.ui.activity.SettingActivity;
import com.ssreader.novel.ui.activity.TaskCenterActivity;
import com.ssreader.novel.ui.activity.WebViewActivity;
import com.ssreader.novel.ui.dialog.PublicDialog;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ScreenSizeUtils;

import static com.ssreader.novel.constant.Constant.DOWN;
import static com.ssreader.novel.constant.Constant.MYCOMMENT;
import static com.ssreader.novel.constant.Constant.READHISTORY;
import static com.ssreader.novel.constant.Constant.REWARDHISTORY;
import static com.ssreader.novel.constant.Constant.getCurrencyUnit;

public class MineModel {

    private int iconResources;
    private int is_click;
    public String icon, title, desc, action, content;
    private String title_color, desc_color;
    private boolean isBottomLine;
    private Intent intent;

    public MineModel(boolean isBottomLine, String title, String desc, String title_color, String desc_color, String action, int is_click) {
        this.isBottomLine = isBottomLine;
        this.title = title;
        this.desc = desc;
        this.title_color = title_color;
        this.desc_color = desc_color;
        this.action = action;
        this.is_click = is_click;
    }

    public MineModel(boolean isBottomLine, String icon, String title, String desc, String title_color, String desc_color, String action, String content, int is_click) {
        this.isBottomLine = isBottomLine;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.title_color = title_color;
        this.desc_color = desc_color;
        this.action = action;
        this.is_click = is_click;
        this.content = content;
    }

    public MineModel(boolean isBottomLine, int iconResources, String title, String desc, String title_color, String desc_color, String action, int is_click) {
        this.isBottomLine = isBottomLine;
        this.iconResources = iconResources;
        this.title = title;
        this.desc = desc;
        this.title_color = title_color;
        this.desc_color = desc_color;
        this.action = action;
        this.is_click = is_click;
    }

    public int getIconResources() {
        return iconResources;
    }

    public void setIconResources(int iconResources) {
        this.iconResources = iconResources;
    }

    public int getIs_click() {
        return is_click;
    }

    public void setIs_click(int is_click) {
        this.is_click = is_click;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle_color() {
        return title_color;
    }

    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }

    public String getDesc_color() {
        return desc_color;
    }

    public void setDesc_color(String desc_color) {
        this.desc_color = desc_color;
    }

    public boolean isBottomLine() {
        return isBottomLine;
    }

    public void setBottomLine(boolean bottomLine) {
        isBottomLine = bottomLine;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void mineOption(Activity activity) {
        switch (action) {
            case "recharge":
                activity.startActivity(new Intent(activity, NewRechargeActivity.class)
                        .putExtra("RechargeTitle", getCurrencyUnit(activity) + LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi))
                        .putExtra("RechargeRightTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_chongzhijilu))
                        .putExtra("RechargeType", "gold"));
                break;
            case "vip":
                activity.startActivity(new Intent(activity, NewRechargeActivity.class)
                        .putExtra("RechargeTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_title))
                        .putExtra("RechargeType", "vip"));
                break;
            case "task":
                activity.startActivity(new Intent(activity, TaskCenterActivity.class));
                break;
            case "invite":
                new MyShare(activity).ShareAPP();
                break;
            case "history":
                activity.startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.noverfragment_yuedulishi))
                        .putExtra("OPTION", READHISTORY));
                break;
            case "download":
                activity.startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.BookInfoActivity_down_manger))
                        .putExtra("OPTION", DOWN));
                break;
            case "comment":
                activity.startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.MineNewFragment_shuping))
                        .putExtra("OPTION", MYCOMMENT));
                break;
            case "service":
            case "service_local":
                activity.startActivity(new Intent(activity, AboutUsActivity.class));
                break;
            case "feedback":
                activity.startActivity(new Intent(activity, FeedBackActivity.class));
                break;
            case "setting":
                activity.startActivity(new Intent(activity, SettingActivity.class));
                break;
            case "reward":
                activity.startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("OPTION", REWARDHISTORY)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.Activity_reward_history)));
                break;
            case "url":
                if (content != null && !content.isEmpty()) {
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("url", content);
                    intent.putExtra("title", title);
                    activity.startActivity(intent);
                }
                break;
            case "share_local":
                // 好评支持
                SettingActivity.support(activity);
                break;
            case "clean_local":
                PublicDialog.publicDialogVoid(activity, "",
                        LanguageUtil.getString(activity, R.string.SettingsActivity_cleanhuancun),
                        LanguageUtil.getString(activity, R.string.app_cancle),
                        LanguageUtil.getString(activity, R.string.app_confirm), new PublicDialog.OnPublicListener() {
                            @Override
                            public void onClickConfirm(boolean isConfirm) {
                                if (isConfirm) {
                                    FileManager.deleteFile(FileManager.getSDCardRoot());
                                    ClearCacheManager.clearAllCache(activity);
                                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.SettingsActivity_clearSeccess));
                                }
                            }
                        });
                break;
            case "feedback_local":
                activity.startActivity(new Intent(activity, com.ssreader.novel.ui.localshell.localapp.FeedBackActivity.class));
                break;
        }
    }

    public void aboutIntent(Activity activity) {
        switch (action) {
            case "email":
            case "qq":
            case "wechat":
                ScreenSizeUtils.getInstance(activity).copy(activity, desc);
                break;
            case "url":
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("flag", "yinsi");
                intent.putExtra("title", title);
                intent.putExtra("url", desc);
                activity.startActivity(intent);
                break;
            case "telphone":
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    new BottomMenuDialog().showBottomMenuDialog(activity, new String[]{desc}, new SCOnItemClickListener() {
                        @Override
                        public void OnItemClickListener(int flag, int position, Object O) {
                            Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + desc));
                            activity.startActivity(intentPhone);
                        }

                        @Override
                        public void OnItemLongClickListener(int flag, int position, Object O) {

                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 10086);
                }
                break;
        }
    }
}
