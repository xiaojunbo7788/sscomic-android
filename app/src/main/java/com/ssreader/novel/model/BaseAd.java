package com.ssreader.novel.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ssreader.novel.R;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.ui.bwad.TTAdShow;
import com.ssreader.novel.ui.bwad.WebAdshow;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;

public class BaseAd {

    public String advert_id;//
    public int ad_type;//": 1,   // 广告类型
    public String ad_title;//": "测试一下25",  // 标题
    public String ad_image;//":"http://ssreader.oss-cn-beijing.aliyuncs.com/comic/banner/f2feec8fb7743d4140cdcb0d6dd21124.jpg?x-oss-process=image%2Fresize%2Cw_600%2Ch_200%2Cm_lfit",    // 广告图
    public String ad_skip_url;//skip_url":"http://www.baidu.com", // 跳转地址
    public int ad_url_type;//'//'":1   // 跳转地址类型（1-内部跳转，2-外部跳转）
    public int ad_width;
    public int ad_height;//":160
    public String ad_android_key;
    public int time;//服务器时间

    public BaseAd(String advert_id) {
        this.advert_id = advert_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getAd_width() {
        return ad_width;
    }

    public void setAd_width(int ad_width) {
        this.ad_width = ad_width;
    }

    public int getAd_height() {
        return ad_height;
    }

    public void setAd_height(int ad_height) {
        this.ad_height = ad_height;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getAd_image() {
        return ad_image;
    }

    public void setAd_image(String ad_image) {
        this.ad_image = ad_image;
    }

    public String getAd_skip_url() {
        return ad_skip_url;
    }

    public void setAd_skip_url(String ad_skip_url) {
        this.ad_skip_url = ad_skip_url;
    }

    public int getAd_url_type() {
        return ad_url_type;
    }

    public void setAd_url_type(int ad_url_type) {
        this.ad_url_type = ad_url_type;
    }

    public String getAdvert_id() {
        return advert_id;
    }

    public void setAdvert_id(String advert_id) {
        this.advert_id = advert_id;
    }

    public int getAd_type() {
        return ad_type;
    }

    public void setAd_type(int ad_type) {
        this.ad_type = ad_type;
    }

    /**
     * 设置广告
     *
     * @param activity
     * @param frameLayout
     * @param flag
     * @return
     */
    public void setAd(Activity activity, FrameLayout frameLayout, int flag) {
        MyToash.Log("TTAdShow", flag + "  " + this.toString());
        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        int W20 = ImageUtil.dp2px(activity, 20);
        int W30 = ImageUtil.dp2px(activity, 30);
        int width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        int height = 0;
        if (flag == 0 || flag == -1) {
            width -= W30;
            height = (int) ((float) width / 1.2);
        } else if (flag == 3) {
            height = Constant.getReadBottomHeight(activity);
        } else if (flag == 5) {
            width -= W20;
            if (ad_type == 1) {
                height = (width * 4 / 7);
            } else {
                height = width / 3;
            }
        } else {
            if (flag == 2) {
                width -= W30;
            } else {
                width -= W20;
            }
            if (ad_type == 1) {
                if (getAd_width() != 0 && getAd_height() != 0) {
                    height = (width * getAd_height() / getAd_width());
                } else {
                    height = (width / 3);
                }
            } else if (ad_type == 2) {
                height = width / 84 * 31;
            }
        }
        if (flag != 0 && flag != 3) {
            layoutParams.width = width;
            layoutParams.height = height;
            frameLayout.setLayoutParams(layoutParams);
        }
        TTAdShow ttAdShow = null;
        if (ad_type == 1) {
            setAd_height(height);
            setAd_width(width);
            WebAdshow.webAdshow(activity, frameLayout, this, flag, getttAdShowBitamp);
            if (flag == 0) {
                getttAdShowBitamp.getttAdShowBitamp(null,1);
            }
        } else if (ad_type == 2) {
            setAd_height(ImageUtil.px2dip(activity, height));
            setAd_width(ImageUtil.px2dip(activity, width));
            if (flag != 0) {
                if (ttAdShow == null) {
                    ttAdShow = new TTAdShow(activity, flag, this);
                }
                if (flag != 3) {
                    frameLayout.removeAllViews();
                }
                ttAdShow.getTodayOneBanner(frameLayout, getttAdShowBitamp);
            } else {
                getttAdShowBitamp.getttAdShowBitamp(null,0);
            }
        }
    }

    public void setAd(Activity activity, FrameLayout frameLayout, int flag, GetttAdShowBitamp getttAdShowBitamp) {
        this.getttAdShowBitamp = getttAdShowBitamp;
        setAd(activity, frameLayout, flag);
    }

    public GetttAdShowBitamp getttAdShowBitamp;

    public interface GetttAdShowBitamp {

        void getttAdShowBitamp(View bitmap, int img);
    }

    public BaseAd() {

    }

    @Override
    public String toString() {
        return "BaseAd{" +
                "advert_id='" + advert_id + '\'' +
                ", ad_type=" + ad_type +
                ", ad_title='" + ad_title + '\'' +
                ", ad_image='" + ad_image + '\'' +
                ", ad_skip_url='" + ad_skip_url + '\'' +
                ", ad_url_type=" + ad_url_type +
                ", ad_width=" + ad_width +
                ", ad_height=" + ad_height +
                ", ad_android_key='" + ad_android_key + '\'' +
                '}';
    }

    public String getAd_android_key() {
        return ad_android_key;
    }

    public void setAd_android_key(String ad_android_key) {
        this.ad_android_key = ad_android_key;
    }


    public GetttAdShowBitamp getGetttAdShowBitamp() {
        return getttAdShowBitamp;
    }

    public void setGetttAdShowBitamp(GetttAdShowBitamp getttAdShowBitamp) {
        this.getttAdShowBitamp = getttAdShowBitamp;
    }
}
