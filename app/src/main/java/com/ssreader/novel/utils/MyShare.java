package com.ssreader.novel.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.ShareBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.dialog.ShareDialogFragment;
import com.ssreader.novel.ui.utils.MyToash;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import static com.ssreader.novel.constant.Constant.*;

public class MyShare {

    public static boolean IS_SHARE;
    public int flag;
    public long id;
    public Activity activity;

    public MyShare(Activity activity) {
        this.activity = activity;
    }

    public int getFlag() {
        return flag;
    }

    public MyShare setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public long getId() {
        return id;
    }

    public MyShare setId(long id) {
        this.id = id;
        return this;
    }

    public void Share() {
        chapterShare(activity, getId(), 0, getFlag() + 1);
    }

    /**
     * 分享(要在界面加onActivityResult)
     */
    public void ShareAPP() {
        final ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.APP_SHARE, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            ShareBean shareBean = HttpUtils.getGson().fromJson(response, ShareBean.class);
                            if (!TextUtils.isEmpty(shareBean.link) && (shareBean.link.startsWith("www") ||
                                    shareBean.link.startsWith("http"))) {
                                // 使用本地分享
                                setShare(activity, shareBean.link, shareBean.title, shareBean.desc);
//                                if (Constant.USE_PAY) {
//                                    ShareCompleteToSendHttp(activity);
//                                }
//                                if (MyShare.isEnableShare(activity)) {
//                                    ShareDialogFragment shareDialogFragment = new ShareDialogFragment(activity, shareBean);
//                                    shareDialogFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "ShareDialogFragment");
//                                }
                            } else {
                                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.share_noUrl));
                            }
                        } catch (Exception e) {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.share_noUrl));
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    /**
     * 章节分享接口
     *
     * @param activity
     * @param novel_id
     * @param chapter_id
     * @param type
     */
    public static void chapterShare(Activity activity, long novel_id, long chapter_id, int type) {
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("type", type);
        readerParams.putExtraParams("novel_id", novel_id);
        if (chapter_id != 0) {
            readerParams.putExtraParams("chapter_id", chapter_id);
        }
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.NOVEL_SHARE, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    ShareBean shareBean = HttpUtils.getGson().fromJson(response, ShareBean.class);
                    if (!TextUtils.isEmpty(shareBean.link) && (shareBean.link.startsWith("www") ||
                            shareBean.link.startsWith("http"))) {
                        // 使用本地分享
                        setShare(activity, shareBean.link, shareBean.title, shareBean.desc);
//                        if (Constant.USE_PAY) {
//                            ShareCompleteToSendHttp(activity);
//                        }
//                        if (MyShare.isEnableShare(activity)) {
//                            ShareDialogFragment shareDialogFragment = new ShareDialogFragment(activity, shareBean);
//                            shareDialogFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "ShareDialogFragment");
//                        }
                    } else {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.share_noUrl));
                    }
                } catch (Exception e) {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.share_noUrl));
                }
            }

            @Override
            public void onErrorResponse(String ex) {

            }
        });
    }

    /**
     * 分享完成请求接口
     */
    private static void ShareCompleteToSendHttp(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            activity = BWNApplication.applicationContext.getActivity();
        }
        final ReaderParams params = new ReaderParams(activity);
        Activity finalActivity = activity;
        HttpUtils.getInstance().sendRequestRequestParams(finalActivity, Api.ShareAddGold, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String tip = jsonObject.getString("tip");
                            EventBus.getDefault().post(new RefreshMine());
                            MyToash.ToashSuccess(finalActivity, tip);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    /**
     * 系统分享
     *
     * @param activity
     * @param uri
     * @param title
     * @param desc
     */
    public static void setShare(Activity activity, String uri, String title, String desc) {
        if (TextUtils.isEmpty(title) || desc.equals("null")) {
            title = "";
        }
        if (TextUtils.isEmpty(desc) || desc.equals("null")) {
            desc = "";
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // 内容
        if (TextUtils.isEmpty(title)) {
            intent.putExtra(Intent.EXTRA_TEXT, desc + uri);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, "【" + title + "】" + desc + uri);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, title));
    }


    /**
     * @param activity
     * @return 是否可以分享
     */
    public static boolean isEnableShare(Activity activity) {
        if (!(SystemUtil.checkAppInstalled(activity, SystemUtil.WEChTE_PACKAGE_NAME) && USE_WEIXIN) &&
                !((SystemUtil.checkAppInstalled(activity, SystemUtil.QQ_PACKAGE_NAME) && USE_QQ) ||
                        (SystemUtil.checkAppInstalled(activity, SystemUtil.TIM_PACKAGE_NAME) && USE_QQ))) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.share_fail_no_app));
            return false;
        }
        return true;
    }
}
