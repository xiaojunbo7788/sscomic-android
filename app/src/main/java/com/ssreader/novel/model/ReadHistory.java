package com.ssreader.novel.model;

import android.app.Activity;
import android.text.TextUtils;

import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshReadHistory;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.utils.MainFragmentTabUtils;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.ssreader.novel.constant.Api.task_read;

public class ReadHistory extends PublicPage {

    public List<BaseReadHistory> list;

    /**
     * 添加历史
     *
     * @param activity
     * @param productType
     * @param id
     * @param chapterId
     */
    public static void addReadHistory(final Activity activity, int productType, final long id, long chapterId) {
        if (!InternetUtils.internet(activity) || !UserUtils.isLogin(activity)) {
            return;
        }
        ReaderParams params = new ReaderParams(activity);
        String url = "";
        if (productType == Constant.BOOK_CONSTANT) {
            params.putExtraParams("book_id", id);
            url = Api.add_read_log;
        } else if (productType == Constant.COMIC_CONSTANT) {
            params.putExtraParams("comic_id", id);
            url = Api.COMIC_read_log_add;
        } else if (productType == Constant.AUDIO_CONSTANT) {
            params.putExtraParams("audio_id", id);
            url = Api.AUDIO_ADD_READ_LOG;
        }
        params.putExtraParams("chapter_id", chapterId);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        HttpUtils.getInstance().sendRequestRequestParams(activity,url, params.generateParamsJson(),  new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        // 向历史界面发送通知(不在历史界面是收不到的)
                        EventBus.getDefault().post(new RefreshReadHistory(productType));
                        ReadTwoBook(activity, id);
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    /**
     * 阅读两本书的任务
     * @param activity
     * @param mBookId
     */
    public static void ReadTwoBook(Activity activity, long mBookId) {
        if (!Constant.USE_PAY) {
            return;
        }
       // MyToash.Log("ReadTwoBookDate",ShareUitls.getLong(activity,"ReadTwoBookDate",0)+"  "+mBookId);
        if(ShareUitls.getLong(activity,"ReadTwoBookDate",0)==mBookId){


            return;
        }
        ShareUitls.putLong(activity,"ReadTwoBookDate",mBookId);

        /*SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        final String ReadTwoBookDate = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        final String flag = ShareUitls.getString(activity, UserUtils.getToken(activity) + "ReadTwoBookDate", ReadTwoBookDate + "-0-false");
        String[] flag2 = flag.split("-");
        if (flag2[2].equals("true")) {
            return;
        }*/
        final ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,task_read, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        MainFragmentTabUtils.UserCenterRefarsh=true;
                        //EventBus.getDefault().post(new RefreshMine());
                       /* ShareUitls.putString(activity, UserUtils.getToken(activity) + "ReadTwoBookDate",
                                ReadTwoBookDate + "-" + mBookId + "-true");*/
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                    }
                }
        );
    }
}
