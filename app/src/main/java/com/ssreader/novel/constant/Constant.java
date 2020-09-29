package com.ssreader.novel.constant;

import android.app.Activity;
import android.content.Context;

import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

/**
 * 松鼠仓库
 */
public class Constant {

    //TODO:线上
    // appKey
//    public static final String mAppkey = "wCDMZq0BYWMyd6qD";
//    // appSecret
//    public static final String mAppSecretKey = "lAB9XrdwCSWTvxjzTlfT7Nx6FSWGLamG";
//    // IP域名
//    public static final String BASE_URL = "https://app.sscomic.life";
//    //图片路径
//    public static final String IMAGE_URL = "https://www.syslly.com/";

    //TODO:测试
    // appKey
    public static final String mAppkey = "0557723a55501453";
    // appSecret
    public static final String mAppSecretKey = "caee1f9922be06cfc2016107fd303182";
    // IP域名
    public static final String BASE_URL =  "http://api.sscomic.life";
//     图片路径
    public static final String IMAGE_URL = "http://backend.songshucangku.com/";


    // 是否打开Log日志开关
    public final static boolean SUE_LOG = true;
    // 友盟统计
    public static final String UMENG = "5f29503db4b08b653e91316f";
    // 微信appId
    public static final String WEIXIN_PAY_APPID = "";
    // 微信secret
    public static final String WEIXIN_APP_SECRET = "";
    // QQ id
    public static final String QQ_APPID = "";
    // QQ secret
    public static final String QQ_SECRET = "";
    // 穿山甲广告
    public static final String TTAdID = "5040994";
    // 讯飞appId
    public static final String KDXF_APPID = "";

    /**
     * 获取站点类型
     */
    private static List<String> productTypeList = new ArrayList<>();

    public static List<String> getProductTypeList(Activity activity) {
        if (productTypeList == null || productTypeList.isEmpty()) {
            productTypeList = ShareUitls.getDataList(activity, "ProductTypeList", String.class);
        }
        if (productTypeList.isEmpty()) {
            productTypeList.add("2");
        }
        return productTypeList;
    }

    public static void setProductTypeList(Activity activity, List<String> productTypeList) {
        Constant.productTypeList = productTypeList;
        ShareUitls.setDataList(activity, "ProductTypeList", productTypeList);
    }

    /**
     * 获取打赏站点类型
     */
    public static int rewardswitch = -1;

    public static void setRewardSwitch(Activity activity, int rewardswitch) {
        Constant.rewardswitch = rewardswitch;
        ShareUitls.getInt(activity, "rewardswitch", rewardswitch);
    }

    public static int getRewardSwitch(Activity activity) {
        if (rewardswitch == -1) {
            rewardswitch = ShareUitls.getInt(activity, "rewardswitch", 0);
        }
        return rewardswitch;
    }

    /**
     * 获取月票站点类型
     */
    public static int monthlyticket = -1;

    public static void setMonthlyTicket(Activity activity, int monthlyticket) {
        Constant.monthlyticket = monthlyticket;
        ShareUitls.getInt(activity, "monthlyticket", monthlyticket);
    }

    public static int getMonthlyTicket(Activity activity) {
        if (monthlyticket == -1) {
            monthlyticket = ShareUitls.getInt(activity, "monthlyticket", 0);
        }
        return monthlyticket;
    }
    public static boolean LordNext = true;


    /**
     * 获取刘海屏的高度
     */
    private static int NotchHeight = -1;

    public static int GETNotchHeight(Activity activity) {
        if (NotchHeight == -1) {
            NotchHeight = ShareUitls.getInt(activity, "NotchHeight", 0);
        }
        return NotchHeight;
    }

    public static void PUTNotchHeightE(Activity activity, int N) {
        NotchHeight = N;
        ShareUitls.putInt(activity, "NotchHeight", NotchHeight);
    }

    // 产品类型
    public static final int BOOK_CONSTANT = 0;//小说
    public static final int COMIC_CONSTANT = 1;//漫画
    public static final int AUDIO_CONSTANT = 2;//听书

    public static final int MIANFEI = 0;//免费频道
    public static final int WANBEN = 1;//完本频道
    public static final int SHUKU = 2;//书库频道
    public static final int PAIHANG = 3;//排行频道
    public static final int PAIHANGINSEX = 10;//排行首页频道
    public static final int BAOYUE = 4;//包月
    public static final int BAOYUE_SEARCH = 5;//包月赛选列表

