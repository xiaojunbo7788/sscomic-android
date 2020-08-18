package com.ssreader.novel.ui.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ssreader.novel.R;
import com.ssreader.novel.model.BaseComicImage;
import com.ssreader.novel.utils.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.ssreader.novel.utils.FileManager.getLocalComicImageFile;

public class MyGlide {

    public static void GlideImageNoSize(Activity activity, String url, ImageView imageView) {
        GlideImageNoSize(activity, url, imageView, R.mipmap.icon_comic_def);
    }

    public static void GlideImageNoSize(Activity activity, String url, ImageView imageView, int def_id) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def_w)    //加载成功之前占位图
                    .error(def_id)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像
            setGlide(activity, url, imageView, options);
        }
    }

    public static RequestOptions getRequestOptions(Context activity, int radius) {
        if (radius != 0) {
            return new RequestOptions()
                    .error(R.mipmap.icon_comic_def_w)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    .transforms(new CenterCrop(), new RoundedCornersTransformation(ImageUtil.dp2px(activity, radius), 0));
        } else {
            return new RequestOptions()
                    .error(R.mipmap.icon_comic_def_w)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL);       //缓存所有版本的图像
        }
    }

    public static RequestOptions getRequestOptions(Context activity, int radius,int def) {
        if (radius!=0) {
            return new RequestOptions()
                    .error(def)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    .transforms(new CenterCrop(), new RoundedCornersTransformation(ImageUtil.dp2px(activity, radius), 0));
        }else {
            return new RequestOptions()
                    .error(def)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL) ;       //缓存所有版本的图像
        }
    }

    public static void setGlide(Activity activity, String url, ImageView imageView, RequestOptions options, MyTarget.GetFirstReadImage getFirstReadImage) {
        try {
            Glide.with(activity).load(url).apply(options).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    getFirstReadImage.getFirstReadImage(null);
                    return false;
                }
            }).into(imageView);

        } catch (Throwable e) {
        }
    }
    /**
     * 加载用户头像
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void GlideImageHeadNoSize(Activity activity, String url, ImageView imageView) {
        if (url == null) {
            if (!activity.isFinishing()) {
                Glide.with(activity).load(R.mipmap.icon_def_head)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imageView);
            }
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def_w)    //加载成功之前占位图
                    .error(R.mipmap.icon_def_head)    //加载错误之后的错误图
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像
            if (!activity.isFinishing()) {
                Glide.with(activity).load(url)
                        .apply(options)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imageView);
            }
        }
    }

    /**
     * 用于加载漫画图片
     *
     * @param activity
     * @param width
     * @param height
     * @param comicImage
     * @param imageView
     * @param glideLoadListener
     */
    public static void GlideImage(Activity activity, int width, int height, BaseComicImage comicImage,
                                  ImageView imageView, OnGlideLoadListener glideLoadListener) {
        File localPathFile = getLocalComicImageFile(comicImage);
        if (localPathFile != null) {
            if (localPathFile.exists()) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.icon_comic_def_w)        //加载成功之前占位图
                        .error(R.mipmap.icon_comic_def_w)        //加载错误之后的错误图
                        .skipMemoryCache(false)
                        .override(width, height)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                byte[] bytes = FileManager.readFile(localPathFile.getAbsolutePath());
                Glide.with(activity)
                        .load(bytes)
                        .apply(options)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (glideLoadListener != null) {
                                    glideLoadListener.loadFail();
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(imageView);
            } else {
                String url = comicImage.image;
                if (url == null || url.length() == 0) {
                    return;
                } else {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.icon_comic_def_w)        //加载成功之前占位图
                            .error(R.mipmap.icon_comic_def_w)        //加载错误之后的错误图
                            .skipMemoryCache(false)
                            .override(width, height)
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .diskCacheStrategy(DiskCacheStrategy.NONE);    //缓存所有版本的图像
                    Glide.with(activity).load(url).apply(options).into(imageView);
                }
            }
        }
    }

    /**
     * 加载图片,结合RecyclerView
     *
     * @param activity
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void GlideImage(Activity activity, String url, ImageView imageView, int width, int height) {
        if (activity == null || imageView == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.icon_comic_def_w)    //加载成功之前占位图
                .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图
                //指定图片的尺寸
                .override(width, height)
                .centerCrop()
                .skipMemoryCache(false)        //
                .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像
        setGlide(activity, url, imageView, options);
    }

    /**
     * 加载图片,结合RecyclerView
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void GlideImage(Activity activity, String url, ImageView imageView) {
        if (activity == null || imageView == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.icon_comic_def_w)    //加载成功之前占位图
                .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图
                .centerCrop()
                .skipMemoryCache(false)        //
                .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像
        setGlide(activity, url, imageView, options);
    }

    private static void setGlide(Activity activity, String url, ImageView imageView, RequestOptions options) {
        try {
            Glide.with(activity).load(url).apply(options).into(imageView);
        } catch (Throwable e) {
        }
    }

    /**
     * 没有加载前的预览图
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void glideNoPlaceholder(Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def_w)
                    .error(R.mipmap.icon_comic_def_w)    //加载错误之后的错误图
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像
            setGlide(activity, url, imageView, options);
        }
    }


    public static void GlideImageRoundedCorners(int radius, Activity activity, String url, ImageView imageView, int width, int height) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def_w)
                    .error(R.mipmap.icon_comic_def_w)    //加载错误之后的错误图
                    .override(width, height)
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    .transforms(new CenterCrop(), new RoundedCornersTransformation(com.ssreader.novel.ui.utils.ImageUtil.dp2px(activity, radius), 0));
            setGlide(activity, url, imageView, options);
        }
    }

    /**
     * 圆角image，带宽高
     * @param radius
     * @param activity
     * @param width
     * @param height
     * @param url
     * @param imageView
     */
    public static void GlideImageRoundedCornersNoSize(int radius, Activity activity,int width, int height, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def_w)
                    .error(R.mipmap.icon_comic_def_w)    //加载错误之后的错误图
                    .skipMemoryCache(false)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    .transforms(new CenterCrop(), new RoundedCornersTransformation(com.ssreader.novel.ui.utils.ImageUtil.dp2px(activity, radius), 0));
            setGlide(activity, url, imageView, options);
        }
    }

    /**
     * 圆角image
     * @param radius
     * @param activity
     * @param url
     * @param imageView
     */
    public static void GlideImageRoundedCornersNoSize(int radius, Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def_w)
                    .error(R.mipmap.icon_comic_def_w)    //加载错误之后的错误图
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    .transforms(new CenterCrop(), new RoundedCornersTransformation(com.ssreader.novel.ui.utils.ImageUtil.dp2px(activity, radius), 0));
            setGlide(activity, url, imageView, options);
        }
    }

    @SuppressLint("CheckResult")
    public static void GlideImageRoundedGasoMohu(Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def_w)    //加载成功之前占位图
                    .error(R.mipmap.icon_comic_def_w)    //加载错误之后的错误图
                    .transform(new BlurTransformation(7, 8))
                    .skipMemoryCache(true);
            Glide.with(activity)
                    .load(url)
                    .apply(options)
                    .into(imageView);
        }
    }

    public static void GlideCicleHead(Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            imageView.setImageResource(R.mipmap.icon_def_head);
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_def_head)    //加载成功之前占位图
                    .error(R.mipmap.icon_def_head)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL);        //缓存所有版本的图像
            RequestOptions mRequestOptions = RequestOptions.circleCropTransform().apply(options);
            Glide.with(activity).load(url).apply(mRequestOptions).into(imageView);
        }
    }

    public static void GlideCicleHeadDouyin(Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            imageView.setImageResource(R.mipmap.icon_def_head);
            return;
        } else {
            Glide.with(activity).load(url)
                    .apply(new RequestOptions()
                            .error(R.mipmap.icon_def_head)    //加载错误之后的错误图
                            .placeholder(R.mipmap.icon_def_head).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new com.ssreader.novel.ui.utils.GlideCircleWithBorder(activity, 1, Color.WHITE))).into(imageView);
        }
    }

    @SuppressLint("CheckResult")
    public static void DownLordImage(Activity activity, String imgURL, String name) {
        MyToash.Log("DownLordImage", imgURL);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Observable.create(new ObservableOnSubscribe<File>() {
                @Override
                public void subscribe(ObservableEmitter<File> e) throws Exception {
                    //通过gilde下载得到file文件,这里需要注意android.permission.INTERNET权限
                    e.onNext(Glide.with(activity)
                            .load(imgURL)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get());
                    e.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) throws Exception {
                            FileManager.GlideCopy(file, FileManager.getSquareBigImgs(name));
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.activity_square_savesuccess));

                                }
                            });
                        }
                    });
        }
    }

    /**
     * glide加载失败监听
     */
    public interface OnGlideLoadListener{

        void loadFail();
    }
}





