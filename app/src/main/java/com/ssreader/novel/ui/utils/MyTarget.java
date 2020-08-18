package com.ssreader.novel.ui.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ssreader.novel.R;

public class MyTarget implements Target<Bitmap> {

    private GetFirstReadImage getFirstReadImage;
    private Activity activity;

    public MyTarget(Activity activity, GetFirstReadImage getFirstReadImage) {
        this.getFirstReadImage = getFirstReadImage;
        this.activity = activity;
    }

    public interface GetFirstReadImage {
        void getFirstReadImage(Bitmap bitmap);
    }

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {

    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        Bitmap resource;
        try {
            BitmapDrawable bd = (BitmapDrawable) errorDrawable;
            resource = bd.getBitmap();
        } catch (Throwable T) {
            resource = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.icon_comic_def);
        }
        getFirstReadImage.getFirstReadImage(resource);
    }

    @Override
    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        getFirstReadImage.getFirstReadImage(resource);
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {

    }

    @Override
    public void getSize(@NonNull SizeReadyCallback cb) {

    }

    @Override
    public void removeCallback(@NonNull SizeReadyCallback cb) {

    }

    @Override
    public void setRequest(@Nullable Request request) {

    }

    @Nullable
    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
