package com.ssreader.novel.ui.bwad;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告池
 */
public class AdReadCachePool {

    public TTSplashAd ttSplashAd;
    private static volatile AdReadCachePool adReadCachePool;

    public static AdReadCachePool getInstance() {
        if (adReadCachePool == null) {
            synchronized (AdReadCachePool.class) {
                if (adReadCachePool == null) {
                    adReadCachePool = new AdReadCachePool();
                }
            }
        }
        return adReadCachePool;
    }

    private AdReadCachePool() {
        adPoolBeenList = new ArrayList<>();
    }

    public List<AdPoolBeen> adPoolBeenList;
    public BaseAd baseAdCenter, baseAdButtom;
    private int current_show;

    public void putAdPoolBeen(AdPoolBeen adPoolBeen) {
        if (adPoolBeenList == null) {
            adPoolBeenList = new ArrayList<>();
        }
        if (!adPoolBeenList.contains(adPoolBeen)) {
            adPoolBeenList.add(adPoolBeen);
        }
    }

    public AdPoolBeen adPoolBeen;

    public AdPoolBeen gutAdPoolBeen() {
      /*  if (adPoolBeenList != null && !adPoolBeenList.isEmpty()) {
            int size = adPoolBeenList.size();
            if (current_show >= size) {
                current_show = 0;
            }
            AdPoolBeen adPoolBeen = adPoolBeenList.get(current_show);
            // MyToash.Log("adPoolBeenList",current_show+"");
            ++current_show;
            return adPoolBeen;
        }*/
        return adPoolBeen;
    }

    public AdPoolBeen gutAdPoolBeen(String addTime) {
        if (adPoolBeenList != null && !adPoolBeenList.isEmpty()) {
            for (AdPoolBeen adPoolBeen : adPoolBeenList) {
                if (adPoolBeen.addTime.equals(addTime)) {
                    return adPoolBeen;
                }
            }
        }
        return null;
    }

    /**
     * 获取广告
     *
     * @param activity
     */
    public void putBaseAd(Activity activity) {
        // 小说阅读器底部
        AdHttp.getWebViewAD(activity, 1, 12, new AdHttp.GetBaseAd() {
            @Override
            public void getBaseAd(BaseAd baseAdd) {
                if (baseAdd != null) {
                    if (!TextUtils.isEmpty(baseAdd.ad_android_key) || !TextUtils.isEmpty(baseAdd.ad_image)) {
                        baseAdButtom = baseAdd;
                    }
                }
            }
        });
        // 小说阅读器章节中
        AdHttp.getWebViewAD(activity, 1, 8, new AdHttp.GetBaseAd() {
            @Override
            public void getBaseAd(BaseAd baseAdd) {
                if (baseAdd != null) {
                    if (!TextUtils.isEmpty(baseAdd.ad_android_key) || !TextUtils.isEmpty(baseAdd.ad_image)) {
                        baseAdCenter = baseAdd;
                    }
                }
            }
        });
        // 视频广告
        AdHttp.getWebViewAD(activity, 1, 13, new AdHttp.GetBaseAd() {
            @Override
            public void getBaseAd(BaseAd baseAdd) {
                if (baseAdd != null) {
                    ShareUitls.putString(activity, "USE_AD_VIDEO_CODE", baseAdd.getAd_android_key());
                    ShareUitls.putString(activity, "USE_AD_VIDEO_Advert_id", baseAdd.getAdvert_id());
                }
            }
        });
    }

    public BaseAd getReadBaseAd(int flag) {
        return flag == 0 ? baseAdCenter : baseAdButtom;
    }
}