    public static final int DOWN = 6;//下载频道
    public static final int READHISTORY = 7;//阅读历史频道
    public static final int LIUSHUIJIELU = 8;//流水记录
    public static final int LOOKMORE = 9;//查看更多
    public static final int MYCOMMENT = 11;//我的评论
    public static final int MONTHTICKETHISTORY = 12;
    public static final int REWARDHISTORY = 13;
    //收藏
    public static final int COLLECTION = 14;
    public static final int COLLECTION_CONTENT_LIST = 15;
    public static final int TAG_LIST = 16;

    // 书城向下滑动距离
    public static final int REFRESH_HEIGHT = 100;
    // 书城向上滑动距离
    public static final int REFRESH_HEIGHT_TOP = 300;
    // 获取手机能显示图片的最高高度
    public static int MAXheigth;
    // 用于判断是否是本地书籍
    public static int LOCAL_BOOKID = 500000;
    // 防止多次点击
    public static final long AgainTime = 1000;

    public static final String AUTOBUY = "AUTOBUY";

    public static int getMAXheigth() {
        if (MAXheigth == 0) {
            MAXheigth = ImageUtil.getOpenglRenderLimitValue();
        }
        return MAXheigth;
    }

    // 是否使用广告
    public static final boolean USE_AD_FINAL = true;

    public static final int READ_AD_CENTER_PAGE = 5;

    public static int getReadAdCenterPage(Context activity) {
        return READ_AD_CENTER_PAGE;
    }

    // 是否使用游客登录
    public static final boolean DEVICE_LOGIN = false;
    // 是否默认使用游客登录
    public static final boolean DEVICE_LOGIN_DEF = false;
    // 是否使用国际化语言选择
    public static final boolean USE_LANAGUAGE = false;
    // 是否启用支付
    public static boolean USE_PAY = true;
    // 是否使用微信
    public static boolean USE_WEIXIN = false;
    // 是否使用QQ
    public static boolean USE_QQ = false;
    // 是否使用分享
    public static boolean USE_SHARE = true;
    // 是否使用AI
    public static boolean USE_AUDIO_AI = false;

    /**
     * @param activity
     * @return 是否有小说阅读器底部广告
     */
    public static boolean getIsReadBottomAd(Context activity) {
        return ShareUitls.getBoolean(activity, "USE_AD_READBUTTOM", false);
    }

    /**
     * @param activity
     * @return 是否有小说阅读器中部广告
     */
    public static boolean getIsReadCenterAd(Context activity) {
        if (getReadAdCenterPage(activity) <= 0) {
            return false;
        }
        return ShareUitls.getBoolean(activity, "USE_AD_READCENDET", false);
    }

    /**
     * @param activity
     * @return 是否有Home在回到前台广告
     */
    public static boolean getUSE_AD_HOME(Context activity) {
        return ShareUitls.getBoolean(activity, "USE_AD_HOME", false);
    }

    /**
     * @param activity
     * @return 是否使用视频广告
     */
    public static boolean getIsUseVideoAd(Context activity) {
        return ShareUitls.getBoolean(activity, "USE_AD_VIDEO", false);
    }

    public static boolean getIsReadBottomAd_COMIC(Activity activity) {
        return ShareUitls.getBoolean(activity, "USE_AD_READCENDET_COMIC", false);
    }

    // 一级货币单位
    public static String currencyUnit;
    // 二级货币单位
    public static String subUnit;

    public static String getCurrencyUnit(Activity activity) {
        if (currencyUnit == null) {
            currencyUnit = ShareUitls.getString(activity, "currencyUnit", "书币");
        }
        return currencyUnit;
    }

    public static String getSubUnit(Activity activity) {
        if (subUnit == null) {
            subUnit = ShareUitls.getString(activity, "subUnit", "书券");
        }
        return subUnit;
    }

    // 漫画弹幕
    private static boolean isComicDanmu;

    public static boolean isIsComicDanmu(Activity activity) {
        if (!isComicDanmu) {
            isComicDanmu = ShareUitls.getBoolean(activity, "isComicDanmu", true);
        }
        return isComicDanmu;
    }

    public static void setComicIsDanmu(Activity activity, boolean flag) {
        isComicDanmu = flag;
        ShareUitls.putBoolean(activity, "isComicDanmu", flag);
    }

    /**
     * @param activity
     * @return 阅读器底部高度
     */
    public static int getReadBottomHeight(Context activity) {
        return ImageUtil.dp2px(activity, 60);
    }
}
