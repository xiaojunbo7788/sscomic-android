package com.ssreader.novel.ui.bwad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.ssreader.novel.R;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.WebViewActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyTarget;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.RegularUtlis;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.ui.bwad.AdHttp.adClick;

public class WebAdshow {
    public static void webAdshow(Activity activity, FrameLayout frameLayout, BaseAd baseAd, int flag, BaseAd.GetttAdShowBitamp getttAdShowBitamp) {
        ImageView imageView = frameLayout.findViewById(R.id.list_ad_view_img);
        if (imageView == null) {
            imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (baseAd.ad_width != 0) {
                params.width = baseAd.ad_width;
            }
            if (baseAd.ad_height != 0) {
                params.height = baseAd.ad_height;
            }
            frameLayout.addView(imageView, params);
        }
        if (flag != -1 && getttAdShowBitamp != null) {
            ImageView finalImageView = imageView;
            MyGlide.setGlide(activity, baseAd.ad_image, imageView, MyGlide.getRequestOptions(activity, 8), new MyTarget.GetFirstReadImage() {
                @Override
                public void getFirstReadImage(Bitmap bitmap) {
                    frameLayout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 8), Color.WHITE));
                    getttAdShowBitamp.getttAdShowBitamp(finalImageView, 0);
                }
            });
        } else {
            if (flag != 3) {
                MyGlide.GlideImageRoundedCornersNoSize(8, activity, baseAd.ad_image, imageView);
            } else {
                MyGlide.GlideImage(activity, baseAd.ad_image, imageView);
            }
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(baseAd.ad_skip_url)) {
                        long ClickTimeNew = System.currentTimeMillis();
                        if (ClickTimeNew - ClickTime > AgainTime) {
                            ClickTime = ClickTimeNew;
                            ClickWebAd(activity, baseAd, flag);
                        }
                    } else {
                        MyToash.ToashError(activity, R.string.web_ad_url_error);
                    }
                }
            });
        }


    }

    private static long ClickTime;
    public static void ClickWebAd(Activity activity, BaseAd baseAd, int flag) {
        Intent intent = new Intent();
        intent.setClass(activity, WebViewActivity.class);
        intent.putExtra("url", baseAd.ad_skip_url);
        intent.putExtra("title", baseAd.ad_title);
        intent.putExtra("advert_id", baseAd.advert_id);
        intent.putExtra("ad_url_type", baseAd.ad_url_type);
        intent.putExtra("is_otherBrowser", baseAd.ad_url_type == 2);
        activity.startActivity(intent);
        adClick(activity, baseAd, flag, null);
    }

}
